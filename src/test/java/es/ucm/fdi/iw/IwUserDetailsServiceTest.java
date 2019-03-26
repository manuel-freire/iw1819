package es.ucm.fdi.iw;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import es.ucm.fdi.iw.IwApplication;
import es.ucm.fdi.iw.IwUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IwApplication.class)
public class IwUserDetailsServiceTest {

	@Autowired private EntityManager entityManager;	
	@Autowired private IwUserDetailsService iwUserDetailsService;

	@Test(expected=UsernameNotFoundException.class)
	public void testInvalidUser() {
	    assertTrue(entityManager != null);
		iwUserDetailsService.loadUserByUsername("a user that does not exist"); 
	}

	@Test()
	public void testValidUser() {
		iwUserDetailsService.loadUserByUsername("a"); // exists by default 
	}
}
