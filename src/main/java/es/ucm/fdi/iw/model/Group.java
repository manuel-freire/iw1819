package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@Entity
public class Group {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	
	
	@ManyToMany(targetEntity=User.class)
	private List<User> users;

	@ManyToMany(targetEntity=File.class)
	private List<File> files;

	@OneToMany(targetEntity=GroupMessage.class, mappedBy="group")
	private List<GroupMessage> messages;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<GroupMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<GroupMessage> messages) {
		this.messages = messages;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
