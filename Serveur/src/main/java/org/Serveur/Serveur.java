package org.Serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.Model.Carte;
import org.Model.Id;
import org.Model.Joueur;
import org.Model.Main;
import org.Model.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Serveur {
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	private ArrayList<Joueur> listeJoueur;
	private final int nbJoueur = 1;
	private final int nbCartes = 3;
	private int idJoueur = 1;
	private Carte[] c; 

	public Serveur(Configuration config){
		test();
		serveur = new SocketIOServer(config);
		listeJoueur = new ArrayList<Joueur>();
		c = new Carte[nbCartes];
		recupererDonnees();
		System.out.println("Serveur : Attente de connexion du joueur...");
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				System.out.println("Serveur : Connexion de " + socketIOClient.getRemoteAddress());
				System.out.println("Attente de l'authentification de : " + socketIOClient.getRemoteAddress());
			}	
		});
		
		serveur.addEventListener("id", Id.class, new DataListener<Id>(){

			@Override
			public void onData(SocketIOClient client, Id data, AckRequest ackSender) throws Exception {
				Joueur j = new Joueur(data.getNom(),idJoueur,client);
				System.out.println("Serveur : le joueur : " + client.getRemoteAddress() + " s'est identifié en tant que : " + data.getNom() + ".");
				listeJoueur.add(j);
				if(listeJoueur.size() == nbJoueur) {
					System.out.println("Serveur : le lobby est complet, préparation de la partie en cours...");
					initPartie();
				}

			}
			
		});
		
		serveur.addEventListener("reponse", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				synchronized(attenteConnexion) {
					attenteConnexion.notify();
				}
				
			}
			
		});
		
	}
	public void test() {
		Gson gson = new Gson();
		FileWriter writer = null;
		try {
			writer = new FileWriter("/home/jeremy/eclipse-workspace/seven-wonders/carte.json");
			Carte[] c = new Carte[nbCartes];
			c[0] = new Carte("Marché","vert");
			c[1] = new Carte("Tour","marron");
			c[2] = new Carte("Champs","vert");
			gson.toJson(c, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public void recupererDonnees() {
		FileReader reader = null;
		Gson gson = new Gson();
		try {
			reader = new FileReader("/home/jeremy/eclipse-workspace/seven-wonders/carte.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur : Chargement des données...");
		c = gson.fromJson(reader, Carte[].class);

	}
	public void initPartie() {
		for(int i=0;i<listeJoueur.size();i++) {
			// Opération de préparation de la partie
			Main m = new Main();
			m.add(c[0]);
			m.add(c[1]);
			m.add(c[2]);
			listeJoueur.get(i).getSocket().sendEvent("main", m);
		}
	}
	
	public void demarrer() {
		serveur.start();
		
		synchronized(attenteConnexion) {
			try {
				attenteConnexion.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Serveur : fin de la connexion");
		serveur.stop();
	}
	
	public static void main(String[] args) {

        try {
            System.setOut(new MyPrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);
        
        Serveur serveur = new Serveur(config);
        serveur.demarrer();
	}
	
}
