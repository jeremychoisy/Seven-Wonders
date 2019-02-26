package org.Model.assets;


public class Carte {
	private String nom;
	private String type;
	//private HashMap<String, Integer> cout;
	private int configurationNumber;
	private int age;
	private String nomEffet;
	private Effet effet;

	public Carte() {}
	
	public Carte(String nom, String type, String nomEffet, int configurationNumber) {
		this.nom = nom;
		this.type = type;
		this.nomEffet = nomEffet;
		//this.cout = cout;
		this.configurationNumber= configurationNumber;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	/*public int getCout(String ressource) {
		return cout.get(ressource);
	}


	public void addCout(String ressource, int valeur) {
		cout.put(ressource, valeur);
	}*/

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

	public Effet getEffet() {
		return effet;
	}

	public void setEffet(Effet effet) {
		this.effet = effet;
	}

	public String getNomEffet() {
		return nomEffet;
	}

	public void setNomEffet(String nomEffet) {
		this.nomEffet = nomEffet;
	}
	
	public String toString() {
		return "" + this.nom + " " + this.nomEffet + " " + this.effet; 
	}
}
