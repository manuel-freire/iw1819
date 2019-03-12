package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;

@Embeddable
public class UserMessageId {
	private int transmitterUserId;
	private int receiverUserId;
	private int messageId;
	
	
}
