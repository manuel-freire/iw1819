package es.ucm.fdi.iw.control;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.Question;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Views;
import es.ucm.fdi.iw.model.Vote;

/**
 * Rest controller for managing a class.
 * Asking questions, removing them, and voting are the main tasks. 
 * 
 * @author mfreire
 */
@RestController
@RequestMapping("api")
public class ApiController {
	
	private static final Logger log = LogManager.getLogger(ApiController.class);
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;
	
	@Autowired
	private HttpSession session;	
	
	@GetMapping("/status/{groupCode}")
	@JsonView(Views.Public.class)
	public CGroup status(Model model, @PathVariable String groupCode) {
		return entityManager.createNamedQuery("CGroup.ByCode", CGroup.class)
	                            .setParameter("groupCode", groupCode)
	                            .getSingleResult();
	}
	
	@PostMapping("/v/{qid}")
	@JsonView(Views.Public.class)
	@Transactional
	public Vote vote(Model model, @PathVariable long qid, 
			@RequestBody Vote vote) throws JsonProcessingException {
		log.info("Voting on q={}", qid);
		User u = (User)session.getAttribute("u");
		vote.setVoter(entityManager.find(User.class, u.getId()));
		vote.setTime(new Timestamp(new Date().getTime()));
		CGroup g = (CGroup)session.getAttribute("g");
		Question q = entityManager.find(Question.class,  qid);
		if (q.getGroup().getId() != g.getId()) {
			throw new IllegalArgumentException(
					"Tried to vote on out-of-group question");
		}
		vote.setQuestion(q);
		
		// ensure that previous votes are no longer the last one
		@SuppressWarnings("unchecked")
		List<Vote> last = entityManager
			.createNamedQuery("Vote.lastByVoterAndQuestion")
			.setParameter("userId", u.getId())
			.setParameter("questionId", q.getId())
			.getResultList();
		for (Vote v : last) { 
			log.info("No longer last: " + v.getId());
			v.setLast(false);
		}
		
		// add a date, set 'last' flag, and persist 
		vote.setLast(true);
		entityManager.persist(vote);
		q.getVotes().add(vote);
		
		// prepare updated vote results, and notify listeners
		@SuppressWarnings("unchecked")
		List<Integer> values = entityManager
			.createNamedQuery("Vote.allLastByQuestion")			
			.setParameter("questionId", q.getId())
			.getResultList();
		Collections.shuffle(values); // anonymization!
		String message = "{\"vote\": {"
				+ "\"q_" + q.getId() + "\": " + 
				Arrays.toString(values.toArray()) + "}}";
		for (User p : q.getGroup().getParticipants()) {
			iwSocketHandler.sendText(p.getLogin(), message);
		}
		return vote;
	}
	
	@PostMapping("/d/{qid}")
	@ResponseBody
	@Transactional
	public String delete(Model model, @PathVariable long qid, 
			HttpServletResponse response) {
		
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());
				
		Question q = entityManager.find(Question.class,  qid);
		if (q.getAuthor().getId() != u.getId() && ! u.hasRole("admin")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			log.warn("user {} tried to delete question {} from user {}",
					u.getId(), qid, q.getAuthor().getId());
			return "no es tu pregunta para borrarla, ni eres admin";
		}

		// notify all group members
		String message = "{\"deletion\": " + qid + "}";
		for (User p : q.getGroup().getParticipants()) {
			iwSocketHandler.sendText(p.getLogin(), message);
		}
		
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		entityManager.remove(q);
		return "hecho";
	}	
	
	@PostMapping("/q")
	@JsonView(Views.Public.class)
	@Transactional
	public Question ask(Model model, @RequestBody Question question) 
			 throws JsonProcessingException {
		
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());
		question.setAuthor(u);
		
		CGroup g = (CGroup)session.getAttribute("g");
		g = entityManager.find(CGroup.class, g.getId());
		question.setGroup(g);
		
		question.setTime(new Timestamp(new Date().getTime()));
		question.setText(HtmlUtils.htmlEscape(question.getText()));
		entityManager.persist(question);
		g.getQuestions().add(question);
		u.getQuestions().add(question);
		
		ObjectMapper mapper = new ObjectMapper();		
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		String questionAsJson = mapper
			      .writerWithView(Views.Public.class)
			      .writeValueAsString(question);
		String message = "{\"question\": "
				+ questionAsJson + "}";
		for (User p : g.getParticipants()) {
			iwSocketHandler.sendText(p.getLogin(), message);
		}		
		return question;
	}
	
}
