package org.Model.assets;

import java.util.HashMap;

public class Effet {
	private String nom;
	private String orientation;
	private int valeur;
	private String ressource;
	
	public Effet(String nom,String ressource, String orientation,int valeur) {
		this.nom = nom;
		this.ressource = ressource;
		this.orientation = orientation;
		this.valeur = valeur;		
	}
	
	

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
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
}
