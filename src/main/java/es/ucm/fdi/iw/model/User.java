package es.ucm.fdi.iw.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
<<<<<<< HEAD
<<<<<<< HEAD
@NamedQueries({
	@NamedQuery(name="User.all",
		query="SELECT u FROM User u"),
})
=======
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
<<<<<<< Updated upstream
=======
@NamedQueries({
	@NamedQuery(name="User.byEmailOrNickname",
			query="SELECT u FROM User u "
					+ "WHERE (u.email = :userLogin OR u.nickname = :userLogin) AND u.active = 1"),
})
>>>>>>> Stashed changes
<<<<<<< HEAD
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
public class User{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(unique=true)
	private String nickname;
	
	private String name;
	
	private String lastName;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	
	private Date birthdate;
	
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
<<<<<<< Updated upstream
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
	private String description;
	
	private boolean active;
	
<<<<<<< HEAD
<<<<<<< HEAD
=======
	private String roles;
	
>>>>>>> Stashed changes
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
	private String roles;
	
>>>>>>> Stashed changes
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
	
	@ManyToOne(targetEntity=User.class)
	private List<Friend> friends;
	
	@OneToMany(targetEntity=Notification.class, mappedBy="user")
	private List<Notification> notifications;
	
	
	@OneToMany(targetEntity=UserFile.class, mappedBy="user")
	private List<UserFile> files;
	
	
	@ManyToMany(targetEntity=Group.class, mappedBy="users")
	private List<Group> groups;
	
	
	@OneToMany(targetEntity=Message.class, mappedBy="sender")
	private List<Message> sentMessages;
	
	@OneToMany(targetEntity=Message.class, mappedBy="receiver")
	private List<Message> receivedMessages;

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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
}*/
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
<<<<<<< Updated upstream
}*/
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"

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
	
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
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
	
<<<<<<< HEAD
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
}








<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
=======
>>>>>>> Stashed changes
>>>>>>> parent of f0d4d74... Revert "Añadidos cambios para dividir los Controller y que funcione con Spring security"
