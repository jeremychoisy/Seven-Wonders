package org.Model.tools;

import java.util.Map;

import org.Model.assets.Joueur;

public abstract class GestionEffets
{
	public static void appliquerEffet(Map<String,String> effet, Joueur j) {
		System.out.println(effet.get("nomEffet"));
		if(effet.get("nomEffet").equals("commerce_ressources_primaires")) {
			j.setCommerce_ressources_primaires(true);
		}
		if(effet.get("nomEffet").equals("commerce_ressources_secondaires")) {
			j.setCommerce_ressources_secondaires(true);
		}
		if(effet.get("nomEffet").equals("gain_pièces")) {
			j.setPièces(j.getPièces() + Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("gain_points_victoire")) {
			System.out.println("OK");
			j.setPoints_victoire(j.getPoints_victoire() + Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("gain_boucliers")) {
			j.setBouclier(j.getBouclier() + Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("batiment_gratuit")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_ressources")) {
			if(j.GetRessources().get(effet.get("ressourceEffet")) != null){
				j.GetRessources().put(effet.get("ressourceEffet"),j.GetRessources().get(effet.get("ressourceEffet")) + Integer.parseInt(effet.get("valeurEffet")));
			}
			else
			{
				j.GetRessources().put(effet.get("ressourceEffet"),Integer.parseInt(effet.get("valeurEffet")));
			}
		}
		if(effet.get("nomEffet").equals("gain_ressources_multiples")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_pièces_par_cartes")) {
			// TODO
		}
	}
}
