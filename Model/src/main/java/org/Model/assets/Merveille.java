package org.Model.assets;

import java.util.HashMap;

public class Merveille {
	private String nom;
	private String ressource;
	private int etapeCourante;
	private HashMap<String,Integer> ressourceEtape1;
	private HashMap<String,Integer> ressourceEtape2;
	private HashMap<String,Integer> ressourceEtape3;
	

	public Merveille() {
	}

	public Merveille(String nom, String ressource,HashMap<String,Integer> ressourceEtape1,HashMap<String,Integer> ressourceEtape2,HashMap<String,Integer> ressourceEtape3) {
		this.nom = nom;
		this.ressource = ressource;
		etapeCourante = 1;
		this.ressourceEtape1=ressourceEtape1;
		this.ressourceEtape2=ressourceEtape2;
		this.ressourceEtape3=ressourceEtape3;
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

	public void changeEtape() {
		if(etapeCourante<3) {
			etapeCourante++;
		}
		
	}
}
