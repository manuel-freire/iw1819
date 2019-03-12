package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class GroupMessageId {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int messageId;
	
	private int transmitterUserId;
	private int groupReceiverId;
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getTransmitterUserId() {
		return transmitterUserId;
	}
	public void setTransmitterUserId(int transmitterUserId) {
		this.transmitterUserId = transmitterUserId;
	}
	public int getGroupReceiverId() {
		return groupReceiverId;
	}
	public void setGroupReceiverId(int groupReceiverId) {
		this.groupReceiverId = groupReceiverId;
	}
}
