package es.ucm.fdi.iw.control;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.GameMove;
import es.ucm.fdi.iw.model.GameState;
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
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid request")  // 401
	public static class ApiException extends RuntimeException {
	     public ApiException(String text, Throwable cause) {
	    	 super(text, cause);
	    	 if (cause != null) {
	    		 log.warn(text, cause);
	    	 } else {
	    		 log.info(text);
	    	 }
	     }
	}
	
	@GetMapping("/status/{groupCode}")
	@JsonView(Views.Public.class)
	public CGroup status(Model model, @PathVariable String groupCode) {
		return entityManager.createNamedQuery("CGroup.byCode", CGroup.class)
	                            .setParameter("groupCode", groupCode)
	                            .getSingleResult();
	}
	
	@PostMapping("/v/{qid}")
	@JsonView(Views.Public.class)
	@Transactional
	public Vote vote(Model model, @PathVariable long qid, 
			@RequestBody Vote vote, HttpSession session) throws JsonProcessingException {
		log.info("Voting on q={}", qid);
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());
		if (u.getEnabled() == 0) {
			session.invalidate();
			throw new ApiException("User has been disabled", null);
		}
		vote.setVoter(u);
		vote.setTime(new Timestamp(new Date().getTime()));
		CGroup g = (CGroup)session.getAttribute("g");
		Question q = entityManager.find(Question.class,  qid);
		if (q.getGroup().getId() != g.getId()) {
			throw new ApiException(
					"Tried to vote on out-of-group question", null);
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
		String message = Vote.latestVotesForQuestion(entityManager, q);
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
		if (u.getEnabled() == 0) {
			session.invalidate();
			throw new ApiException("User has been disabled", null);
		}
				
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
		if (u.getEnabled() == 0) {
			session.invalidate();
			throw new ApiException("User has been disabled", null);
		}
		
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
	
	@PostMapping("/e")
	@Transactional
	public GameState eval(@RequestBody GameMove gameMove) {
		GameState state = new GameState(); // retrieve from database!
		state.setJson("{un: 'objeto de estado', de: 123, ejemplo: false}");
		
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		// read script file
		try {
			engine.eval(new InputStreamReader(getClass().getResourceAsStream(
					"/static/js/game.js"))); // relativo a src/main/resources
		} catch (ScriptException e) {
			log.warn("Error loading script",  e);
		}

		Invocable inv = (Invocable) engine;
		Object result = null;
		// call function from script file
		try {
			result = inv.invokeFunction("prueba", state.getJson(), gameMove.getJson());
		} catch (NoSuchMethodException | ScriptException e) {
			log.warn("Error running script",  e);
		}
		log.warn(result);
		return null;
	}	
}
