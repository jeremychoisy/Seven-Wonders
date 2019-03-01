package org.Model.assets;

import java.util.ArrayList;

public class Main {
	private ArrayList<Carte> main;
	
	public Main() {
		main = new ArrayList<Carte>();
	}
	
	public void add(Carte c) {
		if(main.size() < 7) {
			main.add(c);
		}
	}
	
	public int size() {
		return main.size();
	}
	public void remove(int i) {
		main.remove(i);
	}
	
	public ArrayList<Carte> getMain() {
		return main;
	}
	
	public void RemoveCardFromName(String nomCarte) {
		for(int i = 0;i < main.size();i++) {
			if(main.get(i).getNom().equals(nomCarte)) {
				main.remove(i);
			}
		}
	}
	
	public String toString() {
		String buffer ="Contenu de la main :";
		
		for(int i =0;i<main.size();i++) {
			buffer = buffer + " " + main.get(i).getNom() + ";";
		}
		
		return buffer;
	}

}
