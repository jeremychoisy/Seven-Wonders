package org.Model.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.Model.assets.Carte;
import org.Model.assets.Merveille;

import com.google.gson.Gson;

public class GestionPersistance {
	
    public static boolean isData() {
    	File fileCarte = new File("./../Assets/cartes.json");
    	File fileMerveille = new File("./../Assets/merveilles.json");
    	return (fileCarte.exists() && fileMerveille.exists());
    }
    
    public static void generateData() {
    	Gson gson = new Gson();
		FileWriter writer = null;
		try {

			writer = new FileWriter("./../Assets/cartes.json");
			Carte[] c = new Carte[29];
			
			c[0] = new Carte("Prêteur sur gage","Batiment Civil","gain_points_victoire", 3, "", "", 7);
			c[1] = new Carte("Bains","Batiment Civil","gain_points_victoire", 3, "", "", 3);
			c[2] = new Carte("Autel","Batiment Civil","gain_points_victoire", 2, "", "", 5);
			c[3] = new Carte("Théâtre","Batiment Civil","gain_points_victoire", 2, "", "", 3);
			c[4] = new Carte("Prêteur sur gage","Batiment Civil","gain_points_victoire", 3, "", "", 4);
			c[5] = new Carte("Bains","Batiment Civil","gain_points_victoire", 3, "", "", 7);
			c[6] = new Carte("Autel","Batiment Civil","gain_points_victoire", 2, "", "", 3);
			c[7] = new Carte("Théâtre","Batiment Civil","gain_points_victoire", 2, "", "", 6);
			c[8] = new Carte("Palissade","Conflit Militaire","gain_boucliers", 1, "", "", 3);
			c[9] = new Carte("Caserne","Conflit Militaire","gain_boucliers", 1, "", "", 3);
			c[10] = new Carte("Tour de Garde","Conflit Militaire","gain_boucliers", 1, "", "", 3);
			c[11] = new Carte("Palissade","Conflit Militaire","gain_boucliers", 1, "", "", 7);
			c[12] = new Carte("Caserne","Conflit Militaire","gain_boucliers", 1, "", "", 5);
			c[13] = new Carte("Tour de Garde","Conflit Militaire","gain_boucliers", 1, "", "", 4);
			c[14] = new Carte("Taverne","Batiment Commercial","gain_pièces", 5, "", "", 4);
			c[15] = new Carte("Taverne","Batiment Commercial","gain_pièces", 5, "", "", 5);
			c[16] = new Carte("Taverne","Batiment Commercial","gain_pièces", 5, "", "", 7);
			c[17] = new Carte("Comptoir Est","Batiment Commercial","commerce_ressources_primaires", 0, "droite", "", 7);
			c[18] = new Carte("Comptoir Est","Batiment Commercial","commerce_ressources_primaires", 0, "droite", "", 3);
			c[19] = new Carte("Comptoir Ouest","Batiment Commercial","commerce_ressources_primaires", 0, "gauche", "", 7);
			c[20] = new Carte("Comptoir Ouest","Batiment Commercial","commerce_ressources_primaires", 0, "gauche", "", 3);
			c[21] = new Carte("Marché","Batiment Commercial","commerce_ressources_secondaires", 0, "tous", "", 3);
			c[22] = new Carte("Marché","Batiment Commercial","commerce_ressources_secondaires", 0, "tous", "", 6);
            c[23] = new Carte("Métier à tisser","Produit Manufacture","gain_ressources", 1, "", "tissu", 3);
            c[24] = new Carte("Métier à tisser","Produit Manufacture","gain_ressources", 1, "", "tissu", 6);
            c[25] = new Carte("Verrerie","Produit Manufacture","gain_ressources", 1, "", "verre", 3);
            c[26] = new Carte("Verrerie","Produit Manufacture","gain_ressources", 1, "", "verre", 6);
            c[27] = new Carte("Presse","Produit Manufacture","gain_ressources", 1, "", "parchemin", 3);
            c[28] = new Carte("Presse","Produit Manufacture","gain_ressources", 1, "", "parchemin", 6);
			/*c[15] = new Carte("Officine","Bâtiment Scientifique",0);
			c[16] = new Carte("Atelier","Bâtiment Scientifique",0);
			c[17] = new Carte("Scriptorium","Bâtiment Scientifique",0);
            c[18] = new Carte("Métier à tisser","Produit Manufacturé",0);
            c[19] = new Carte("Verrerie","Produit Manufacturé",0);
            c[20] = new Carte("Presse","Produit Manufacturé",0);
            c[21] = new Carte("Métier à tisser","Produit Manufacturé",0);
            c[22] = new Carte("Verrerie","Produit Manufacturé",0);
            c[23] = new Carte("Presse","Produit Manufacturé",0); 
            c[24] = new Carte("Guide des Travailleurs","Guilde",0);
            c[25] = new Carte("Guilde des Artisans","Guilde",0);
            c[26] = new Carte("Guilde des Commerçants","Guilde",0);
            c[27] = new Carte("Guilde des Philosophes","Guilde",0);
            c[28] = new Carte("Guilde des Espions","Guilde",0);
            c[29] = new Carte("Guilde des Stratèges","Guilde",0);
            c[30] = new Carte("Guilde des Armateurs","Guilde",0);
            c[31] = new Carte("Guilde des Scientifiques","Guilde",0);
            c[32] = new Carte("Guilde des Magistrats","Guilde",0);
            c[33] = new Carte("Guilde des Bâtisseurs","Guilde",0);*/


			gson.toJson(c, writer);
			writer.flush();
			writer.close();
			
			writer = new FileWriter("./../Assets/merveilles.json");
			Merveille[] m = new Merveille[4];
			HashMap<String,Integer> ressourceEtapeUne = new HashMap<String,Integer>();
			ressourceEtapeUne.put("bois", 2);
			HashMap<String,Integer> ressourceEtapeDeux = new HashMap<String,Integer>();
			ressourceEtapeDeux.put("pierre", 2);
			HashMap<String,Integer> ressourceEtapeTrois = new HashMap<String,Integer>();
			ressourceEtapeTrois.put("minerai", 2);
			
			HashMap<String,String> effetEtapeUne = new HashMap<String,String>();
			HashMap<String,String> effetEtapeDeux = new HashMap<String,String>();
			HashMap<String,String> effetEtapeTrois = new HashMap<String,String>();
			
			effetEtapeUne.put("nomEffet", "gain_points_victoire");
			effetEtapeUne.put("valeurEffet", "3");
			effetEtapeDeux.put("nomEffet", "batiment_gratuit");
			effetEtapeTrois.put("nomEffet", "gain_points_victoire");
			effetEtapeTrois.put("valeurEffet", "7");

			
			
			m[0] = new Merveille("Olympia","bois",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);
			
			ressourceEtapeUne.clear();
			ressourceEtapeDeux.clear();
			ressourceEtapeTrois.clear();
			
			effetEtapeUne.clear();
			effetEtapeDeux.clear();
			effetEtapeTrois.clear();

			ressourceEtapeUne.put("pierre",2);
			ressourceEtapeDeux.put("bois",3);
			ressourceEtapeTrois.put("pierre",4);
			
			effetEtapeUne.put("nomEffet", "gain_points_victoire");
			effetEtapeUne.put("valeurEffet", "3");
			effetEtapeDeux.put("nomEffet", "gain_points_victoire");
			effetEtapeDeux.put("valeurEffet", "5");
			effetEtapeTrois.put("nomEffet", "gain_points_victoire");
			effetEtapeTrois.put("valeurEffet", "7");
			
			m[1] = new Merveille("Gizah","pierre",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);
			
			ressourceEtapeUne.clear();
			ressourceEtapeDeux.clear();
			ressourceEtapeTrois.clear();
			
			effetEtapeUne.clear();
			effetEtapeDeux.clear();
			effetEtapeTrois.clear();
			
			ressourceEtapeUne.put("bois",2);
			ressourceEtapeDeux.put("argile",3);
			ressourceEtapeTrois.put("minerai",4);
			
			effetEtapeUne.put("nomEffet", "gain_points_victoire");
			effetEtapeUne.put("valeurEffet", "3");
			effetEtapeDeux.put("nomEffet", "gain_boucliers");
			effetEtapeDeux.put("valeurEffet", "2");
			effetEtapeTrois.put("nomEffet", "gain_points_victoire");
			effetEtapeTrois.put("valeurEffet", "7");
			
			m[2] = new Merveille("Rhodos","minerai",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);
			
			ressourceEtapeUne.clear();
			ressourceEtapeDeux.clear();
			ressourceEtapeTrois.clear();
			
			effetEtapeUne.clear();
			effetEtapeDeux.clear();
			effetEtapeTrois.clear();
			
			ressourceEtapeUne.put("pierre",2);
			ressourceEtapeDeux.put("bois",2);
			ressourceEtapeTrois.put("papyrus",2);
			
			effetEtapeUne.put("nomEffet", "gain_points_victoire");
			effetEtapeUne.put("valeurEffet", "3");
			effetEtapeDeux.put("nomEffet", "gain_pièces");
			effetEtapeDeux.put("valeurEffet", "9");
			effetEtapeTrois.put("nomEffet", "gain_points_victoire");
			effetEtapeTrois.put("valeurEffet", "7");
			
			m[3] = new Merveille("Ephesos","papyrus",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);
			
			gson.toJson(m,writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
