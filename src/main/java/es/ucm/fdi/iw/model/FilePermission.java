package es.ucm.fdi.iw.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class FilePermission {
	
	@EmbeddedId
	private FilePermissionId id;
	
	@ManyToOne
	@MapsId("userId")
	private User user;
	
	
	@ManyToOne
	@MapsId("fileId")
	private File file;
	
	private String permission;
	
}
