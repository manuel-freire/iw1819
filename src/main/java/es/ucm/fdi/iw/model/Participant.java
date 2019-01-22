package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A student of a class.
 * 
 * Can ask an vote questions. Can also be banned.
 *  
 * @author mfreire
 */
@Entity
public class Participant {
	private long id;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
