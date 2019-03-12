package es.ucm.fdi.iw.control;

import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("xs", "uno dos tres cuatro cinco".split(" "));
		return "index";
	}
	
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
}
