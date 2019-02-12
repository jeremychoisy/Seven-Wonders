package org.Model;

public class Carte {
	private String nom;
	private String type;
	private int cout;
	private int configurationNumber;
	private int age;
	private boolean pose;

	public Carte() {}
	
	public Carte(String nom, String type, int cout, int age, boolean pose){
		this.nom = nom;
		this.type = type;
		this.cout=cout;
		this.age=age;
		this.pose=pose;
	}
	
	public boolean isPose() {
		return pose;
	}
	
	public void pose() {
		pose=true;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getCout() {
		return cout;
	}
	public void setCout(int cout) {
		this.cout = cout;
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
