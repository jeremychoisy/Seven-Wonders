package org.Model.assets;

import java.util.HashMap;

import org.Model.tools.GestionEffets;

public class Merveille {
	private String nom;
	private String ressource;
	private int etapeCourante;
	
	//Ressources nécessaires à la réalisation de l'étape de Merveille
	private HashMap<String,Integer> ressourceEtape1;
	private HashMap<String,Integer> ressourceEtape2;
	private HashMap<String,Integer> ressourceEtape3;
	
	//Effets associés à chaque étape de la marveille (face A uniquement)
	private HashMap<String,String> effetEtapeUne;
	private HashMap<String,String> effetEtapeDeux;
	private HashMap<String,String> effetEtapeTrois;
	

	public Merveille() {
	}

	public Merveille(String nom, String ressource,HashMap<String,Integer> ressourceEtape1,HashMap<String,Integer> ressourceEtape2,HashMap<String,Integer> ressourceEtape3, HashMap<String,String> effetEtapeUne, HashMap<String,String> effetEtapeDeux, HashMap<String,String> effetEtapeTrois) {
		this.nom = nom;
		this.ressource = ressource;
		this.etapeCourante = 0;
		
		this.setRessourceEtape1(ressourceEtape1);
		this.setRessourceEtape2(ressourceEtape2);
		this.setRessourceEtape3(ressourceEtape3);
		
		this.effetEtapeUne = effetEtapeUne;
		this.effetEtapeDeux = effetEtapeDeux;
		this.effetEtapeTrois = effetEtapeTrois;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getRessource() {
		return ressource;
	}

	public void setRessource(String ressource) {
		this.ressource = ressource;
	}

	public int getEtapeCourante() {
		return etapeCourante;
	}

	public void etapeSuivante(Joueur j) { 
		if(this.etapeCourante < 3) {
			switch(this.etapeCourante) {
				case 0:
					GestionEffets.appliquerEffetMerveille(this.effetEtapeUne, j);
					break;
				case 1:
					GestionEffets.appliquerEffetMerveille(this.effetEtapeDeux, j);
					break;
				case 2:
					GestionEffets.appliquerEffetMerveille(this.effetEtapeTrois, j);
					break;
			}
			this.etapeCourante++;
		}
		
	}

	public HashMap<String,Integer> getRessourceEtape1() {
		return ressourceEtape1;
	}

	public void setRessourceEtape1(HashMap<String,Integer> ressourceEtape1) {
		this.ressourceEtape1 = ressourceEtape1;
	}

	public HashMap<String,Integer> getRessourceEtape2() {
		return ressourceEtape2;
	}

	public void setRessourceEtape2(HashMap<String,Integer> ressourceEtape2) {
		this.ressourceEtape2 = ressourceEtape2;
	}

	public HashMap<String,Integer> getRessourceEtape3() {
		return ressourceEtape3;
	}

	public void setRessourceEtape3(HashMap<String,Integer> ressourceEtape3) {
		this.ressourceEtape3 = ressourceEtape3;
	}

	public HashMap<String,String> getEffetEtapeTrois() {
		return effetEtapeTrois;
	}

	public void setEffetEtapeTrois(HashMap<String,String> effetEtapeTrois) {
		this.effetEtapeTrois = effetEtapeTrois;
	}

	public HashMap<String,String> getEffetEtapeDeux() {
		return effetEtapeDeux;
	}

	public void setEffetEtapeDeux(HashMap<String,String> effetEtapeDeux) {
		this.effetEtapeDeux = effetEtapeDeux;
	}

	public HashMap<String,String> getEffetEtapeUne() {
		return effetEtapeUne;
	}

	public void setEffetEtapeUne(HashMap<String,String> effetEtapeUne) {
		this.effetEtapeUne = effetEtapeUne;
	}
}
