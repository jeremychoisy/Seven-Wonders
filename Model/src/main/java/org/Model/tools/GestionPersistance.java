package org.Model.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.Model.assets.Carte;
import org.Model.assets.Merveille;

import com.google.gson.Gson;

public class GestionPersistance {
	
    public static boolean isData() {
    	File fileCarte = new File("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
    	File fileMerveille = new File("/home/jeremy/eclipse-workspace/seven-wonders/Assets/merveilles.json");
    	return (fileCarte.exists() && fileMerveille.exists());
    }
    
    public static void generateData() {
    	Gson gson = new Gson();
		FileWriter writer = null;
		try {
			writer = new FileWriter("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
			Carte[] c = new Carte[14];
			c[0] = new Carte("Marché","Commerce",0);
			c[1] = new Carte("Tour","Militaire",0);
			c[2] = new Carte("Champs","Agricole",2);
			c[3] = new Carte("Echoppe","Commerce",3);
			c[4] = new Carte("Caserne","Militaire",2);
			c[5] = new Carte("Boucher","Commerce",2);
			c[6] = new Carte("Boulangerie","Commerce",0);
			c[7] = new Carte("Poissonier","Commerce",0);
			c[8] = new Carte("Etable","Militaire",3);
			c[9] = new Carte("Ferme","Agricole",2);
			c[10] = new Carte("Tisseur","Commerce",2);
			c[11] = new Carte("Flotte","Militaire",2);
			c[12] = new Carte("Cordonnier","Commerce",3);
			c[13] = new Carte("Armurerie","Commerce",2);
			gson.toJson(c, writer);
			writer.flush();
			writer.close();
			
			writer = new FileWriter("/home/jeremy/eclipse-workspace/seven-wonders/Assets/merveilles.json");
			Merveille[] m = new Merveille[3];
			m[0] = new Merveille("Olympia","bois");
			m[1] = new Merveille("Gizah","pierre");
			m[2] = new Merveille("Rhodos","minerai");
			gson.toJson(m,writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
