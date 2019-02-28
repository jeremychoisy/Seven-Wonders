package org.Model.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.Model.assets.Carte;
import org.Model.assets.Merveille;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GestionPersistance {
	
	// Méthodes pour sérialisation / désérialisation des échanges serveur/client.
	public static Map<String,String> JSONToMapEffet(JSONObject j){
		return new Gson().fromJson(j.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
	}
	
	public static Map<String,Integer> JSONToMapRessource(JSONObject j){
		return new Gson().fromJson(j.toString(), new TypeToken<HashMap<String, Integer>>() {}.getType());
	}
	
	public static String ObjectToJSONString(Object o) {
		return new Gson().toJson(o);
	}
	
	// Méthodes pour sérialisation / désérialisation des données vers / depuis des fichiers json.
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
			
			Map<String,String> effet = new HashMap<String,String>();
			
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","3");
			
			c[0] = new Carte("Prêteur sur gage","Batiment Civil", effet, 7);
			c[1] = new Carte("Bains","Batiment Civil",effet, 3);
			c[2] = new Carte("Autel","Batiment Civil",effet,5);
			c[3] = new Carte("Théâtre","Batiment Civil",effet,3);
			c[4] = new Carte("Prêteur sur gage","Batiment Civil",effet, 4);
			c[5] = new Carte("Bains","Batiment Civil",effet, 7);
			c[6] = new Carte("Autel","Batiment Civil",effet, 3);
			c[7] = new Carte("Théâtre","Batiment Civil",effet, 6);
			c[8] = new Carte("Palissade","Conflit Militaire",effet, 3);
			c[9] = new Carte("Caserne","Conflit Militaire",effet, 3);
			c[10] = new Carte("Tour de Garde","Conflit Militaire",effet, 3);
			c[11] = new Carte("Palissade","Conflit Militaire",effet, 7);
			c[12] = new Carte("Caserne","Conflit Militaire",effet, 5);
			c[13] = new Carte("Tour de Garde","Conflit Militaire",effet, 4);
			c[14] = new Carte("Taverne","Batiment Commercial",effet, 4);
			c[15] = new Carte("Taverne","Batiment Commercial",effet, 5);
			c[16] = new Carte("Taverne","Batiment Commercial",effet, 7);
			c[17] = new Carte("Comptoir Est","Batiment Commercial",effet, 7);
			c[18] = new Carte("Comptoir Est","Batiment Commercial",effet, 3);
			c[19] = new Carte("Comptoir Ouest","Batiment Commercial",effet, 7);
			c[20] = new Carte("Comptoir Ouest","Batiment Commercial",effet, 3);
			c[21] = new Carte("Marché","Batiment Commercial",effet, 3);
			c[22] = new Carte("Marché","Batiment Commercial",effet, 6);
            c[23] = new Carte("Métier à tisser","Produit Manufacture",effet, 3);
            c[24] = new Carte("Métier à tisser","Produit Manufacture",effet, 6);
            c[25] = new Carte("Verrerie","Produit Manufacture",effet, 3);
            c[26] = new Carte("Verrerie","Produit Manufacture",effet, 6);
            c[27] = new Carte("Presse","Produit Manufacture",effet, 3);
            c[28] = new Carte("Presse","Produit Manufacture",effet, 6);
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
			Map<String,Integer> ressourceEtapeUne = new HashMap<String,Integer>();
			ressourceEtapeUne.put("bois", 2);
			Map<String,Integer> ressourceEtapeDeux = new HashMap<String,Integer>();
			ressourceEtapeDeux.put("pierre", 2);
			Map<String,Integer> ressourceEtapeTrois = new HashMap<String,Integer>();
			ressourceEtapeTrois.put("minerai", 2);
			
			Map<String,String> effetEtapeUne = new HashMap<String,String>();
			Map<String,String> effetEtapeDeux = new HashMap<String,String>();
			Map<String,String> effetEtapeTrois = new HashMap<String,String>();
			
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
