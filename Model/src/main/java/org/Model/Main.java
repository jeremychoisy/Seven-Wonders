package org.Model;

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
}
