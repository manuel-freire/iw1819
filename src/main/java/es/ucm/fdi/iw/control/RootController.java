package es.ucm.fdi.iw.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< Updated upstream
=======
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.service.UserService;
import es.ucm.fdi.iw.session.MySession;
import es.ucm.fdi.util.StringUtil;
>>>>>>> Stashed changes

@Controller
public class RootController {
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;
	
<<<<<<< Updated upstream
=======
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	UserService userService;
	
	
>>>>>>> Stashed changes
	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		return "index";
	}
<<<<<<< Updated upstream

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/profile")
	public String profile(Model model) {
		return "profile";
	}
	
	@GetMapping("/modifyProfile")
	public String modifyProfile(Model model) {
		return "modifyProfile";
	}
=======
>>>>>>> Stashed changes
	
	@GetMapping("/chats")
	public String chats(Model model) {
		return "chats";
	}
	
	@GetMapping("/friends")
	public String friends(Model model) {
		return "friends";
	}
	
	@GetMapping("/groups")
	public String groups(Model model) {
		return "groups";
	}
	
	@GetMapping("/history")
	public String history(Model model) {
		return "history";
	}
	
	@GetMapping("/share")
	public String share(Model model) {
		return "share";
	}
	
	/*
	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		
		log.info("let us all welcome this admin, {}", principal.getName());
		
		return "index";
	}
	
	@GetMapping("/chat")
	public String chat(Model model, HttpServletRequest request) {
		model.addAttribute("socketUrl", request.getRequestURL().toString()
				.replaceFirst("[^:]*", "ws")
				.replace("chat", "ws"));
		return "chat";
	}
	*/
}
