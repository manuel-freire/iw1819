package es.ucm.fdi.iw.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class UserFile {
	
	@EmbeddedId
	private UserFileId id;
	
	@ManyToOne
	@MapsId("userId")
	private User user;
	
	
	@ManyToOne
	@MapsId("fileId")
	private File file;
	
	private String permission;
	
}
