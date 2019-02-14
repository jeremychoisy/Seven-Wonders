package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;

public class Joueur {
	private String name;
	private SocketIOClient socket;
	
	public Joueur(String name, SocketIOClient socket) {
		this.name = name;
		this.socket =socket;
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


}
