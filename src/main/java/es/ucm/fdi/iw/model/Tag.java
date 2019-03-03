package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Tag {
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String color;
	
	@ManyToOne(targetEntity=Tag.class)
	private Tag parent;
	
	@OneToMany(targetEntity=Tag.class)
	@JoinColumn(name="parent_id")
	private List<Tag> children;
	
	@ManyToMany(targetEntity=File.class)
	private List<File> files;
	
	public Tag getParent() {
		return this.parent;
	}
	
	public void setParent(Tag parent) {
		this.parent = parent;
	}
	
	public List<Tag> getChildren() {
		return this.children;
	}
	
	public void setChildren(List<Tag> children) {
		this.children = children;
	}
	
	public List<File> getFiles() {
		return this.files;
	}
	
	public void setFiles(List<File> files) {
		this.files = files;
	}

}
