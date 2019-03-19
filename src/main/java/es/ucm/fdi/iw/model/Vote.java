package es.ucm.fdi.iw.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Expresses interest in a question by a student.
 * 
 * Questions can be either polls (such as "am I going too slow?" 
 * or "are you following the class?") or actual questions seeking
 * more complex answers from the teacher, such as "when should I use
 * POST vs GET?"). Both can have at most 1 outstanding vote per user,
 * in the range 0 to 100. Teachers&Students will see aggregates.
 * 
 * @author mfreire
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Vote.allLastByQuestion",
	query="SELECT v.voter.id, v.value FROM Vote v JOIN v.question q "
			+ "WHERE q.id = :questionId"
			+ " AND v.last = TRUE"),
	@NamedQuery(name="Vote.lastByVoterAndQuestion",
	query="SELECT v FROM Vote v JOIN v.question q JOIN v.voter u "
			+ "WHERE q.id = :questionId"
			+ "	AND u.id = :userId"
			+ " AND u.enabled = 1"
			+ " AND v.last = TRUE"),
	@NamedQuery(name="Vote.count",
	query="SELECT v.voter.id, COUNT(v) "
			+ "FROM Vote v "
			+ "GROUP BY v.voter.id")	
	})
public class Vote {
	
	private long id;
    @JsonView(Views.Public.class)
	private User voter;
	private Question question;
    @JsonView(Views.Public.class)
	private int value;
    @JsonView(Views.Public.class)
	private Timestamp time;
    @JsonView(Views.Public.class)
	private boolean last;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=User.class)
	public User getVoter() {
		return voter;
	}
	public void setVoter(User voter) {
		this.voter = voter;
	}
	@ManyToOne(targetEntity=Question.class)
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	
	/**
	 * Returns a JSON string representing votes for a question.
	 * 
	 * @param questions for which votes will be tallied
	 * @param manager to use for queries
	 * @return votes from enabled users, and only the last vote for each user.
	 * 			The format is {"vote": {"q_123": {"1": 11, "3": 33}}}
	 */
	public static String latestVotesForQuestion(EntityManager manager, Question ... questions) {
		List<String> lines = new ArrayList<String>();
		for (Question q : questions) {
			@SuppressWarnings("unchecked")
			List<Object[]> values = manager
				.createNamedQuery("Vote.allLastByQuestion")			
				.setParameter("questionId", q.getId())
				.getResultList();
			List<String> votes = new ArrayList<>();
			for (Object[] o : values) {
				votes.add("\"" + o[0] + "\": " + o[1]);
			}
			lines.add("\"q_" + q.getId() + "\": {" + String.join(",", votes) + "}");
		}
		return "{\"vote\": {" + String.join(",", lines) + "}}";
	}
}
