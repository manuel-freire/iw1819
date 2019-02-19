package es.ucm.fdi.iw.control;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Group;

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
	
	@GetMapping("/")
	public String index(Model model) {
		return "test";
	}

	@PostMapping("/")
	public String login(Model model, @RequestParam String userName, @RequestParam String groupCode) {
		try {
	        Group g = entityManager.createNamedQuery("Group.ByCode", Group.class)
	                            .setParameter("groupCode", groupCode)
	                            .getSingleResult();
	        // if no exception here, the group code is valid - yay!
	
	        int usersWithLogin = entityManager.createNamedQuery("User.HasLogin", Integer.class)
                    .setParameter("userLogin", userName)
                    .getSingleResult();
	        // if the user exists, we have a problem
	        if (usersWithLogin != 0) {
	        	return "test";
	        }
	        
	        // ok, let us create & authenticate a student-user
	        
		} catch (Exception e) {
    		log.info("No such group code: {}", groupCode);
    		return "bad";
    	}
		return "test";
	}
}
