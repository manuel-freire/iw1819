package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * A question or comment by a student.
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
	@NamedQuery(name="Question.ValidVotes",
			query="SELECT NEW java.lang.Integer(v.value) "
					+ "FROM User u JOIN u.votes v "
					+ "WHERE u.enabled = 1 AND v.question = :questionId")
})
public class Question {
    @JsonView(Views.Public.class)	
	private long id;    
	private CGroup group;
    @JsonView(Views.Public.class)
	private List<Vote> votes;
    @JsonView(Views.Public.class)
	private User author;
    @JsonView(Views.Public.class)    
	private String text;
    @JsonView(Views.Public.class)    
	private boolean poll;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isPoll() {
		return poll;
	}
	public void setPoll(boolean poll) {
		this.poll = poll;
	}
	@ManyToOne(targetEntity=User.class)
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@ManyToOne(targetEntity=CGroup.class)
	public CGroup getGroup() {
		return group;
	}
	public void setGroup(CGroup group) {
		this.group = group;
	}
	@OneToMany(targetEntity=Vote.class)
	public List<Vote> getVotes() {
		return votes;
	}
	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}	
}
