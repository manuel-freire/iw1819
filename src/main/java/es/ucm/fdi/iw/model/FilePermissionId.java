package es.ucm.fdi.iw.model;

import javax.persistence.Embeddable;

@Embeddable
public class FilePermissionId {
	private int fileId;
	private int userId;
}
