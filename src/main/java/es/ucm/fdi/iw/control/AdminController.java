package es.ucm.fdi.iw.control;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

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

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.User;

/**
 * Admin-only controller
 * @author mfreire
 */
@Controller()
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));

		model.addAttribute("users", entityManager.createQuery(
				"SELECT u FROM User u").getResultList());
		model.addAttribute("groups", entityManager.createQuery(
				"SELECT g FROM CGroup g").getResultList());
		model.addAttribute("questionCount", countsToMap("Question.count"));
		model.addAttribute("voteCount", countsToMap("Vote.count"));
		model.addAttribute("groupActivity", countsToMap("CGroup.activity"));
		
		return "admin";
	}

	/**
	 * Creates a map from a query, where the 1st column of results is used as key.
	 * @param queryName that returns unique long ids as 1st column
	 * @return a map using this id as key, and either
	 * 	- full rows, if each row has over 2 columns
	 *  - single values (those of the 2nd column), if each row has exactly 2 columns
	 */
	private Map<Long, Object> countsToMap(String queryName) {
		Map<Long, Object> m = new HashMap<>();
		@SuppressWarnings("unchecked")
		List<Object[]> results = entityManager.createNamedQuery(queryName).getResultList();
		for (Object[] row : results) {
			if (row.length == 2) {
				m.put((Long)row[0], row[1]);
			} else {
				m.put((Long)row[0], row);
			}
		}
		log.info("CountsToMap for {} returned {} rows", queryName, m.size());
		return m;
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
	
	@PostMapping("/toggleuser")
	@Transactional
	public String delUser(Model model,	@RequestParam long id) {
		User target = entityManager.find(User.class, id);
		if (target.getEnabled() == 1) {
			// disable
			File f = localData.getFile("user", ""+id);
			if (f.exists()) {
				f.delete();
			}
			target.setEnabled((byte)0); // disable user
		} else {
			// enable
			target.setEnabled((byte)1);
		}
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
