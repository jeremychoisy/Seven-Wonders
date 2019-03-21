package org.Model;

import org.Model.assets.Joueur;
import org.Model.assets.Merveille;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class MerveilleTest{

	Joueur j;

	Merveille merveille;

	@BeforeEach
	public void setUp(){

		Map<String,Integer> ressourceEtapeUne = new HashMap<String,Integer>();
		Map<String,Integer> ressourceEtapeDeux = new HashMap<String,Integer>();
		Map<String,Integer> ressourceEtapeTrois = new HashMap<String,Integer>();

		Map<String,String> effetEtapeUne = new HashMap<String,String>();
		Map<String,String> effetEtapeDeux = new HashMap<String,String>();
		Map<String,String> effetEtapeTrois = new HashMap<String,String>();

		ressourceEtapeUne.put("pierre",2);
		ressourceEtapeDeux.put("bois",3);
		ressourceEtapeTrois.put("pierre",4);

		effetEtapeUne.put("nomEffet", "gain_pointsVictoire");
		effetEtapeUne.put("valeurEffet", "3");
		effetEtapeDeux.put("nomEffet", "gain_pointsVictoire");
		effetEtapeDeux.put("valeurEffet", "5");
		effetEtapeTrois.put("nomEffet", "gain_pointsVictoire");
		effetEtapeTrois.put("valeurEffet", "7");

		merveille = new Merveille("Gizah", "pierre",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois,effetEtapeUne,effetEtapeDeux,effetEtapeTrois);
		j = new Joueur();
	}

	@Test
	public void ChangeEtapeTest() {
		assertEquals(0,merveille.getEtapeCourante());
		merveille.etapeSuivante(j);
		assertEquals(3, j.getPointsVictoire(), "Le joueur devrait maintenant avoir gagné 3 points de victoire.");
		assertEquals(1,merveille.getEtapeCourante(), "L'étape courante de la merveille devrait maintenant être 1.");
		merveille.etapeSuivante(j);
		assertEquals(8, j.getPointsVictoire(), "Le joueur devrait maintenant avoir gagné 8 points de victoire.");
		assertEquals(2,merveille.getEtapeCourante(), "L'étape courante de la merveille devrait maintenant être 2.");
		merveille.etapeSuivante(j);
		assertEquals(15, j.getPointsVictoire(), "Le joueur devrait maintenant avoir gagné 15 points de victoire.");
		assertEquals(3,merveille.getEtapeCourante(), "L'étape courante de la merveille devrait maintenant être 3.");
	}
	
}
