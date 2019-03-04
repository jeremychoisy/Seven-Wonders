package org.Client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.tools.CouleurSorties;
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

	
	public Client(String url, final String name) {
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
					
					connexion.emit("ReadyCheck", "Prêt");
				}
				
			});
			
			// traitement de l'événement "voici tes pièces" venant du serveur
			connexion.on("Pièces", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					int valeur = (Integer) args[0];
					
					b.addPièces(valeur);
					
				}
			});
			
			// traitement de l'événement "voici ta merveille" venant du serveur
			connexion.on("Merveille", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {

					JSONObject Json = (JSONObject)args[0];
					
					b.setMerveille(Json);	
				}
			});
			
			// Traitement de l'événement "c'est ton tour de jouer" venant du serveur
			connexion.on("Ton tour", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					
					b.jouerTour(connexion);
				}
			});
			
			// Traitement de l'événement "c'est ton tour de jouer" venant du serveur
			connexion.on("Ton dernier tour", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					
					b.defausserDerniereCarte(connexion);
				}
			});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void emit(Socket s, String event, Object...args) {
		s.emit(event, args);
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
