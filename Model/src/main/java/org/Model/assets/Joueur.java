package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;

public class Joueur {
	private SocketIOClient socket;
	private String nom;
	private int score;
	private Main m;
	private boolean isRdy;
	
	
	public Joueur(String nom, SocketIOClient socket) {
		this.nom = nom;
		this.socket = socket;
		this.score = 0;
		this.isRdy = false;
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
}
