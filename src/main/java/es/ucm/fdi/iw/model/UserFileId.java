package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserFileId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int fileId;
	private int userId;
}
