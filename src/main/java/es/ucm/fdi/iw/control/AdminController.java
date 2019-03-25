package es.ucm.fdi.iw.control;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.service.UserService;
import es.ucm.fdi.util.StringUtil;

@Controller()
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	
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
	
	@GetMapping("/users")
	public ModelAndView usersGet(ModelAndView modelAndView) {
		
		/*
		 * //To check if the user is logged
		User userLogged = MySession.getInstance().getUserLogged(modelAndView, session, status);
		if(userLogged != null) {
			//Do things
		}
		*/
		
		List<User> users = userService.getAll();

		modelAndView.addObject("users", users);
		modelAndView.setViewName("admin");
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
	
}
