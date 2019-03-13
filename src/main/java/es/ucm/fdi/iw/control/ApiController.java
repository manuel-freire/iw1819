package es.ucm.fdi.iw.control;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import es.ucm.fdi.iw.model.CGroup;
import es.ucm.fdi.iw.model.Question;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Views;
import es.ucm.fdi.iw.model.Vote;

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
		CGroup g = (CGroup)session.getAttribute("g");
		Question q = entityManager.find(Question.class,  qid);
		if (q.getGroup().getId() != g.getId()) {
			throw new IllegalArgumentException(
					"Tried to vote on out-of-group question");
		}
		entityManager.persist(vote);
		q.getVotes().add(vote);
		iwSocketHandler.sendText("a", "new vote: " + vote.getValue());
		return vote;
	}
	
	@PostMapping("/q")
	@JsonView(Views.Public.class)
	@Transactional
	public Question ask(Model model, @ModelAttribute Question question) {
		
		User u = (User)session.getAttribute("u");
		u = entityManager.find(User.class, u.getId());
		question.setAuthor(u);
		
		CGroup g = (CGroup)session.getAttribute("g");
		g = entityManager.find(CGroup.class, g.getId());
		question.setGroup(g);
		
		question.setText(HtmlUtils.htmlEscape(question.getText()));
		entityManager.persist(question);
		g.getQuestions().add(question);
		u.getQuestions().add(question);
		
		return question;
	}
	
}
