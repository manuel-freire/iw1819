package es.ucm.fdi.iw.control;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.IwUserDetailsService;
import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.User;

@Controller()
@RequestMapping("vote")
public class VoteController {
	
	private static final Logger log = LogManager.getLogger(VoteController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;

	@Autowired
	private IwUserDetailsService userDetailsService;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	public String index(Model model, Principal principal) {
		
		if (principal == null) {
			// force login
			return "vote";
		}
		
		//model.addAttribute("qs", Question.getQuestionsWithVotes(entityManager));
		return "class";
	}

	@GetMapping("/enter")
	public String getEnter(Model model) {
		return "enter";			
	}

	@PostMapping("/enter")
	@Transactional
	public String enter(Model model, HttpServletRequest request, Principal principal, 
			@RequestParam String userName, @RequestParam String groupCode) {
		CGroup g = null;
		try {
	        g = entityManager.createNamedQuery("CGroup.ByCode", CGroup.class)
	                            .setParameter("groupCode", groupCode)
	                            .getSingleResult();
	        // if no exception here, the group code is valid - yay!
		} catch (Exception e) {
			List<CGroup> groups = (List<CGroup>)entityManager
					.createQuery("select g from CGroup g").getResultList();
    		log.info("No such group code: {}; listing {} valid groups", groupCode, groups.size());
    		for (CGroup i : groups) {
    			log.info(i.toString());
    		}
    		return "bad";
    	}
		
        Long usersWithLogin = entityManager.createNamedQuery("User.HasLogin", Long.class)
                .setParameter("userLogin", userName)
                .getSingleResult();
        // if the user exists, we have a problem
        if (usersWithLogin != 0) {
        	return "test";
        }
        
        // ok, let us create & authenticate a student-user
        String randomPassword = passwordEncoder.encode("foo"); // result is quite random!
        User u = new User();
        u.setEnabled((byte)1);
        u.setLogin(userName);
        u.setPassword(passwordEncoder.encode(randomPassword));
        u.setGroups(new ArrayList<CGroup>());
        u.getGroups().add(g);
        u.setRoles("STUDENT");
        log.info("Creating & logging in student {}, with ID {} and password {}", userName, u.getId(), randomPassword);
        entityManager.persist(u);
        entityManager.flush();	// <-- flush before querying DB again (as part of autologin)
        
        List<User> users = (List<User>)entityManager
				.createQuery("select u from User u").getResultList();
		for (User i : users) {
			log.info(i.toString());
		}
        
        doAutoLogin(userName, randomPassword, request);	  
        log.info("Created & logged in student {}, with ID {} and password {}", userName, u.getId(), randomPassword);
        
		return "forward:/";
	}
	
	/**
	 * Non-interactive authentication; user and password must already exist
	 * @param username
	 * @param password
	 * @param request
	 */
	private void doAutoLogin(String username, String password, HttpServletRequest request) {
	    try {
	        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
	        token.setDetails(new WebAuthenticationDetails(request));
	        Authentication authentication = authenticationManager.authenticate(token);
	        log.debug("Logging in with [{}]", authentication.getPrincipal());
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        log.error("Failure in autoLogin", e);
	    }
	}
}
