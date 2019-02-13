package org.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Carte {
	private String nom;
	private String type;
	private HashMap<String, Integer> cout;
	private int configurationNumber;
	private int age;
	private boolean pose;

	public Carte() {
	}
	
	public Carte(String nom, String type, HashMap<String, Integer> cout) {
		this.nom = nom;
		this.type = type;
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

	public boolean isPose() {
		return pose;
	}

	public void pose() {
		pose = true;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getCout(String ressource) {
		return cout.get(ressource);
		/*HashMap<String, Integer> ret = new HashMap<String, Integer>();
		Set set = cout.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry) iterator.next();
			ret.put((String) mentry.getKey(), (Integer) mentry.getValue());
		}

		return ret;*/
	}

	public void addCout(String ressource, int valeur) {
		this.cout.put(ressource, valeur);
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String gettype() {
		return type;
	}

	public void settype(String type) {
		this.type = type;
	}
}
