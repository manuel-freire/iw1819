package es.ucm.fdi.iw.control;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.service.UserService;

import es.ucm.fdi.iw.session.MySession;
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

	@GetMapping("/signin")
	public String login(Model model) {
		return "login";
	}
	
	@GetMapping("/users")
	public ModelAndView usersGet(ModelAndView modelAndView) {
		
		/*
		 * //To check if the user is logged
		User userLogged = MySession.getInstance().getUserLogged(modelAndView, session, status);
		if(userLogged != null) {
			//Do things
		}
		*/
		
		createExampleUsers();
		
		List<User> users = userService.getAll();

		modelAndView.addObject("users", users);
		modelAndView.setViewName("admin");
		return modelAndView;
	}
	
	/**
	 * Holds /register POST
	 * @return register view
	 */
	@RequestMapping(value= "/delete", method = RequestMethod.POST)
	public ModelAndView deleteUser(ModelAndView modelAndView, HttpSession session, SessionStatus status, @ModelAttribute ("userId") Long userId) {
		String err = "User not found";

		if(userId != null) {
			User user = userService.findById(userId);
			if(user != null) {
				if(user.isActive()) {
					user = userService.delete(user);
					if(user != null && !user.isActive()) {
						err = null;
						String msg = "User "+user.getName()+" ("+user.getEmail()+")"+
										" with id: "+userId+", has been deactivated";
						this.notifyModal(modelAndView, "User notification", msg);
					}
				}
				else {
					err = "The user with id: "+userId+" is already deactivated";
				}
			}
		}

		if(err != null) {
			this.notifyModal(modelAndView, "Error", err);
		}
		//If redirect to users the modal wont be rendered
		this.usersGet(modelAndView);
		
		return modelAndView;
	}
	
	@RequestMapping(value= "/delete-users", method = RequestMethod.POST)
	public ModelAndView deleteUsers(ModelAndView modelAndView, HttpSession session, SessionStatus status, @ModelAttribute ("userIdsToDelete") JSONArray userIdsToDelete) {
		String err = "";
		
		if(userIdsToDelete != null) {
			for(int i = 0; i < userIdsToDelete.length(); i++) {
				String errUserId = "";
				Long userId = null;
				try {
					userId = userIdsToDelete.getLong(i);
				} catch (JSONException e) {
					log.error("delete-users JSONArray parseLong exception", e);
				}
				
				if(userId != null) {
					User user = userService.findById(userId);
	
					errUserId += "User with id: "+userId+", not found";
				
					if(user != null) {
						if(user.isActive()) {
							user = userService.delete(user);
							if(user != null && !user.isActive()) {
								errUserId = "";
							}
						}
						else {
							errUserId = "The user with id: "+userId+" is already deactivated";
						}
					}
				}
				else {
					errUserId += "Error with user id";
				}
				err += errUserId + '\n';
			}
		}
		
		err = err.trim();

		if(err != null && err != "") {
			this.notifyModal(modelAndView, "Error", err);
		}
		//If redirect to users the modal wont be rendered
		this.usersGet(modelAndView);
		
		return modelAndView;
	}
	
	@GetMapping("/profile")
	public ModelAndView profile(ModelAndView modelAndView, HttpSession session, SessionStatus status, @ModelAttribute ("userId") Long userId) {
		
		String err = "User not found";

		if(userId != null) {
			User user = userService.findById(userId);
			if(user != null && user.isActive()) {
				err = null;
				modelAndView.addObject("user", user);
			}
		}
		
		if(err != null) {
			this.notifyModal(modelAndView, "Error", err);
		}
		
		modelAndView.setViewName("profile");
		return modelAndView;
	}
	
	@GetMapping("/modifyProfile")
	public ModelAndView modifyProfileGet(ModelAndView modelAndView, HttpSession session, SessionStatus status, @ModelAttribute ("userId") Long userId) {
		
		String err = "User not found";

		User user = null;
		if(userId != null) {
			user = userService.findById(userId);
			if(user != null && user.isActive()) {
				err = null;
				modelAndView.addObject("user", user);
			}
		}
		
		if(err != null) {
			this.notifyModal(modelAndView, "Error", err);
			modelAndView.setViewName("profile");
		}
		else {
			modelAndView.addObject("user", user);
			modelAndView.setViewName("modifyProfile");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("/modifyProfile")
	public ModelAndView modifyProfilePost(ModelAndView modelAndView, HttpSession session, SessionStatus status, @ModelAttribute ("user") User user)  {
		String err = "User not found";

		if(user != null) {
			User userDatabase = userService.findById(user.getId());
			if(userDatabase != null && userDatabase.isActive()) {
				//PARSE USER
				User userSaved = userService.save(user);
				if(userSaved != null) {
					err = null;
					this.notifyModal(modelAndView, "Saved changes", "Your data has been saved successfully!");
				}
			}
		}

		if(err != null) {
			this.notifyModal(modelAndView, "Error", err);
		}
		//If redirect to users the modal wont be rendered
		this.usersGet(modelAndView);
		
		return modelAndView;
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
