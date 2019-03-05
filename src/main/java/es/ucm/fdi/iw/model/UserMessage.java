package es.ucm.fdi.iw.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import es.ucm.fdi.iw.model.User;

@Entity
public class UserMessage {

	@EmbeddedId
	private UserMessage id;
	
	@ManyToOne
	@MapsId("transmitterUserId")
	private User userTransmitter;
	
	@ManyToOne
	@MapsId("receiverUserId")
	private User userReceiver;
	
	@ManyToOne
	@MapsId("messageId")
	private Message message;
}
