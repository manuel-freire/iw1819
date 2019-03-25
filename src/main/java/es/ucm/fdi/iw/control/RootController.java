package es.ucm.fdi.iw.control;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.service.UserService;
import es.ucm.fdi.util.StringUtil;


@Controller
public class RootController {
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;

	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	UserService userService;
	
	/**
	 * Function to notify the current user a message from server
	 * the modal is located on nav.html in order to display a message in any view
	 * @param model
	 * @param errorTitle
	 * @param errorMsg
	 */
	private void notifyModal(ModelAndView modelAndView, String title, String msg) {
		if(title != null && title != "" && msg != null && msg != "") {
			modelAndView.addObject("title", StringUtil.convertToTitleCaseSplitting(title));
			modelAndView.addObject("msg", msg);
		}
	}
	
	private void createExampleUsers() {
		List<User> users = userService.getAll();
		
		if(users.size() == 0) {
			User u = new User();
			u.setId(77777777);
			u.setEmail("ferlopezcarr@gmail.com");
			u.setNickname("ferlopezcarr");
			u.setName("Fernando");
			u.setLastName("López");
			u.setPassword("123456");
			u.setBirthdate(new Date());
			u.setDescription("Hola me llamo Fernando");
			u.setActive(true);
			userService.create(u);
			
			User u2 = new User();
			u2.setId(88888888);
			u2.setEmail("maria@gmail.com");
			u2.setNickname("maria");
			u2.setName("María");
			u2.setLastName("Sánchez");
			u2.setPassword("123456");
			u2.setBirthdate(new Date());
			u2.setDescription("Hola me llamo María");
			u2.setActive(true);
			userService.create(u2);
		}
	}
	
	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		return "index";
	}

	@GetMapping("/login")
	public ModelAndView login(ModelAndView modelAndView, 
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "logout", required = false) String logout
	) {
		
		if(error != null) {
			modelAndView.addObject("error", true);
		}
		if(logout != null) {
			notifyModal(modelAndView, "logout", "You have been logged out");
		}
		
		modelAndView.setViewName("login");
		
		return modelAndView;
	}
	
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
