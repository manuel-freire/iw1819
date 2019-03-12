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
	

	public UserFileId getId() {
		return id;
	}

	public void setId(UserFileId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
