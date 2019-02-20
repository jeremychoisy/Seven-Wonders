package org.Model.assets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Carte {
	private String nom;
	private String type;
	private HashMap<String, Integer> cout;
	//private int configurationNumber;
	private int age;
	private int pointsVictoire;

	public Carte() {
	}
	
	public Carte(String nom, String type, int pointsVictoire) {
		this.nom = nom;
		this.type = type;
		this.pointsVictoire = pointsVictoire;
	}
	

	public Carte(String nom, String type, int pointsVictoire,HashMap<String, Integer> cout) {
		this.nom = nom;
		this.type = type;
		this.pointsVictoire = pointsVictoire;
		this.cout = cout;

	}
	
	/*public Carte(String nom, String type, HashMap<String, Integer> cout, int age, boolean pose,
			int configurationNumber) {
		this.nom = nom;
		this.type = type;
		this.cout = cout;
		this.age = age;
		this.pose = pose;
		this.configurationNumber = configurationNumber;
	}*/


	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public HashMap<String, Integer> getCout(String ressource) {
		//return cout.get(ressource);
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		Set set = cout.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			ret.put((String) mentry.getKey(), (Integer) mentry.getValue());
		}

		return ret;
	}

	public void addCout(String ressource, int valeur) {
		cout.put(ressource, valeur);
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPointsVictoire() {
		return pointsVictoire;
	}

	public void setPointsVictoire(int pointsVictoire) {
		this.pointsVictoire = pointsVictoire;
	}
}
