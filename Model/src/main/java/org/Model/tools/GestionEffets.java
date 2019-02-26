package org.Model.tools;

import org.Model.assets.Carte;
import org.Model.assets.Effet;
import org.Model.assets.Joueur;

public abstract class GestionEffets
{
	public static void  AppliquerEffet(Effet effet, Joueur j) {

		if(effet.getNom().equals("commerce_ressources_primaires")) {
			j.setCommerce_ressources_primaires(true);
		}
		if(effet.getNom().equals("commerce_ressources_secondaires")) {
			j.setCommerce_ressources_secondaires(true);
		}
		if(effet.getNom().equals("gain_pièces")) {
			j.setPièces(j.getPièces() + effet.getValeur());
		}
		if(effet.getNom().equals("gain_points_victoire")) {
			j.setPoints_victoire(j.getPoints_victoire() + effet.getValeur());
		}
		if(effet.getNom().equals("gain_boucliers")) {
			j.setBouclier(j.getBouclier() + effet.getValeur());
		}
		if(effet.getNom().equals("gain_ressources")) {
			j.ajouterRessources(effet.getRessource(), effet.getValeur());
		}	

	}
	
	public static Effet FabriquerEffet(Carte c,String name) {
		if(name.equals("commerce_ressources_primaires")){
			if(c.getNom().equals("Comptoir Est")) {
				return new Effet(name,null,"droite",0);
			}else {
				return new Effet(name,null,"gauche",0);
			}
		}
		if(name.equals("commerce_ressources_secondaires")){
				return new Effet(name,null,"tous",0);
		}
		if(name.equals("gain_pièces")){
			return new Effet(name,null,null, 5);
		}
		if(name.equals("gain_boucliers")) {
			return new Effet(name,null,null,1);
		}
		if(name.equals("gain_ressources")){
			if(c.getNom().equals("Chantier")) {
				return new Effet(name,"bois",null,1);
			}
			if(c.getNom().equals("Cavité")) {
				return new Effet(name,"pierre",null,1);
			}
			if(c.getNom().equals("Bassin argileux")) {
				return new Effet(name,"argile",null,0);
			}
			if(c.getNom().equals("Filon")) {
				return new Effet(name,"minerai",null,0);
			}
			if(c.getNom().equals("Exploitation forestière")) {
				//ressources.put("pierre", 1);
				return new Effet(name,"bois",null,1);
			}
			if(c.getNom().equals("Excavation")) {
				//ressources.put("argile", 1);
				return new Effet(name,"pierre",null,1);
			}
			if(c.getNom().equals("Friche")) {
				//ressources.put("argile",1);
				return new Effet(name,"bois",null,1);
			}
			if(c.getNom().equals("Fosse argileuse")) {
				//ressources.put("argile", 1);
				return new Effet(name,"minerai",null,1);
			}		
			if(c.getNom().equals("Mine")) {
				//ressources.put("pierre",1);
				return new Effet(name,"minerai",null,1);
			}
			if(c.getNom().equals("Gisement")) {
				//ressources.put("bois", 1);
				return new Effet(name,"minerai",null,1);
			}
			if(c.getNom().equals("Métier à tisser")) {
				return new Effet(name,"tissu",null,1);
			}
			if(c.getNom().equals("Verrerie")) {
				return new Effet(name,"verre",null,1);
			}
			if(c.getNom().equals("Presse")) {
				return new Effet(name,"parchemin",null,1);
			}
		}
		if(name.equals("gain_points_victoire")) {
			if(c.getNom().equals("Autel")) {
				return new Effet(name,null,null,2);
			}
			if(c.getNom().equals("Bains")) {
				return new Effet(name,null,null,3);
			}
			if(c.getNom().equals("Théâtre")) {
				return new Effet(name,null,null,2);
			}
			if(c.getNom().equals("Prêteur sur gage")) {
				return new Effet(name,null,null,3);
			}
		}
		return null;
	}
}
