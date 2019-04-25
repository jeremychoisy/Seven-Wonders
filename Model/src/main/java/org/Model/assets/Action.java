package org.Model.assets;

public class Action {
    private Carte carte;
    private String type;
    private int cout;


    public Action(Carte carte, String type, int cout){
        this.carte = carte;
        this.type = type;
        this.cout = cout;

    }


    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte c) {
        this.carte = c;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCout() { return cout; }

    public void setCout(int cout) { this.cout = cout; }
}
