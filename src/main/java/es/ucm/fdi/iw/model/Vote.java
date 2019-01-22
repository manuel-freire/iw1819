package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * An individual act of participation by a student of a class.
 * @author mfreire
 */
@Entity
public class Vote {
	
	public enum Type {
		AskQuestion, VoteQuestion, VotePoll
	}
	
	private long id;
	private Type type;
	private Participant participant;
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
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@ManyToOne(targetEntity=Participant.class)
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
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
