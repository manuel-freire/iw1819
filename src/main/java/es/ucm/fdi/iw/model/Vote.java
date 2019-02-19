package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
public class Vote {
	
	private long id;
	private User voter;
	private Question question;
	private int value;
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=User.class)
	public User getParticipant() {
		return voter;
	}
	public void setParticipant(User participant) {
		this.voter = participant;
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
}
