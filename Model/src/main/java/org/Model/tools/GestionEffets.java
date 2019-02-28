package org.Model.tools;

import java.util.Map;

import org.Model.assets.Joueur;

public abstract class GestionEffets
{
	public static void appliquerEffet(Map<String,String> effetEtape, Joueur j) {
		if(effetEtape.get("nomEffet").equals("commerce_ressources_primaires")) {
			j.setCommerce_ressources_primaires(true);
		}
		if(effetEtape.get("nomEffet").equals("commerce_ressources_secondaires")) {
			j.setCommerce_ressources_secondaires(true);
		}
		if(effetEtape.get("nomEffet").equals("gain_pièces")) {
			j.setPièces(j.getPièces() + Integer.parseInt(effetEtape.get("valeurEffet")));
		}
		if(effetEtape.get("nomEffet").equals("gain_points_victoire")) {
			j.setPoints_victoire(j.getPoints_victoire() + Integer.parseInt(effetEtape.get("valeurEffet")));
		}
		if(effetEtape.get("nomEffet").equals("gain_boucliers")) {
			j.setBouclier(j.getBouclier() + Integer.parseInt(effetEtape.get("valeurEffet")));
		}
		if(effetEtape.get("nomEffet").equals("batiment_gratuit")) {
			// TODO
		}
	}
}
