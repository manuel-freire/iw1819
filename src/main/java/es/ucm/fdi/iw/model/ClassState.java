package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * State of the a class at a given instant.
 * @author mfreire
 */
@Entity
public class ClassState {
	private long id;
	private List<Vote> votes = new ArrayList<>();
	private List<Participant> participants = new ArrayList<>();
	//private List<User> owners = new ArrayList<>();

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@OneToMany(targetEntity=Vote.class)
	@JoinColumn(name="class_id")
	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	@OneToMany(targetEntity=Participant.class)
	@JoinColumn(name="class_id")
	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
}
