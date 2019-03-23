package org.Model.tools;

import java.util.Map;

import org.Model.assets.Joueur;

public abstract class GestionEffets
{
	public static void appliquerEffet(Map<String,String> effet, Joueur j) {
		if(effet.get("nomEffet").equals("commerceRessourcesPrimaires")) {
			j.setcommerceRessourcesPrimaires(true);
		}
		if(effet.get("nomEffet").equals("commerceRessourcesSecondaires")) {
			j.setcommerceRessourcesSecondaires(true);
		}
		if(effet.get("nomEffet").equals("gain_pièces")) {
			j.addPièces(Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("gain_pointsVictoire")) {
			j.setPointsVictoire(j.getPointsVictoire() + Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("gain_boucliers")) {
			j.setBouclier(j.getBouclier() + Integer.parseInt(effet.get("valeurEffet")));
		}
		if(effet.get("nomEffet").equals("batiment_gratuit")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_ressources")) {
			if(j.getRessources().get(effet.get("ressourceEffet")) != null){
				j.getRessources().put(effet.get("ressourceEffet"),j.getRessources().get(effet.get("ressourceEffet")) + Integer.parseInt(effet.get("valeurEffet")));
			}
			else
			{
				j.getRessources().put(effet.get("ressourceEffet"),Integer.parseInt(effet.get("valeurEffet")));
			}
		}
		if(effet.get("nomEffet").equals("gain_ressources_multiples")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_pièces_par_cartes")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_pointsVictoire_par_cartes")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_pointsVictoire_par_merveille")) {
			// TODO
		}
		if(effet.get("nomEffet").equals("gain_pointsVictoire_par_merveille")) {
			// TODO
		}
	}
}
