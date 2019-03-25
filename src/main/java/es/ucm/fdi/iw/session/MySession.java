package es.ucm.fdi.iw.session;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.iw.model.User;

public class MySession {
	
	private static MySession instance;
	
	public MySession() { }
	
	public static MySession getInstance() {
		if(instance == null) {
			instance = new MySession();
		}
		return instance;
	}
	
	public String userLoggedStr = "userLogged";

	public void setUserLogged(HttpSession session, User userDatabase) {
		if(userDatabase != null) {
			session.setAttribute(userLoggedStr, userDatabase);
		}
	}
	
	public User getUserLogged(ModelAndView modelAndView, HttpSession session, SessionStatus status) {
		User userLogged = (User) session.getAttribute(userLoggedStr);
		
		if (userLogged == null) {
			rejectUser(modelAndView, session, status);
			userLogged = null;
		}
		
		return userLogged;
	}
	
	public ModelAndView rejectUser(ModelAndView modelAndView, HttpSession session, SessionStatus status) {
		session.removeAttribute(userLoggedStr);
		session.invalidate();
		status.setComplete();
		modelAndView = new ModelAndView("redirect:/");
		return modelAndView;
	}
	
}
