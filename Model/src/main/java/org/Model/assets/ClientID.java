package org.Model.assets;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;

public class ClientID {
    private int id;
    private String name;
    private SocketIOClient socket;
    private int victoires;
    private ArrayList<Integer> scores;


    public ClientID(int id, String name, SocketIOClient socket){
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.victoires = 0;
        this.scores = new ArrayList<>();
    }
    public void addVictoire(int score){
        this.victoires += 1;
        this.scores.add(score);
    }
    public int getVictoires(){
        return victoires;
    }
    public double getScoresAverage(){
        double cmp = 0;
        for(int i =0;i<scores.size();i++){
            cmp += scores.get(i);
        }
        cmp /= scores.size();
        return cmp;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
