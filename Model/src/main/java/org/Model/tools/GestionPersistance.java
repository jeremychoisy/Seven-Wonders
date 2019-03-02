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
			Carte[] c = new Carte[98];
			
			Map<String,String> effet = new HashMap<String,String>();
			Map<String,Integer> ressources = new HashMap<String,Integer>();
						
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","3");
			
			// Age I
			c[0] = new Carte("Prêteur sur gage","Batiment Civil", effet,ressources, 7,1);
			c[1] = new Carte("Prêteur sur gage","Batiment Civil",effet,ressources, 4,1);
			
			ressources = new HashMap<String,Integer>();
			ressources.put("pierre",1);
			
			c[2] = new Carte("Bains","Batiment Civil",effet,ressources, 3,1);
			c[3] = new Carte("Bains","Batiment Civil",effet,ressources, 7,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","2");
			
			ressources = new HashMap<String,Integer>();
			
			c[4] = new Carte("Autel","Batiment Civil",effet,ressources,5,1);
			c[5] = new Carte("Autel","Batiment Civil",effet,ressources, 3,1);
			c[6] = new Carte("Théâtre","Batiment Civil",effet,ressources,3,1);
			c[7] = new Carte("Théâtre","Batiment Civil",effet,ressources, 6,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_boucliers");
			effet.put("valeurEffet", "1");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("bois",1);
			
			c[8] = new Carte("Palissade","Conflit Militaire",effet,ressources, 7,1);
			c[9] = new Carte("Palissade","Conflit Militaire",effet,ressources, 3,1);
			
			ressources = new HashMap<String,Integer>();
			ressources.put("minerai",1);
			
			c[10] = new Carte("Caserne","Conflit Militaire",effet,ressources, 3,1);
			c[11] = new Carte("Caserne","Conflit Militaire",effet,ressources, 5,1);
			
			ressources = new HashMap<String,Integer>();
			ressources.put("argile",1);
			
			c[12] = new Carte("Tour de Garde","Conflit Militaire",effet,ressources, 3,1);
			c[13] = new Carte("Tour de Garde","Conflit Militaire",effet,ressources, 4,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_pièces");
			effet.put("valeurEffet", "5");
			
			ressources = new HashMap<String,Integer>();


			c[14] = new Carte("Taverne","Batiment Commercial",effet,ressources, 4,1);
			c[15] = new Carte("Taverne","Batiment Commercial",effet,ressources, 5,1);
			c[16] = new Carte("Taverne","Batiment Commercial",effet,ressources, 7,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "commerce_ressources_primaires");
			effet.put("orientationEffet", "droite");
			
			c[17] = new Carte("Comptoir Est","Batiment Commercial",effet,ressources, 7,1);
			c[18] = new Carte("Comptoir Est","Batiment Commercial",effet,ressources, 3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "commerce_ressources_primaires");
			effet.put("orientationEffet", "gauche");
			
			c[19] = new Carte("Comptoir Ouest","Batiment Commercial",effet,ressources, 7,1);
			c[20] = new Carte("Comptoir Ouest","Batiment Commercial",effet,ressources, 3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "commerce_ressources_secondaires");
			effet.put("orientationEffet", "droiteGauche");
			
			c[21] = new Carte("Marché","Batiment Commercial",effet,ressources, 3,1);
			c[22] = new Carte("Marché","Batiment Commercial",effet,ressources, 6,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "tissu");
			effet.put("valeurEffet", "1");
			
            c[23] = new Carte("Métier à tisser","Produit Manufacturé",effet,ressources, 3,1);
            c[24] = new Carte("Métier à tisser","Produit Manufacturé",effet,ressources, 6,1);
            
            effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "verre");
			effet.put("valeurEffet", "1");
			
            c[25] = new Carte("Verrerie","Produit Manufacturé",effet,ressources, 3,1);
            c[26] = new Carte("Verrerie","Produit Manufacturé",effet,ressources, 6,1);
            
            effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "papier");
			effet.put("valeurEffet", "1");
            
            c[27] = new Carte("Presse","Produit Manufacturé",effet,ressources, 3,1);
            c[28] = new Carte("Presse","Produit Manufacturé",effet,ressources, 6,1);
            
            effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
            effet.put("symboleEffet","science");
            
            ressources = new HashMap<String,Integer>();
			ressources.put("tissu",1);
            
			c[29] = new Carte("Officine","Bâtiment Scientifique",effet,ressources,3,1);
			c[30] = new Carte("Officine","Bâtiment Scientifique",effet,ressources,5,1);
			
			effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
			effet.put("symboleEffet","ingénieur");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("verre",1);
			
			c[31] = new Carte("Atelier","Bâtiment Scientifique",effet,ressources,7,1);
			c[32] = new Carte("Atelier","Bâtiment Scientifique",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
			effet.put("symboleEffet","tablette");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("papier",1);
			
			c[33] = new Carte("Scriptorium","Bâtiment Scientifique",effet,ressources,4,1);
			c[34] = new Carte("Scriptorium","Bâtiment Scientifique",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "pierre");
			effet.put("valeurEffet", "1");
			
			ressources = new HashMap<String,Integer>();
			
			c[35] = new Carte("Cavité","Matières Premières",effet,ressources,5,1);
			c[36] = new Carte("Cavité","Matières Premières",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "bois");
			effet.put("valeurEffet", "1");
			
			c[37] = new Carte("Chantier","Matières Premières",effet,ressources,4,1);
			c[38] = new Carte("Chantier","Matières Premières",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "minerai");
			effet.put("valeurEffet", "1");
			
			c[39] = new Carte("Filon","Matières Premières",effet,ressources,4,1);
			c[40] = new Carte("Filon","Matières Premières",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "argile");
			effet.put("valeurEffet", "1");
			
			c[41] = new Carte("Bassin Argileux","Matières Premières",effet,ressources,5,1);
			c[42] = new Carte("Bassin Argileux","Matières Premières",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet", "argile");
			effet.put("ressourceEffet2","pierre");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("pièces",1);
			
			c[43] = new Carte("Excavation","Matières Premières",effet,ressources,4,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "argile");
			effet.put("ressourceEffet2","bois");
			
			c[44] = new Carte("Friche","Matières Premières",effet,ressources,6,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "pierre");
			effet.put("ressourceEffet2","bois");
			
			c[45] = new Carte("Exploitation Forestière","Matières Premières",effet,ressources,3,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "pierre");
			effet.put("ressourceEffet2","minerai");
			
			c[46] = new Carte("Mine","Matières Premières",effet,ressources,6,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "bois");
			effet.put("ressourceEffet2","minerai");
			
			c[47] = new Carte("Gisement","Matières Premières",effet,ressources,5,1);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "argile");
			effet.put("ressourceEffet2","minerai");
			
			c[48] = new Carte("Fosse Argileuse","Matières Premières",effet,ressources,3,1);
			
			// Age II
			ressources = new HashMap<String,Integer>();

			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "tissu");
			effet.put("valeurEffet", "1");
			
            c[49] = new Carte("Métier à tisser","Produit Manufacturé",effet,ressources, 3,2);
            c[50] = new Carte("Métier à tisser","Produit Manufacturé",effet,ressources, 5,2);
            
            effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "verre");
			effet.put("valeurEffet", "1");
			
            c[51] = new Carte("Verrerie","Produit Manufacturé",effet,ressources, 3,2);
            c[52] = new Carte("Verrerie","Produit Manufacturé",effet,ressources, 5,2);
            
            effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "papier");
			effet.put("valeurEffet", "1");
            
            c[53] = new Carte("Presse","Produit Manufacturé",effet,ressources, 3,2);
            c[54] = new Carte("Presse","Produit Manufacturé",effet,ressources, 5,2);
 
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "pierre");
			effet.put("valeurEffet", "2");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("pièces",1);
			
			c[55] = new Carte("Carrière","Matières Premières",effet,ressources,4,2);
			c[56] = new Carte("Carrière","Matières Premières",effet,ressources,3,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "bois");
			effet.put("valeurEffet", "2");
			
			c[57] = new Carte("Scierie","Matières Premières",effet,ressources,4,2);
			c[58] = new Carte("Scierie","Matières Premières",effet,ressources,3,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "minerai");
			effet.put("valeurEffet", "2");
			
			c[59] = new Carte("Fonderie","Matières Premières",effet,ressources,4,2);
			c[60] = new Carte("Fonderie","Matières Premières",effet,ressources,3,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressources");
			effet.put("ressourceEffet", "argile");
			effet.put("valeurEffet", "2");
			
			c[61] = new Carte("Briqueterie","Matières Premières",effet,ressources,4,2);
			c[62] = new Carte("Briqueterie","Matières Premières",effet,ressources,3,2);
			
            effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
            effet.put("symboleEffet","science");
            
            ressources = new HashMap<String,Integer>();
			ressources.put("verre",1);
			ressources.put("minerai",2);
			           
			c[63] = new Carte("Dispensaire","Bâtiment Scientifique",effet,ressources,3,2);
			c[64] = new Carte("Dispensaire","Bâtiment Scientifique",effet,ressources,4,2);
			
			effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
			effet.put("symboleEffet","ingénieur");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("papier",1);
			ressources.put("argile",2);
			
			c[65] = new Carte("Laboratoire","Bâtiment Scientifique",effet,ressources,5,2);
			c[66] = new Carte("Laboratoire","Bâtiment Scientifique",effet,ressources,3,2);
			
			effet = new HashMap<String,String>();
            effet.put("nomEffet","gain_symboles");
			effet.put("symboleEffet","tablette");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("tissu",1);
			ressources.put("pierre",2);
			
			c[67] = new Carte("Bibliothèque","Bâtiment Scientifique",effet,ressources,3,2);
			c[68] = new Carte("Bibliothèque","Bâtiment Scientifique",effet,ressources,6,2);
			
            ressources = new HashMap<String,Integer>();
			ressources.put("papier",1);
			ressources.put("bois",1);
			
			c[69] = new Carte("Ecole","Bâtiment Scientifique",effet,ressources,7,2);
			c[70] = new Carte("Ecole","Bâtiment Scientifique",effet,ressources,3,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_boucliers");
			effet.put("valeurEffet", "2");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("minerai",1);
			ressources.put("bois",1);
			ressources.put("argile",1);
			
			c[71] = new Carte("Ecuries","Conflit Militaire",effet,ressources, 5,2);
			c[72] = new Carte("Ecuries","Conflit Militaire",effet,ressources, 3,2);
			
            ressources = new HashMap<String,Integer>();
			ressources.put("minerai",1);
			ressources.put("bois",2);
			
			c[73] = new Carte("Champs de tir","Conflit Militaire",effet,ressources, 3,2);
			c[74] = new Carte("Champs de tir","Conflit Militaire",effet,ressources, 6,2);
			
            ressources = new HashMap<String,Integer>();
			ressources.put("pierre",3);
			
			c[75] = new Carte("Muraille","Conflit Militaire",effet,ressources, 3,2);
			c[76] = new Carte("Muraille","Conflit Militaire",effet,ressources, 7,2);
			
            ressources = new HashMap<String,Integer>();
			ressources.put("minerai",2);
			ressources.put("bois",1);
			
			c[77] = new Carte("Place d'armes","Conflit Militaire",effet,ressources, 4,2);
			c[78] = new Carte("Place d'armes","Conflit Militaire",effet,ressources, 6,2);
			c[79] = new Carte("Place d'armes","Conflit Militaire",effet,ressources, 7,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","3");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("verre",1);
			ressources.put("bois",1);
			ressources.put("argile",1);
			
			c[80] = new Carte("Temple","Batiment Civil", effet,ressources, 6,2);
			c[81] = new Carte("Temple","Batiment Civil",effet,ressources, 3,2);			

			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","4");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("tissu",1);
			ressources.put("argile",2);
			
			c[82] = new Carte("Tribunal","Batiment Civil",effet,ressources,5,2);
			c[83] = new Carte("Tribunal","Batiment Civil",effet,ressources, 3,2);
			
            ressources = new HashMap<String,Integer>();
			ressources.put("minerai",2);
			ressources.put("bois",1);
			
			c[84] = new Carte("Statue","Batiment Civil",effet,ressources,3,2);
			c[85] = new Carte("Statue","Batiment Civil",effet,ressources, 7,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_points_victoire");
			effet.put("valeurEffet","5");
			
            ressources = new HashMap<String,Integer>();
			ressources.put("pierre",3);
			
			c[86] = new Carte("Aqueduc","Batiment Civil",effet,ressources, 3,2);
			c[87] = new Carte("Aqueduc","Batiment Civil",effet,ressources, 7,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_pièces_par_cartes");
			effet.put("valeurEffet", "2");
			effet.put("couleurEffet","grise");
			
			ressources = new HashMap<String,Integer>();

			c[88] = new Carte("Bazar","Batiment Commercial",effet,ressources, 4,2);
			c[89] = new Carte("Bazar","Batiment Commercial",effet,ressources, 7,2);
			
			effet.put("valeurEffet", "1");
			effet.put("couleurEffet","marron");
			
			c[90] = new Carte("Vignoble","Batiment Commercial",effet,ressources, 6,2);
			c[91] = new Carte("Vignoble","Batiment Commercial",effet,ressources, 3,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "bois");
			effet.put("ressourceEffet2","minerai");
			effet.put("ressourceEffet3", "pierre");
			effet.put("ressourceEffet4","argile");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("bois", 2);
			
			c[92] = new Carte("Caravansérail","Batiment Commercial",effet,ressources, 6,2);
			c[93] = new Carte("Caravansérail","Batiment Commercial",effet,ressources, 3,2);
			c[94] = new Carte("Caravansérail","Batiment Commercial",effet,ressources, 5,2);
			
			effet = new HashMap<String,String>();
			effet.put("nomEffet", "gain_ressource_multiples");
			effet.put("valeurEffet", "1");
			effet.put("ressourceEffet1", "verre");
			effet.put("ressourceEffet2","papier");
			effet.put("ressourceEffet3", "tissu");
			
			ressources = new HashMap<String,Integer>();
			ressources.put("argile", 2);
			
			c[95] = new Carte("Forum","Batiment Commercial",effet,ressources, 6,2);
			c[96] = new Carte("Forum","Batiment Commercial",effet,ressources, 3,2);
			c[97] = new Carte("Forum","Batiment Commercial",effet,ressources, 7,2);

			/*
            c[] = new Carte("Guide des Travailleurs","Guilde",0);
            c[] = new Carte("Guilde des Artisans","Guilde",0);
            c[] = new Carte("Guilde des Commerçants","Guilde",0);
            c[] = new Carte("Guilde des Philosophes","Guilde",0);
            c[] = new Carte("Guilde des Espions","Guilde",0);
            c[] = new Carte("Guilde des Stratèges","Guilde",0);
            c[] = new Carte("Guilde des Armateurs","Guilde",0);
            c[] = new Carte("Guilde des Scientifiques","Guilde",0);
            c[] = new Carte("Guilde des Magistrats","Guilde",0);
            c[] = new Carte("Guilde des Bâtisseurs","Guilde",0);*/


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
			
			ressourceEtapeUne = new HashMap<String,Integer>();
			ressourceEtapeDeux = new HashMap<String,Integer>();
			ressourceEtapeTrois = new HashMap<String,Integer>();
			
			effetEtapeUne = new HashMap<String,String>();
			effetEtapeDeux = new HashMap<String,String>();
			effetEtapeTrois = new HashMap<String,String>();

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
			
			ressourceEtapeUne = new HashMap<String,Integer>();
			ressourceEtapeDeux = new HashMap<String,Integer>();
			ressourceEtapeTrois = new HashMap<String,Integer>();
			
			effetEtapeUne = new HashMap<String,String>();
			effetEtapeDeux = new HashMap<String,String>();
			effetEtapeTrois = new HashMap<String,String>();
			
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
			
			ressourceEtapeUne = new HashMap<String,Integer>();
			ressourceEtapeDeux = new HashMap<String,Integer>();
			ressourceEtapeTrois = new HashMap<String,Integer>();
			
			effetEtapeUne = new HashMap<String,String>();
			effetEtapeDeux = new HashMap<String,String>();
			effetEtapeTrois = new HashMap<String,String>();
			
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
