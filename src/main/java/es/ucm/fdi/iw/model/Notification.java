package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Notification {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(targetEntity=User.class)
	private User user;
	private String text;
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
