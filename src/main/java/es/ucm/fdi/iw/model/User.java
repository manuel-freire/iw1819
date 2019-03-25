package es.ucm.fdi.iw.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
<<<<<<< Updated upstream
=======
@NamedQueries({
	@NamedQuery(name="User.byEmailOrNickname",
			query="SELECT u FROM User u "
					+ "WHERE (u.email = :userLogin OR u.nickname = :userLogin) AND u.active = 1"),
})
>>>>>>> Stashed changes
public class User{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String nickname;
	private String name;
	private String email;
	private String password;
	private Date birthdate;
	
<<<<<<< Updated upstream
=======
	private String description;
	
	private boolean active;
	
	private String roles;
	
>>>>>>> Stashed changes
	
	@ManyToOne(targetEntity=User.class)
	private List<Friend> friends;
	
	@OneToMany(targetEntity=Notification.class, mappedBy="user")
	private List<Notification> notifications;
	
	
	@OneToMany(targetEntity=UserFile.class, mappedBy="user")
	private List<UserFile> files;
	
	
	@ManyToMany(targetEntity=Group.class, mappedBy="users")
	private List<Group> groups;
	
	
	@ManyToMany(targetEntity=GroupMessage.class, mappedBy="user")
	private List<GroupMessage> groupsMessages;
	
	
	@OneToMany(targetEntity=UserMessage.class, mappedBy="userReceiver")
	private List<UserMessage> usersMessages;
	
	
	
	
}






/**
 * A user.
 * 
 * Note that, in this particular application, we will automatically be creating
 * users for students. Those users will have the group password as their "password", 
 * but will be generally unable to actually log in without the group password.  
 * 
 * @author mfreire
 */
/*@Entity
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
	
	// application-specific fields
	private List<Vote> votes; 
	private List<Question> questions;
	private List<Group> groups;
	
	@Id
	@GeneratedValue
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
	
	@ManyToMany(targetEntity=Group.class, mappedBy="participants")
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
<<<<<<< Updated upstream
}*/
=======

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}
	
	public String getRoles() {
		return this.roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public boolean hasRole(String roleName) {
		return Arrays.stream(this.roles.split(","))
				.anyMatch(r -> r.equalsIgnoreCase(roleName));
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", nickname=" + nickname + ", name=" + name + ", lastName=" + lastName + 
					", password=" + password + ", birthdate=" + birthdate + ", description=" + description + ", roles=" + roles + ", active=" + active + "]";
	}
	
}








>>>>>>> Stashed changes
