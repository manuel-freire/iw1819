package es.ucm.fdi.iw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.ucm.fdi.iw.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IwApplicationTests {
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void contextLoads() {
		User u = entityManager.createNamedQuery("User.byEmailOrNickname", User.class)
                .setParameter("userLogin", "Darko")
                .getSingleResult();
		
		assertNotNull(u);
		assertEquals(u.getNickname(), "Darko");
		
		u = entityManager.createNamedQuery("User.byEmailOrNickname", User.class)
                .setParameter("userLogin", "flopezcarr@ucm.es")
                .getSingleResult();
		
		assertNotNull(u);
		assertEquals(u.getNickname(), "BadmintonNoob");
	}
	
	
}

