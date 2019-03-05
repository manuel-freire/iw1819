package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;

@Embeddable
public class UserFileId {
	private int fileId;
	private int userId;
}
