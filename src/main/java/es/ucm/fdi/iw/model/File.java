package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class File {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String metadata;
	
	
	//Hay que añadir en la entidad User lo siguiente : 
	/*
	 	@OneToMany(targetEntity=FilePermission.class, mappedBy="user")
		private List<FilePermission> filePermissions;
	 */
	  
	@OneToMany(targetEntity=UserFile.class, mappedBy="file")
	private List<UserFile> filePermissions;
	
	@ManyToMany(targetEntity=Group.class, mappedBy="files")
	private List<Group> groups;
	
	@ManyToMany(targetEntity=Tag.class)
	private List<Tag> tags;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public List<UserFile> getFilePermissions() {
		return filePermissions;
	}

	public void setFilePermissions(List<UserFile> filePermissions) {
		this.filePermissions = filePermissions;
	}

	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return this.groups;
	}
	
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}
