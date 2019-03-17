package es.ucm.fdi.iw.control;

import java.io.File;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.User;

@Controller()
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@GetMapping("/")
	public String index(Model model) {
		
		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
		model.addAttribute("groups", entityManager.createQuery(
				"SELECT g FROM CGroup g").getResultList());
		return "admin";
	}

	@GetMapping("/group")
	@Transactional
	public String index(HttpSession session, @RequestParam long id) {
		CGroup g = entityManager.find(CGroup.class, id);
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());
		if ( ! g.getParticipants().contains(u)) {
			g.getParticipants().add(u);
		}
		session.setAttribute("g",  g);
		return "redirect:/clase/";
	}
	
	@PostMapping("/deluser")
	@Transactional
	public String delUser(Model model,	@RequestParam long id) {
		User target = entityManager.find(User.class, id);
		File f = localData.getFile("user", ""+id);
		if (f.exists()) {
			f.delete();
		}
		entityManager.remove(target);
		return index(model);
	}	

	@PostMapping("/delgroup")
	@Transactional
	public String delGroup(Model model,	@RequestParam long id) {
		CGroup target = entityManager.find(CGroup.class, id);
		entityManager.remove(target);
		return index(model);
	}	
	
	@PostMapping("/addgroup")
	@Transactional
	public String addGroup(Model model, HttpSession session) {
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());			
		CGroup g = new CGroup();
		g.setCode(CGroup.createRandomId());
		g.getParticipants().add(u);
		entityManager.persist(g);		
		session.setAttribute("g",  g);
		return index(model);
	}	
}
