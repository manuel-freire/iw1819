package es.ucm.fdi.iw.control;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller()
@RequestMapping("user")
public class UserController {
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
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
	
	private void loginTest(HttpSession session, User user) {
		MySession.getInstance().setUserLogged(session, user);
	}
	
	@GetMapping("/")
	public String index(ModelAndView modelAndView, HttpSession session) {
		createExampleUsers();
		loginTest(session, userService.getAll().get(0));
		
		return "index";
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
		this.index(modelAndView, session);
		
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
		
		Long userId = user.getId();
		modelAndView.addObject("userId", userId);
		this.profile(modelAndView, session, status, userId);
		
		return modelAndView;
	}

}
