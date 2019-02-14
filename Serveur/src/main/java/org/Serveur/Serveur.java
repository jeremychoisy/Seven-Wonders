package org.Serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import org.Model.assets.Carte;
import org.Model.assets.Id;
import org.Model.assets.Joueur;
import org.Model.assets.Merveille;
import org.Model.tools.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

public class Serveur {
	// Déclaration
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	private ArrayList<Joueur> listeJoueur;
	
	private final int nbJoueur = 1;
	private final int nbCartes = 14;
	private final int nbMerveilles = 3;
	
	
	private Carte[] c; 
	private Merveille[] m;
	
	private int tour;

	public Serveur(Configuration config){
		// Initialisation
		serveur = new SocketIOServer(config);
		listeJoueur = new ArrayList<Joueur>();
		c = new Carte[nbCartes];
		m = new Merveille[nbMerveilles];
		tour = 1;
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
		System.out.println("Serveur : Attente de connexion du joueur...");
		// Ajout de l'écouteur gérant la connexion d'un client
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				System.out.println("Serveur : Connexion de " + socketIOClient.getRemoteAddress());
				System.out.println("Serveur : Attente de l'authentification de : " + socketIOClient.getRemoteAddress());
			}	
		});
		// Ajout de l'écouteur traitant le message d'identification du client
		serveur.addEventListener("id", Id.class, new DataListener<Id>(){

			@Override
			public void onData(SocketIOClient client, Id data, AckRequest ackSender) throws Exception {
				// On crée un joueur avec les caractéristiques du client connecté et on l'ajoute à la liste des joueurs
				Joueur j = new Joueur(data.getNom(),client);
				System.out.println("Serveur : le joueur : " + client.getRemoteAddress() + " s'est identifié en tant que : " + data.getNom() + ".");
				listeJoueur.add(j);
				// Si le nombre de joueurs correspond au nombre de joueurs attendu
				if(listeJoueur.size() == nbJoueur) {
					System.out.println("Serveur : le lobby est complet, préparation de la partie en cours...");
					// Initialisation de la partie : distribution des ressources aux joueurs
					initPartie();
				}

			}
			
		});
	
		serveur.addEventListener("readyCheck", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Prêt")) {
					if(tour == 1) {
						System.out.println("Serveur : Tous les joueurs sont prêts !");
						System.out.println("Serveur : Début de la partie :");
					}
				}
				synchronized(attenteConnexion) {
					attenteConnexion.notify();
				}
				
			}
			
		});
		
	}

	// Fonction responsable de la récupération des données depuis les fichiers JSON
	public void recupererDonnees() {
		FileReader reader = null;
		Gson gson = new Gson();
		
		// Stockage des cartes du jeu dans un tableau depuis le fichier JSON correspondant
		try {
			reader = new FileReader("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur : Chargement des cartes...");
		c = gson.fromJson(reader, Carte[].class);
		try {
			reader.close();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		// Stockage des merveilles du jeu dans un tableau depuis le fichier JSON correspondant
		try {
			reader = new FileReader("/home/jeremy/eclipse-workspace/seven-wonders/Assets/merveilles.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur : Chargement des merveilles...");
		m = gson.fromJson(reader, Merveille[].class);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	//Fonction responsable de de la distribution des ressources aux joueurs
	public void initPartie() {
		ArrayList<Carte> main = new ArrayList<Carte>();
		// Construction d'une liste avec toutes les cartes
		ArrayList<Carte> listeCartes = new ArrayList<Carte>();
		for(int i=0;i<nbCartes;i++) {
			listeCartes.add(c[i]);
		}
		
		// Pour chaque joueur
		for(int i=0;i<listeJoueur.size();i++) {
			// Construction de la main

			for(int j=0;j<7;j++) {
				int rdm = new Random().nextInt((listeCartes.size()));
				main.add(listeCartes.get(rdm));
				listeCartes.remove(rdm);
			}
			
			// Envoi de la main au joueur 
			listeJoueur.get(i).getSocket().sendEvent("main", main);
		}
	}
	
	public void demarrer() {
		serveur.start();
		
		synchronized(attenteConnexion) {
			try {
				attenteConnexion.wait();
			} catch (InterruptedException e) {
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
        SocketConfig s = config.getSocketConfig();
        s.setReuseAddress(true);
        
        
        Serveur serveur = new Serveur(config);
        serveur.demarrer();
	}
	
}
