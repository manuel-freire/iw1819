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
	private Date birthdate;
	
	
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
	
}








