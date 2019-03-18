package org.Model.assets;

public class Jeton {
	private String nom;
	private int valeur;
	
	public Jeton(){}
	
	
	public Jeton(String nom, int age) {
		this.nom=nom;
		switch(age) {
			case 1:
				if(this.nom == "Défaite") {
					valeur= -1;
				}
				else {
					valeur = 1;
				}
			case 2:	
				if(this.nom == "Défaite") {
					valeur= -1;
				}
				else {
					valeur = 3;
				}
			case 3:	
				if(this.nom == "Défaite") {
					valeur= -1;
				}
				else {
					valeur = 5;
				}	
		}	
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		if(nom == "Défaite" || nom =="Victoire" ) {
			this.nom=nom;
		}
		else {
			System.out.println("Mauvais nom ! Les valeurs possibles sont Défaite ou Victoire");
		}
	}
	public int getValeur() {
		return valeur;
	}
	
	
	public void setValeur(int valeur) {
		if(valeur != -1 && valeur !=1 && valeur != 3 && valeur !=5) {
			System.out.println("Mauvaise valeur rentrée, Les valeurs disponibles pour les jetons sont -1, 1, 3, 5" );
		}
		else {
			this.valeur=valeur;
		}
	}
}
