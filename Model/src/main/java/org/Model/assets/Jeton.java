package org.Model.assets;

public class Jeton {
	private int valeur;
	
	public Jeton(){}
	
	public Jeton(int valeur) {
		this.valeur=valeur;
	}
	
	public int getValeur() {
		return valeur;
	}
	
	public void setValeur(int valeur) {
		if(valeur != -1 && valeur !=1 && valeur != 3 && valeur !=5) {
			System.out.println("Mauvaise valeur rentrée, Les valeurs disponibles pour les jetons sont -1, 1, 3, 5" );
		}
	}
}
