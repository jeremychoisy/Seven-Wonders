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
			Carte[] c = new Carte[34];

			c[0] = new Carte("Prêteur sur Gage","Bâtiment Civil",3);
			c[1] = new Carte("Bains","Bâtiment Civil",3);
			c[2] = new Carte("Autel","Bâtiment Civil",2);
			c[3] = new Carte("Théâtre","Bâtiment Civil",2);
			c[4] = new Carte("Prêteur sur Gage","Bâtiment Civil",3);
			c[5] = new Carte("Bains","Bâtiment Civil",3);
			c[6] = new Carte("Autel","Bâtiment Civil",2);
			c[7] = new Carte("Théâtre","Bâtiment Civil",2);
			c[8] = new Carte("Palissade","Conflit Militaire",0);
			c[9] = new Carte("Caserne","Conflit Militaire",0);
			c[10] = new Carte("Tour de Garde","Conflit Militaire",0);
			c[11] = new Carte("Taverne","Bâtiment Commercial",0);
			c[12] = new Carte("Comptoir Est","Bâtiment Commercial",0);
			c[13] = new Carte("Comptoir Ouest","Bâtiment Commercial",0);
			c[14] = new Carte("Marché","Bâtiment Commercial",0);
			c[15] = new Carte("Officine","Bâtiment Scientifique",0);
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
            c[33] = new Carte("Guilde des Bâtisseurs","Guilde",0);


			gson.toJson(c, writer);
			writer.flush();
			writer.close();
			
			writer = new FileWriter("./../Assets/merveilles.json");
			Merveille[] m = new Merveille[3];
			HashMap<String,Integer> e1 = new HashMap<String,Integer>();
			e1.put("bois", 2);
			HashMap<String,Integer> e2 = new HashMap<String,Integer>();
			e2.put("pierre", 2);
			HashMap<String,Integer> e3 = new HashMap<String,Integer>();
			e3.put("minerai", 2);
			m[0] = new Merveille("Olympia","bois",e1,e2,e3);
			e1.clear();
			e2.clear();
			e3.clear();
			e1.put("pierre",2);
			e2.put("bois",3);
			e3.put("pierre",4);
			m[1] = new Merveille("Gizah","pierre",e1,e2,e3);
			e1.clear();
			e2.clear();
			e3.clear();
			e1.put("bois",2);
			e2.put("argile",3);
			e3.put("minerai",4);
			m[2] = new Merveille("Rhodos","minerai",e1,e2,e3);
			e1.clear();
			e2.clear();
			e3.clear();
			e1.put("pierre",2);
			e2.put("bois",2);
			e3.put("papyrus",2);
			m[2] = new Merveille("Ephesos","papyrus",e1,e2,e3);
			
			gson.toJson(m,writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
