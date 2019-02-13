package org.Client;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.Id;
import org.Model.MyPrintStream;
import org.json.JSONException;
import org.json.JSONObject;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class Client {
	private Socket connexion;
	private Id id;
	final Object attenteDeconnexion = new Object();
	
	public Client(String url) {
		try {
			connexion = IO.socket(url);
			
			connexion.on("connect",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					System.out.println("Client : connecté");
					id = new Id("WUTED");
					JSONObject idJson = new JSONObject(id);
					connexion.emit("id", idJson);
					
				}
				
			});
			
			connexion.on("disconnect", new Emitter.Listener() {
				
				@Override
				public void call(Object... args) {
					System.out.println("Client : deconnecté");
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
					JSONObject cJson = (JSONObject) args[0];
					System.out.println("Client : la main est reçu par : " + id.getNom());
					connexion.emit("reponse", cJson);
				}
				
			});
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
