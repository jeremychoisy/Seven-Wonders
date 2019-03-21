package org.Model.assets;

import java.util.Map;

import org.Model.tools.GestionEffets;

public class Merveille {
	private String nom;
	private String ressource;
	private int etapeCourante;
	
	//Ressources nécessaires à la réalisation de l'étape de Merveille
	private Map<String,Integer> ressourceEtapeUne;
	private Map<String,Integer> ressourceEtapeDeux;
	private Map<String,Integer> ressourceEtapeTrois;
	
	//Effets associés à chaque étape de la merveille (face A uniquement)
	private Map<String,String> effetEtapeUne;
	private Map<String,String> effetEtapeDeux;
	private Map<String,String> effetEtapeTrois;


	public Merveille() {
	}

	public Merveille(String nom, String ressource,Map<String,Integer> ressourceEtapeUne,Map<String,Integer> ressourceEtapeDeux,Map<String,Integer> ressourceEtapeTrois, Map<String,String> effetEtapeUne, Map<String,String> effetEtapeDeux, Map<String,String> effetEtapeTrois) {
		this.nom = nom;
		this.ressource = ressource;
		this.etapeCourante = 0;
		
		this.ressourceEtapeUne = ressourceEtapeUne;
		this.ressourceEtapeDeux = ressourceEtapeDeux;
		this.ressourceEtapeTrois = ressourceEtapeTrois;
		

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

	public Map<String, Integer> getressourceEtapeCourante() {
		switch(this.etapeCourante){
			case 0:
				return ressourceEtapeUne;
			case 1:
				return ressourceEtapeDeux;
			case 2:
				return ressourceEtapeTrois;
			default:
				return null;
		}
	}


	public void etapeSuivante(Joueur j) {
		if(this.etapeCourante < 3) {
			this.etapeCourante++;
			switch(this.etapeCourante) {
				case 1:
					GestionEffets.appliquerEffet(this.effetEtapeUne, j);
					break;
				case 2:
					GestionEffets.appliquerEffet(this.effetEtapeDeux, j);
					break;
				case 3:
					GestionEffets.appliquerEffet(this.effetEtapeTrois, j);
					break;
			}
		}
		
	}


	public Map<String, Integer> getressourceEtapeUne() {
		return ressourceEtapeUne;
	}

	public void setressourceEtapeUne(Map<String, Integer> ressourceEtapeUne) {
		this.ressourceEtapeUne = ressourceEtapeUne;
	}

	public Map<String, Integer> getressourceEtapeDeux() {
		return ressourceEtapeDeux;
	}

	public void setressourceEtapeDeux(Map<String, Integer> ressourceEtapeDeux) {
		this.ressourceEtapeDeux = ressourceEtapeDeux;
	}

	public Map<String, Integer> getressourceEtapeTrois() {
		return ressourceEtapeTrois;
	}

	public void setressourceEtapeTrois(Map<String, Integer> ressourceEtapeTrois) {
		this.ressourceEtapeTrois = ressourceEtapeTrois;
	}

	public Map<String, String> getEffetEtapeUne() {
		return effetEtapeUne;
	}

	public void setEffetEtapeUne(Map<String, String> effetEtapeUne) {
		this.effetEtapeUne = effetEtapeUne;
	}

	public Map<String, String> getEffetEtapeDeux() {
		return effetEtapeDeux;
	}

	public void setEffetEtapeDeux(Map<String, String> effetEtapeDeux) {
		this.effetEtapeDeux = effetEtapeDeux;
	}

	public Map<String, String> getEffetEtapeTrois() {
		return effetEtapeTrois;
	}

	public void setEffetEtapeTrois(Map<String, String> effetEtapeTrois) {
		this.effetEtapeTrois = effetEtapeTrois;
	}

	public void setEtapeCourante(int etapeCourante) {
		this.etapeCourante = etapeCourante;
	}
}
