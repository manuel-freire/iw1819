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
}
