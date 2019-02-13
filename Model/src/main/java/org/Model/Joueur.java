package org.Model;

import com.corundumstudio.socketio.SocketIOClient;

public class Joueur {
	private int id;
	private String name;
	private SocketIOClient socket;
	
	public Joueur(String name, int id, SocketIOClient socket) {
		this.name = name;
		this.id = id;
		this.socket =socket;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SocketIOClient getSocket() {
		return socket;
	}
	public void setSocket(SocketIOClient socket) {
		this.socket = socket;
	}


}
