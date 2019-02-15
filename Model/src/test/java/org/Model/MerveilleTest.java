
package org.Model;

import org.Model.assets.Merveille;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.*;
import org.junit.jupiter.api.Test;
public class MerveilleTest{


	Merveille merveille = new Merveille();
	Merveille test = new Merveille("Gizah", "pierre");
	
	@Test
	public void testGetNom() {
		assertEquals("Gizah",test.getNom());
	}
	
	@Test
	public void testSetNom() {
		merveille.setNom("Rhodos");
		assertEquals("Rhodos",merveille.getNom());
	}

	@Test
	public void testGetRessource() {
		assertEquals("pierre",test.getRessource());
	}

	@Test
	public void testSetRessource() {
		merveille.setRessource("minerai");
		assertEquals("minerai",merveille.getRessource());
	}

	@Test
	public void testGetEtapeCourante() {
		assertEquals(1,test.getEtapeCourante());
	}

	@Test
	public void testChangeEtape() {
		assertEquals(1,test.getEtapeCourante());
		test.changeEtape();
		assertEquals(2,test.getEtapeCourante());
		test.changeEtape();
		assertEquals(3,test.getEtapeCourante());
		test.changeEtape();
		assertEquals(3,test.getEtapeCourante());
	}
	
}