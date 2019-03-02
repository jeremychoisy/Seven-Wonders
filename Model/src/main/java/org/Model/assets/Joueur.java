package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;
import java.util.HashMap;
import java.util.Map;

public class Joueur {
	private SocketIOClient socket;
	private String nom;
	private Main m;
	private boolean isRdy;
	private Merveille merveille; 

	private boolean commerce_ressources_primaires;
	private boolean commerce_ressources_secondaires;

	private int points_victoire;
	private int boucliers;
	
	private Map<String,Integer> ressources;

	
	public Joueur() {}
	
	public Joueur(String nom) {
		this.nom = nom;
		initJoueur();
	}
	public Joueur(String nom, SocketIOClient socket) {
		this.nom = nom;
		this.socket = socket;
		initJoueur();

	}
	
	public void initJoueur() {
		this.m = new Main();
		this.isRdy = false;
		this.setCommerce_ressources_primaires(false);
		this.setCommerce_ressources_secondaires(false);
		this.points_victoire = 0;
		this.boucliers = 0;
		
		ressources = new HashMap<String,Integer>();
		ressources.put("pièces", 0);
		ressources.put("bois", 0);
		ressources.put("pierre", 0);
		ressources.put("minerai", 0);
		ressources.put("argile", 0);
		ressources.put("tissu", 0);
		ressources.put("verre", 0);
		ressources.put("papier", 0);
	}
	
	public void ajouterRessources(String nomRessource, int quantité) {
		ressources.put(nomRessource, ressources.get(nomRessource) + quantité);
	}

	public int getQuantitéRessource(String nomRessource) {
		return ressources.get(nomRessource);
	}

	// Setters & Getters
	public boolean isCommerce_ressources_primaires() {
		return commerce_ressources_primaires;
	}

	public void setCommerce_ressources_primaires(boolean commerce_ressources_primaires) {
		this.commerce_ressources_primaires = commerce_ressources_primaires;
	}

	public boolean isCommerce_ressources_secondaires() {
		return commerce_ressources_secondaires;
	}

	public void setCommerce_ressources_secondaires(boolean commerce_ressources_secondaires) {
		this.commerce_ressources_secondaires = commerce_ressources_secondaires;
	}

	public int getPièces() {
		return ressources.get("pièces");
	}

	public void setPièces(int gain_pièces) {
		ressources.put("pièces",gain_pièces);
	}

	public int getPoints_victoire() {
		return points_victoire;
	}

	public void setPoints_victoire(int gain_points_victoire) {
		this.points_victoire = gain_points_victoire;
	}

	public int getBouclier() {
		return boucliers;
	}

	public void setBouclier(int gain_bouclier) {
		this.boucliers = gain_bouclier;
	}

	public void setRessources(HashMap<String, Integer> gain_ressources) {
		this.ressources = gain_ressources;
	}

	public Map<String, Integer> GetRessources() {
		return ressources;
	}

	public SocketIOClient getSocket() {
		return socket;
	}
	public void setSocket(SocketIOClient socket) {
		this.socket = socket;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isRdy() {
		return isRdy;
	}

	public void setRdy(boolean isRdy) {
		this.isRdy = isRdy;
	}

	public Main getM() {
		return m;
	}

	public void setM(Main m) {
		this.m = m;
	}

	public Merveille getMerveille() {
		return merveille;
	}

	public void setMerveille(Merveille merveille) {
		this.merveille = merveille;
		this.ressources.put(merveille.getRessource(),1);
	}
}
