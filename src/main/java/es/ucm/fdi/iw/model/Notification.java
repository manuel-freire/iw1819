package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Notification {
	
	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(targetEntity=User.class, mappedBy="Notification")
	private long id_user;
	private String text;
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
