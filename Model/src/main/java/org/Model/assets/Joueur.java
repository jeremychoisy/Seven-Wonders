package org.Model.assets;


import java.util.HashMap;

import com.corundumstudio.socketio.SocketIOClient;

public class Joueur {
	private String name;
	private SocketIOClient socket;
	private Main main;
	private HashMap<String,Integer> ressource;
	
	
	public Joueur(String name, SocketIOClient socket, Main main, HashMap<String,Integer> ressource ) {
		this.name = name;
		this.socket =socket;
		this.main = main;
		this.setRessource(ressource);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public SocketIOClient getSocket() {
		return socket;
	}
	public void setSocket(SocketIOClient socket) {
		this.socket = socket;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public HashMap<String,Integer> getRessource() {
		return ressource;
	}

	public void setRessource(HashMap<String,Integer> ressource) {
		this.ressource = ressource;
	}
	


}
