package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

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
    @JsonView(Views.Public.class)
	private String code;
    @JsonView(Views.Public.class)
	private List<Question> questions = new ArrayList<>();
    @JsonView(Views.Public.class)
	private List<User> participants = new ArrayList<>();
    @JsonView(Views.Public.class)
	private List<User> owners = new ArrayList<>();

	/**
	 * Generates a random ID.
	 * @return the generated ID. If you are unlucky, it may conflict with
	 *      an existing one -- test it before assuming that it is unique!
	 */
	public static String createRandomId() {
		return String.format("%06d", 
				ThreadLocalRandom.current().nextLong(1_000_000));
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@OneToMany(targetEntity=Question.class)
	@JoinColumn(name="group_id")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
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
