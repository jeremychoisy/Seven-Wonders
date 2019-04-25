package org.Model.tools;

import java.util.ArrayList;
import java.util.Map;

import org.Model.assets.Carte;
import org.Model.assets.Joueur;


public abstract class GestionEffets
{
	public static void appliquerEffet(Map<String,String> effet, Joueur j) {
		if(effet.get("nomEffet") != null) {
			if (effet.get("nomEffet").equals("commerceRessourcesPrimaires")) {
				j.setcommerceRessourcesPrimaires(true);
			}
			if (effet.get("nomEffet").equals("commerceRessourcesSecondaires")) {
				j.setcommerceRessourcesSecondaires(true);
			}
			if (effet.get("nomEffet").equals("gain_pièces")) {
				j.addPièces(Integer.parseInt(effet.get("valeurEffet")));
			}
			if (effet.get("nomEffet").equals("gain_pointsVictoire")) {
				j.setPointsVictoire(j.getPointsVictoire() + Integer.parseInt(effet.get("valeurEffet")));
			}
			if (effet.get("nomEffet").equals("gain_boucliers")) {
				j.setBouclier(j.getBouclier() + Integer.parseInt(effet.get("valeurEffet")));
			}
			if (effet.get("nomEffet").equals("gain_ressources")) {
				if (j.getRessources().get(effet.get("ressourceEffet")) != null) {
					j.getRessources().put(effet.get("ressourceEffet"), j.getRessources().get(effet.get("ressourceEffet")) + Integer.parseInt(effet.get("valeurEffet")));
				} else {
					j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffet")));
				}
			}

			if ( effet.get("nomEffet").equals("gain_symboles")){
				if(effet.get("symboleEffet").equals("ingénieur")){
					j.addSymboleIngenieur(1);
				} else if(effet.get("symboleEffet").equals("science")){
					j.addSymboleScience(1);
				} else {
					j.addSymboleTablette(1);
				}
			}
			if (effet.get("nomEffet").equals("gain_ressources_multiples")) {
				// TODO
			}
		}
	}
	

	public static void appliquerEffetFinDePartie(Map<String,String> effet, Joueur j, ArrayList<Carte> cartesPoseesGauche, ArrayList<Carte> cartesPoseesDroit, Joueur joueurGauche, Joueur joueurDroit) {
		if (effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_types_cartes")) {
			if (effet.get("TypeEffetFinDePartie").equals("Matières Premières")) {
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
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}

			if (effet.get("TypeEffetFinDePartie").equals("Produit Manufacturé")) {
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
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}

			if (effet.get("TypeEffetFinDePartie").equals("Bâtiment Commercial")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Bâtiment Commercial")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Bâtiment Commercial")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}

			if (effet.get("TypeEffetFinDePartie").equals("Bâtiment Scientifique")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Bâtiment Scientifique")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Bâtiment Scientifique")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}

			if (effet.get("TypeEffetFinDePartie").equals("Conflit Militaire")) {
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
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}

			if (effet.get("TypeEffetFinDePartie").equals("Bâtiment Civil")) {
				int compteurGauche = 0;
				for (Carte carte : cartesPoseesGauche) {
					if (carte.getType().equals("Bâtiment Civil")) {
						compteurGauche += 1;

					}
				}

				int compteurDroit = 0;
				for (Carte carte : cartesPoseesDroit) {
					if (carte.getType().equals("Bâtiment Civil")) {
						compteurDroit += 1;

					}
				}
				if (compteurDroit >= 1 || compteurGauche >= 1) {
					j.addPointsVictoire((compteurDroit + compteurGauche) * Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}

			}


		}
		else if(effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_types_cartes_perso")){
			if (effet.get("TypeEffetFinDePartie").equals("Matières Premières")) {
				for (Carte carte : j.getCartesPosees()) {
					if (carte.getType().equals("Matières Premières")) {
						j.addPointsVictoire(Integer.parseInt(effet.get("valeurEffetFinDePartie")));
					}
				}
			}
			else if (effet.get("TypeEffetFinDePartie").equals("Produit Manufacturé")) {
				for (Carte carte : j.getCartesPosees()) {
					if (carte.getType().equals("Produit Manufacturé")) {
						j.addPointsVictoire(Integer.parseInt(effet.get("valeurEffetFinDePartie")));
					}
				}
			}
			if (effet.get("TypeEffetFinDePartie").equals("Bâtiment Commercial")) {
				for (Carte carte : j.getCartesPosees()) {
					if (carte.getType().equals("Bâtiment Commercial")) {
						j.addPointsVictoire(Integer.parseInt(effet.get("valeurEffetFinDePartie")));
					}
				}
				
			}
		}
		else if(effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_étapes_merveille_perso")){
			j.addPointsVictoire(Integer.parseInt(effet.get("valeurEffetFinDePartie"))*j.getMerveille().getEtapeCourante());
		}

		else if(effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_étapes_merveille")){
			for (int i = 1; i < j.getMerveille().getEtapeCourante(); i++){

				j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffetFinDePartie")));

			}
			for (int i = 1; i < joueurGauche.getMerveille().getEtapeCourante(); i++){

				j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffetFinDePartie")));

			}
			for (int i = 1; i < joueurDroit.getMerveille().getEtapeCourante(); i++){

				j.getRessources().put(effet.get("ressourceEffet"), Integer.parseInt(effet.get("valeurEffetFinDePartie")));

			}
		}

		else if(effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_types_cartes_multiples")){
			for (Carte carte : j.getCartesPosees()) {
				if (carte.getType().equals("Matières Premières") || carte.getType().equals("Produit Manufacturé") || carte.getType().equals("Guilde")) {
					j.addPointsVictoire(Integer.parseInt(effet.get("valeurEffetFinDePartie")));
				}
			}
		}

		else if(effet.get("nomEffetFinDePartie").equals("choix_symbole_scientifique")){
			if(j.getSymboleIngenieur() <= j.getSymboleScience() && j.getSymboleIngenieur() <= j.getSymboleTablette()){
				j.addSymboleIngenieur(1);
			} else if(j.getSymboleScience() <= j.getSymboleIngenieur() && j.getSymboleScience() <= j.getSymboleTablette()){
				j.addSymboleScience(1);
			} else {
				j.addSymboleTablette(1);
			}
		}

		else if(effet.get("nomEffetFinDePartie").equals("gain_pointsVictoire_par_jetons_défaites")){
			if(joueurDroit.getJetonsDefaites() > 0){
				j.addPointsVictoire(joueurDroit.getJetonsDefaites());

			}
			if(joueurGauche.getJetonsDefaites() > 0){
				j.addPointsVictoire(joueurGauche.getJetonsDefaites());

			}

		}


	}
}
