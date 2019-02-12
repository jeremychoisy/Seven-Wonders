package org.Client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.Model.Carte;
import org.Model.Id;
import org.Model.MyPrintStream;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

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
			connexion.on("Carte",new Emitter.Listener() {

				@Override
				public void call(Object... args) {
					Gson gson = new Gson();
					FileWriter writer;
					FileReader reader;
					try {
						File f = new File("/home/jeremy/eclipse-workspace/seven-wonders/file.json");
						System.out.println(f.exists());
						writer = new FileWriter("/home/jeremy/eclipse-workspace/seven-wonders/file.json");
						reader = new FileReader("/home/jeremy/eclipse-workspace/seven-wonders/file.json");
						Carte[] c = new Carte[] {new Carte("marché","rouge"),new Carte("Tour","marron")};
						gson.toJson(c,writer);
						writer.flush();
						writer.close();
						Carte[] c1 = gson.fromJson(reader, Carte[].class);
						System.out.println("CARTE 1 : " + c1[0].getNom());
						System.out.println("CARTE 2 : " + c1[1].getNom());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					JSONObject cJson = (JSONObject) args[0];
					try {
						System.out.println("Client : la carte reçue par " + id.getNom() + " est : " + cJson.get("nom"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
