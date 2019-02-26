package org.Model.assets;

import java.util.HashMap;

public class Merveille {
	private String nom;
	private String ressource;
	private int etapeCourante;
	
	//Ressources nécessaires à la réalisation de l'étape de Merveille
	private HashMap<String,Integer> ressourceEtape1;
	private HashMap<String,Integer> ressourceEtape2;
	private HashMap<String,Integer> ressourceEtape3;
	
	//Effets associés à chaque étape de la marveille (face A uniquement)
	private Effet effet1;
	private Effet effet2;
	private Effet effet3;
	

	public Merveille() {
	}

	public Merveille(String nom, String ressource,HashMap<String,Integer> ressourceEtape1,HashMap<String,Integer> ressourceEtape2,HashMap<String,Integer> ressourceEtape3) {
		this.nom = nom;
		this.ressource = ressource;
		etapeCourante = 1;
		this.setRessourceEtape1(ressourceEtape1);
		this.setRessourceEtape2(ressourceEtape2);
		this.setRessourceEtape3(ressourceEtape3);
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

	public void etapeSuivante(Joueur J) { 
		if(etapeCourante<3) {
			etapeCourante++;
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

	public Effet getEffet1() {
		return effet1;
	}

	public void setEffet1(Effet effet1) {
		this.effet1 = effet1;
	}

	public Effet getEffet2() {
		return effet2;
	}

	public void setEffet2(Effet effet2) {
		this.effet2 = effet2;
	}

	public Effet getEffet3() {
		return effet3;
	}

	public void setEffet3(Effet effet3) {
		this.effet3 = effet3;
	}
}
