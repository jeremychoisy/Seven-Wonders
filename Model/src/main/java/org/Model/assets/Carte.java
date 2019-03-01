package org.Model.assets;

import java.util.Map;

public class Carte {
	private String nom;
	private String type;
	private Map<String, Integer> cout;
	private int configurationNumber;
	private int age;

	// effet	
	Map<String,String> effet;
	

	public Carte() {}
	
	public Carte(String nom, String type, Map<String,String> effet ,Map<String,Integer> cout, int configurationNumber, int age) {
		this.nom = nom;
		this.type = type;
		this.effet=effet;
		this.cout = cout;
		this.configurationNumber= configurationNumber;
		this.age = age;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Map<String,Integer> getCout() {
		return cout;
	}


	public void setCout(Map<String,Integer> cout) {
		this.cout = cout;
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

	public int getConfigurationNumber() {
		return configurationNumber;
	}

	public void setConfigurationNumber(int configurationNumber) {
		this.configurationNumber = configurationNumber;
	}
	
	public Map<String,String> getEffet(){
		return this.effet;
 }
	
	public void setEffet(Map<String,String> effet){
		this.effet = effet;
	}
	
	public String toString() {
		return "Carte : " + this.nom + " " + this.effet.get("nomEffet");
	}
	
}
