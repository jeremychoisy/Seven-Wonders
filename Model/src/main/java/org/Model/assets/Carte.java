package org.Model.assets;


public class Carte {
	private String nom;
	private String type;
	//private HashMap<String, Integer> cout;
	private int configurationNumber;
	private int age;
	
	// effet
	private String nomEffet;
	private int ValeurEffet;
	private String orientationEffet;
	private String ressourceEffet;
	

	public Carte() {}
	
	public Carte(String nom, String type, String nomEffet,int ValeurEffet, String orientationEffet, String ressourceEffet, int configurationNumber) {
		this.nom = nom;
		this.type = type;
		this.nomEffet = nomEffet;
		//this.cout = cout;
		this.configurationNumber= configurationNumber;
		this.ValeurEffet = ValeurEffet;
		this.orientationEffet = orientationEffet;
		this.ressourceEffet = ressourceEffet;
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

	public String getNomEffet() {
		return nomEffet;
	}

	public void setNomEffet(String nomEffet) {
		this.nomEffet = nomEffet;
	}

	public String getOrientationEffet() {
		return orientationEffet;
	}

	public void setOrientationEffet(String orientationEffet) {
		this.orientationEffet = orientationEffet;
	}

	public int getValeurEffet() {
		return ValeurEffet;
	}

	public void setValeurEffet(int valeurEffet) {
		ValeurEffet = valeurEffet;
	}

	public String getRessourceEffet() {
		return ressourceEffet;
	}

	public void setRessourceEffet(String ressourceEffet) {
		this.ressourceEffet = ressourceEffet;
	}
	
	
}
