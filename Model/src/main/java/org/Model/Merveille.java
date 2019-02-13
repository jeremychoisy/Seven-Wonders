package org.Model;

public class Merveille {
	private String nom;
	private String ressource;
	private int etapeCourante;
	
	public Merveille() {}
	
	public Merveille(String nom, String ressource) {
		this.nom=nom;
		this.ressource=ressource;
		etapeCourante=1;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom=nom;
	}
	public String getRessource() {
		return ressource;
	}
	public void setRessource(String ressource) {
		this.ressource=ressource;
	}
	public int getEtapeCourante() {
		return etapeCourante;
	}
	public void changeEtape() {
		etapeCourante++;
	}
}
