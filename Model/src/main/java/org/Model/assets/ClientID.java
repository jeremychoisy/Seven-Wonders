package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;

public class ClientID {
    private int id;
    private SocketIOClient socket;

    public ClientID(int id, SocketIOClient socket){
        this.id = id;
        this.socket = socket;
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
