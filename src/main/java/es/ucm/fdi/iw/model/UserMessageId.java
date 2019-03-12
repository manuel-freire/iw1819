package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class UserMessageId {
	
	private int transmitterUserId;
	private int receiverUserId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int messageId;

	public int getTransmitterUserId() {
		return transmitterUserId;
	}

	public void setTransmitterUserId(int transmitterUserId) {
		this.transmitterUserId = transmitterUserId;
	}

	public int getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(int receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	
}
