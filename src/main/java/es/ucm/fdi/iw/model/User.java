package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * A user.
 * 
 * Note that, in this particular application, we will automatically be creating
 * users for students. Those users will have the group password as their "password", 
 * but will be generally unable to actually log in without the group password.  
 * 
 * @author mfreire
 */
@Entity
@NamedQueries({
	@NamedQuery(name="User.ByLogin",
	query="SELECT u FROM User u "
			+ "WHERE u.login = :userLogin"),
	@NamedQuery(name="User.HasLogin",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.login = :userLogin")	
})
public class User {
	private long id;
	private String login;
	private String password;
	private String roles; // split by ',' to separate roles
	private byte enabled;
	
	public boolean hasRole(String roleName) {
		String requested = roleName.toLowerCase();
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equals(requested));
	}
	
	// application-specific fields
	private List<Vote> votes = new ArrayList<>(); 
	private List<Question> questions = new ArrayList<>();
	private List<CGroup> groups = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	

	@Column(unique=true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
	
	@OneToMany(targetEntity=Vote.class)
	@JoinColumn(name="author_id")
	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	@OneToMany(targetEntity=Question.class)
	@JoinColumn(name="participant_id")
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}	
	
	@ManyToMany(targetEntity=CGroup.class, mappedBy="participants")
	public List<CGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<CGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", roles=" + roles + ", enabled="
				+ enabled + "]";
	}
}
