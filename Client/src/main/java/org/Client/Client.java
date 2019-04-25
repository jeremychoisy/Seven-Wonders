package org.Client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.tools.CouleurSorties;
import org.Model.tools.GestionPersistance;
import org.Model.tools.MyPrintStream;
import org.json.JSONArray;
import org.json.JSONObject;

import bot.Bot;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class Client{
	private Socket connexion;
	private Bot b;
	private String name;

	
	public Client(String url, final String name) {
		this.name = name;
		this.b = new Bot(name, this);
		try {
			IO.Options opts = new IO.Options();
			opts.forceNew = true;
			connexion = IO.socket(url, opts);
			
			connexion.on("connect",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					log("connecté");
					connexion.emit("id", name);
					
				}
				
			});

			connexion.on("Reset",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					reset();
					connexion.emit("Reset","Done");
				}

			});
			
			connexion.on("disconnect", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					log("deconnecté");
					connexion.disconnect();
					connexion.close();
				}
			});
			// Traitement de l'événement "voici ta main" venant du serveur
			connexion.on("Main",new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					JSONArray cJson = (JSONArray) args[0];
				    
					b.setMain(cJson);

				}
				
			});
			
		/*	connexion.on("Change main", new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					JSONArray cJson = (JSONArray) args[0];
					
					b.setMain(cJson);
					
				
				}
				
			});*/
			// traitement de l'événement "voici tes pièces" venant du serveur
			connexion.on("Pièces", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					int valeur = (Integer) args[0];
					
					b.getJ().addPièces(valeur);
					
				}
			});
			
			// traitement de l'événement "voici ta merveille" venant du serveur
			connexion.on("Merveille", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {

					JSONObject Json = (JSONObject)args[0];
					
					b.setMerveille(Json);
					connexion.emit("ReadyCheck", "Prêt");
				}
			});

			// Traitement de l'événement "c'est ton tour de jouer" venant du serveur
			connexion.on("Ton tour", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					JSONArray jArray = (JSONArray) args[1];
					b.setMain(jArray);
					JSONObject j = (JSONObject) args[0];
					b.jouerTour(GestionPersistance.JSONToMapRessource(j));
					
				}
			});
			
			// Traitement de l'événement "c'est ton tour de jouer" venant du serveur
			connexion.on("Ton dernier tour", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					
					b.defausserDerniereCarte();
				}
			});

			connexion.on("Commerce status", new Emitter.Listener() {
				@Override
				public void call(Object... args) {

					b.defausserDerniereCarte();
				}
			});

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void emit(String event, Object...args) {
		connexion.emit(event, args);
	}

	public void reset(){
		b = new Bot(name,this);
	}
	
	public void demarrer() {
		log("Tentative de connexion");
		connexion.connect();
	}
	
	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_BLUE + "Client [" + b.getJ().getNom() + "] : " + s + CouleurSorties.ANSI_RESET);
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
