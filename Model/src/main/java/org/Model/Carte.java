package org.Model;

public class Carte {
	private String nom;
	private String couleur;

	public Carte() {}
	
	public Carte(String nom, String couleur){
		this.nom = nom;
		this.couleur = couleur;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCouleur() {	
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
}
