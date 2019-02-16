package org.Client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.assets.Id;
import org.Model.assets.Main;
import org.Model.tools.MyPrintStream;
import org.Model.assets.Carte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class Client {
	private Socket connexion;
	private Id id;
	final Object attenteDeconnexion = new Object();
	
	private Main m;
	
	public Client(String url) {
		try {
			connexion = IO.socket(url);
			
			connexion.on("connect",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					log("connecté");
					id = new Id("Joueur 1");
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
			connexion.on("main",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					Carte c =null;
					m = new Main();
					JSONArray cJson = (JSONArray) args[0];

				    for(int i=0;i<7;i++) {
						try {
							c = new Carte((String)(cJson.getJSONObject(i).get("nom")),(String)(cJson.getJSONObject(i).get("type")),(Integer)(cJson.getJSONObject(i).get("pointsVictoire")));
						}
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						m.add(c);
				    }
					connexion.emit("readyCheck", "Prêt");
				}
				
			});
			//méthode pour cartes
			connexion.on("Ton tour",new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					Carte c = null;
					c = m.get(0);
					m.remove(0);
					JSONObject carteJouéeJSON = new JSONObject(c);
					connexion.emit("Carte Jouée", carteJouéeJSON);
				}
			});
			//fin méthode cartes
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demarrer() {
		System.out.println("Tentative de connexion");
		connexion.connect();

		synchronized(attenteDeconnexion) {
			try {
				attenteDeconnexion.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void log(String s) {
		System.out.println("Client : " + s);
	}
	
	public static void main(String[] args) {

        try {
            System.setOut(new MyPrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Client client = new Client("http://127.0.0.1:10101");
        client.demarrer();
	}
}
