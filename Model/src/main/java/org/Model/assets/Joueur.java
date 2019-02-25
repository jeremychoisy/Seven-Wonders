package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;

public class Joueur {
	private SocketIOClient socket;
	private String nom;
	private int score;
	private Main m;
	private boolean isRdy;
	private Merveille merveille;
	
	
	public Joueur(String nom, SocketIOClient socket, Merveille merveille) {
		this.nom = nom;
		this.socket = socket;
		this.score = 0;
		this.isRdy = false;
		this.setMerveille(merveille);
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
	}
}
