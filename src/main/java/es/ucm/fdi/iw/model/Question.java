package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

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
	private long id;
	private CGroup group;
	private User author;
	private String text;
	private boolean poll;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}
