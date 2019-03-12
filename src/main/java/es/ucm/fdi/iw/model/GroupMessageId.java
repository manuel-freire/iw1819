package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;

@Embeddable
public class GroupMessageId {
	private int messageId;
	private int transmitterUserId;
	private int groupReceiverId;
}
