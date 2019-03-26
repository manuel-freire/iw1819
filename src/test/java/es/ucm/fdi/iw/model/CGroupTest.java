package es.ucm.fdi.iw.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CGroupTest {

	/**
	 * Note: this is only intended as an example 
	 */
	@Test
	public void test() {
		String r1 = CGroup.createRandomId();
		assertTrue(r1.matches("[0-9]+"));
		
		String r2 = CGroup.createRandomId();
		assertFalse(r1.equals(r2));
	}
}
