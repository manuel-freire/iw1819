package es.ucm.fdi.iw.control;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.Question;
import es.ucm.fdi.iw.model.User;

@Controller()
@RequestMapping("clase")
public class ClassController {
	
	private static final Logger log = LogManager.getLogger(ClassController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	public String index(Model model, Principal principal, HttpSession session) {
		
		if (principal == null) {
			// force login
			return "enter";
		}
		CGroup g = entityManager.find(CGroup.class, 
				((CGroup)session.getAttribute("g")).getId());
		ArrayList<Question> polling = new ArrayList<>();
		ArrayList<Question> asking = new ArrayList<>();
		for (Question q : g.getQuestions()) {
			if (q.isPoll()) {
				polling.add(q);
			} else {
				asking.add(q);
			}
		}
		model.addAttribute("polling", polling);
		model.addAttribute("asking", asking);
		
		return "clase";
	}

	@GetMapping("/enter")
	public String getEnter(Model model) {
		return "enter";			
	}
	
	@PostMapping("/enter")
	@Transactional
	public String enter(Model model, HttpServletRequest request, Principal principal, 
			@RequestParam String userName, @RequestParam String groupCode, HttpSession session) {
		CGroup g = null;
		try {
	        g = entityManager.createNamedQuery("CGroup.ByCode", CGroup.class)
	                            .setParameter("groupCode", groupCode)
	                            .getSingleResult();
	        // if no exception here, the group code is valid - yay!
		} catch (Exception e) {
			@SuppressWarnings("unchecked")
			List<CGroup> groups = entityManager
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
        u.setRoles("STUDENT,USER");
        entityManager.persist(u);
        entityManager.flush();
        log.info("Creating & logging in student {}, with ID {} and password {}", userName, u.getId(), randomPassword);

        doAutoLogin(userName, randomPassword, request);	  
        log.info("Created & logged in student {}, with ID {} and password {}", userName, u.getId(), randomPassword);
		
        g.getParticipants().add(u);
        log.info("Added to group {}", g.getId());
        
        // add 'u' and 'g' session attributes
        session.setAttribute("u", u);
        session.setAttribute("g", g);
		// add a 'ws' session variable
		session.setAttribute("ws", request.getRequestURL().toString()
				.replaceFirst("[^:]*", "ws")		// http[s]://... => ws://...
				.replaceFirst("/clase.*", "/ws"));	
        
		return "redirect:/clase/";
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
