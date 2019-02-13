package org.Model.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.Model.assets.Carte;

import com.google.gson.Gson;

public class GestionPersistance {
	
    public static boolean isData() {
    	File f = new File("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
    	return f.exists();
    }
    
    public static void generateData() {
    	Gson gson = new Gson();
		FileWriter writer = null;
		try {
			writer = new FileWriter("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
			Carte[] c = new Carte[7];
			c[0] = new Carte("Marché","vert");
			c[1] = new Carte("Tour","marron");
			c[2] = new Carte("Champs","vert");
			c[3] = new Carte("Echoppe","vert");
			c[4] = new Carte("Caserne","marron");
			c[5] = new Carte("Boucher","vert");
			c[6] = new Carte("Boulangerie","vert");
			gson.toJson(c, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
