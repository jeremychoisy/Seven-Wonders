package org.Model.tools;

import java.util.HashMap;

import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Merveille;

public abstract class GestionEffets
{
	public static void  appliquerEffetCarte(Carte c, Joueur j) {
		if(c.getNomEffet().equals("commerce_ressources_primaires")) {
			j.setCommerce_ressources_primaires(true);
		}
		if(c.getNomEffet().equals("commerce_ressources_secondaires")) {
			j.setCommerce_ressources_secondaires(true);
		}
		if(c.getNomEffet().equals("gain_pièces")) {
			j.setPièces(j.getPièces() + c.getValeurEffet());
		}
		if(c.getNomEffet().equals("gain_points_victoire")) {
			j.setPoints_victoire(j.getPoints_victoire() + c.getValeurEffet());
		}
		if(c.getNomEffet().equals("gain_boucliers")) {
			j.setBouclier(j.getBouclier() + c.getValeurEffet());
		}
		if(c.getNomEffet().equals("gain_ressources")) {
			j.ajouterRessources(c.getRessourceEffet(), c.getValeurEffet());
		}	

	}
	
	public static void appliquerEffetMerveille(HashMap<String,String> effetEtape, Joueur j) {
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
