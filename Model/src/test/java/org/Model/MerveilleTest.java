
package org.Model;

import org.Model.assets.Joueur;
import org.Model.assets.Merveille;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

public class MerveilleTest{


	Merveille merveille = new Merveille();
	HashMap<String,Integer> e1 = new HashMap<String,Integer>();
	HashMap<String,Integer> e2 = new HashMap<String,Integer>();
	HashMap<String,Integer> e3 = new HashMap<String,Integer>();
	
	HashMap<String,String> effetEtapeUne = new HashMap<String,String>();
	HashMap<String,String> effetEtapeDeux = new HashMap<String,String>();
	HashMap<String,String> effetEtapeTrois = new HashMap<String,String>();
	
	
	Merveille test = new Merveille("Gizah", "pierre",e1,e2,e3,effetEtapeUne,effetEtapeDeux,effetEtapeTrois);
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
		assertEquals(0,test.getEtapeCourante());
	}

	@Test
	public void testChangeEtape() {
		
		effetEtapeUne.put("nomEffet", "gain_points_victoire");
		effetEtapeUne.put("valeurEffet", "3");
		effetEtapeDeux.put("nomEffet", "gain_points_victoire");
		effetEtapeDeux.put("valeurEffet", "5");
		effetEtapeTrois.put("nomEffet", "gain_points_victoire");
		effetEtapeTrois.put("valeurEffet", "7");
		
		assertEquals(0,test.getEtapeCourante());
		test.etapeSuivante(j);
		assertEquals(1,test.getEtapeCourante());
		test.etapeSuivante(j);
		assertEquals(2,test.getEtapeCourante());
	}
	
}