package es.ucm.fdi.iw.model;

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
public class User{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String nickname;
	private String name;
	private String email;
	private String password;
	private Date birthday;
	private String description;
	
	
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


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Friend> getFriends() {
		return friends;
	}


	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}


	public List<Notification> getNotifications() {
		return notifications;
	}


	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}


	public List<UserFile> getFiles() {
		return files;
	}


	public void setFiles(List<UserFile> files) {
		this.files = files;
	}


	public List<Group> getGroups() {
		return groups;
	}


	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}


	public List<GroupMessage> getGroupsMessages() {
		return groupsMessages;
	}


	public void setGroupsMessages(List<GroupMessage> groupsMessages) {
		this.groupsMessages = groupsMessages;
	}


	public List<UserMessage> getUsersMessages() {
		return usersMessages;
	}


	public void setUsersMessages(List<UserMessage> usersMessages) {
		this.usersMessages = usersMessages;
	}
	
	
	
	
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
}*/
