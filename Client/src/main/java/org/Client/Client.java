package org.Client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.assets.Id;
import org.Model.assets.Joueur;
import org.Model.tools.CouleurSorties;
import org.Model.tools.MyPrintStream;
import org.Model.assets.Carte;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class Client{
	private Socket connexion;
	private Id id;
	final Object attenteDeconnexion = new Object();
	
	private Joueur j;

	
	public Client(String url, final String name) {
		this.j = new Joueur(name);
		try {
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			connexion = IO.socket(url, opts);
			
			connexion.on("connect",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					log("connecté");
					id = new Id(name);
					JSONObject idJson = new JSONObject(id);
					connexion.emit("id", idJson);
					
				}
				
			});
			
			connexion.on("disconnect", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					log("deconnecté");
					connexion.disconnect();
					connexion.close();
					synchronized(attenteDeconnexion) {
						attenteDeconnexion.notify();
					}
				}
			});
			// Traitement de l'événement "voici ta main" venant du serveur
			connexion.on("main",new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					Carte c =null;
					JSONArray cJson = (JSONArray) args[0];
				    for(int i=0;i<7;i++) {
			
						try {
							c = new Carte((String)(cJson.getJSONObject(i).get("nom")),(String)(cJson.getJSONObject(i).get("type")),(String)(cJson.getJSONObject(i).get("nomEffet")),(Integer)(cJson.getJSONObject(i).get("valeurEffet")),(String)(cJson.getJSONObject(i).get("orientationEffet")),(String)(cJson.getJSONObject(i).get("ressourceEffet")),(Integer)cJson.getJSONObject(i).get("configurationNumber"));
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						j.getM().add(c);
				    }
					connexion.emit("readyCheck", "Prêt");
				}
				
			});
			
			// traitement de l'événement "voici tes pièces" venant du serveur
			connexion.on("pièces", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					int valeur = (Integer) args[0];
					j.setPièces(j.getPièces() + valeur);
					
				}
			});
			
			// Traitement de l'événement "c'est ton tour de jouer" venant du serveur
			connexion.on("Ton tour",new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					Carte c = null;
					c = j.getM().get(0);
					j.getM().remove(0);
					JSONObject carteJouéeJSON = new JSONObject(c);
					connexion.emit("Carte Jouée", carteJouéeJSON);
				}
			});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void demarrer() {
		log("Tentative de connexion");
		connexion.connect();

		synchronized(attenteDeconnexion) {
			try {
				attenteDeconnexion.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_BLUE + "Client [" + j.getNom() + "] : " + s + CouleurSorties.ANSI_RESET);
	}
	
	public static void main(String[] args) {

        try {
            System.setOut(new MyPrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Client client = new Client("http://127.0.0.1:10101",args[0]);
        client.demarrer();
	}
}
