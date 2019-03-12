package es.ucm.fdi.iw.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class GroupMessage {

	@EmbeddedId
	private GroupMessageId id;
	
	@ManyToOne
	@MapsId("transmitterUserId")
	private User user;
	
	@ManyToOne
	@MapsId("groupReceiverId")
	private Group group;
	
	@ManyToOne
	@MapsId("messageId")
	private Message message;

	public GroupMessageId getId() {
		return id;
	}

	public void setId(GroupMessageId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
