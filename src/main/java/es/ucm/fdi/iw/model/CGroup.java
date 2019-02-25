package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Group of students.
 * 
 * This is the primary access point to the state of the
 * different questions in a class.
 * 
 * @author mfreire
 */
@Entity
@NamedQueries({
	@NamedQuery(name="CGroup.ByCode",
	query="SELECT g FROM CGroup g "
			+ "WHERE g.code = :groupCode")
})
public class CGroup {
	private long id;
	private String code;
	private List<Vote> votes = new ArrayList<>();
	private List<User> participants = new ArrayList<>();
	private List<User> owners = new ArrayList<>();

	/**
	 * Generates a random ID.
	 * @return the generated ID. If you are unlucky, it may conflict with
	 *      an existing one -- test it before assuming that it is unique!
	 */
	public String createRandomId() {
		return String.format("%06.6d", 
				ThreadLocalRandom.current().nextLong(1_000_000));
	}
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@OneToMany(targetEntity=Vote.class)
	@JoinColumn(name="group_id")
	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	@ManyToMany(targetEntity=User.class)
	public List<User> getParticipants() {
		return participants;
	}
	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	@Column(unique=true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "CGroup [id=" + id + ", code=" + code + "]";
	}
}
