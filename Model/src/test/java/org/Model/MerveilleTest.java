
package org.Model;

import org.Model.assets.Joueur;
import org.Model.assets.Merveille;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
public class MerveilleTest{


	Merveille merveille = new Merveille();
	HashMap<String,Integer> e1 = new HashMap<String,Integer>();
	HashMap<String,Integer> e2 = new HashMap<String,Integer>();
	HashMap<String,Integer> e3 = new HashMap<String,Integer>();
	Merveille test = new Merveille("Gizah", "pierre",e1,e2,e3);
	Joueur j = new Joueur();
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
		test.etapeSuivante(j);
		assertEquals(2,test.getEtapeCourante());
		test.etapeSuivante(j);
		assertEquals(3,test.getEtapeCourante());
		test.etapeSuivante(j);
		assertEquals(3,test.getEtapeCourante());
	}
	
}