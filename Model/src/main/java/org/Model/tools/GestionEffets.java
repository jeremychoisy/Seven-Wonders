package org.Model.tools;

import java.util.ArrayList;
import java.util.Map;

import org.Model.assets.Carte;
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
	}

	public static void appliquerEffetGuilde(Map<String,String> effet, Joueur j, ArrayList<Carte> cartesPoseesGauche, ArrayList<Carte> cartesPoseesDroit) {
		if (effet.get("nomEffet").equals("gain_pointsVictoire_par_types_cartes")) {
			if (effet.get("Type").equals("Matières Premières")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Matières Premières")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Matières Premières")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}

			if (effet.get("Type").equals("Produit Manufacturé")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Produit Manufacturé")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Produit Manufacturé")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}

			if (effet.get("Type").equals("Batiment Commercial")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Batiment Commercial")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Batiment Commercial")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}

			if (effet.get("Type").equals("Batiment Scientifique")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Batiment Scientifique")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Batiment Scientifique")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}

			if (effet.get("Type").equals("Conflit Militaire")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Conflit Militaire")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Conflit Militaire")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}

			if (effet.get("Type").equals("Batiment Civil")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Batiment Civil")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Batiment Civil")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 && compteurGauche >= 1) {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}

			}


		}


		if(effet.get("nomEffet").equals("gain_pointsVictoire_par_étapes_merveilles")){

			j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));

		}

		if(effet.get("nomEffet").equals("gain_pointsVictoire_par_types_cartes_multiples")){
			if(effet.get("Type").equals("Matières Premières") || effet.get("Type").equals("Multiples") || effet.get("Type").equals("Produit Manufacturé")){
				j.getRessources().put(effet.get("ressourceEffet"),Integer.parseInt(effet.get("valeurEffet")));
			}

		}

		if(effet.get("nomEffet").equals("choix_symbole_scientifique")){
			// TODO

		}


	}
}
