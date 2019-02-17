package org.Serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.Model.assets.Carte;
import org.Model.assets.Id;
import org.Model.assets.Joueur;
import org.Model.assets.Main;
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
	// variables client/serveur
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	private ArrayList<Joueur> listeJoueurs;
	
	// variables constantes de configuration d'une partie
	private final int NB_JOUEURS = 3;
	private final int NB_CARTES = 34;
	private final int NB_MERVEILLES = 3;
	
	// variables nécessaires au chargements des ressources
	private Carte[] c; 
	private Merveille[] m;
	
	// variables utiles pour un tour de jeu
	private int tour;
	private int nbCartesJouées;

	public Serveur(Configuration config){
		// Initialisation
		serveur = new SocketIOServer(config);
		listeJoueurs = new ArrayList<Joueur>();
		c = new Carte[NB_CARTES];
		m = new Merveille[NB_MERVEILLES];
		tour = 0;
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
		log("Attente de connexion des joueur...");
		// Ajout de l'écouteur gérant la connexion d'un client
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				log("Connexion de " + socketIOClient.getRemoteAddress());
				log("Attente de l'authentification de : " + socketIOClient.getRemoteAddress());
			}	
		});
		// Ajout de l'écouteur traitant le message d'identification du client
		serveur.addEventListener("id", Id.class, new DataListener<Id>(){

			@Override
			public void onData(SocketIOClient client, Id data, AckRequest ackSender) throws Exception {
				// On crée un joueur avec les caractéristiques du client connecté et on l'ajoute à la liste des joueurs
				Joueur j = new Joueur(data.getNom(),client);
				log("le joueur : " + client.getRemoteAddress() + " s'est identifié en tant que : " + data.getNom() + ".");
				listeJoueurs.add(j);
				// Si le nombre de joueurs correspond au nombre de joueurs attendu
				if(listeJoueurs.size() == NB_JOUEURS) {
					log("le lobby est complet, préparation de la partie en cours...");
					// Initialisation de la partie : distribution des ressources aux joueurs
					initPartie();
				}

			}
			
		});
	
		serveur.addEventListener("readyCheck", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Prêt")) {
						int index = getIndexFromSocket(client);
						listeJoueurs.get(index).setRdy(true);
						log(listeJoueurs.get(index).getNom() + " est prêt !");

						if(everyoneIsRdy()) {
							log("Tous les joueurs sont prêts.");
							demarrerTourSuivant();
						}					
				}
			}
			
		});
		
		serveur.addEventListener("Carte Jouée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				int index = getIndexFromSocket(client);
				String name = listeJoueurs.get(index).getNom();
				listeJoueurs.get(index).setScore(listeJoueurs.get(index).getScore() + data.getPointsVictoire());
				log(name + " a joué " + data.getNom() + " [gain de " + data.getPointsVictoire() + " (score actuel : " + listeJoueurs.get(index).getScore()  +")].");
				if(listeJoueurs.get(index).getScore() >= 10 || tour == 6) {
					if(listeJoueurs.get(index).getScore() >= 10) {
						log("Victoire de " + name + "!");
					}
					else
					{
						nbCartesJouées += 1;
						if(nbCartesJouées == NB_JOUEURS) {
							displayWinner();
						}
							
					}
					synchronized(attenteConnexion) {
						attenteConnexion.notify();
					}
				}
				else
				{
					nbCartesJouées += 1;
					if(nbCartesJouées == NB_JOUEURS)
						demarrerTourSuivant();
				}
				
			}
			
		});
		
	}
	
	public void displayWinner() {
		int indexMax = 0;
		int max = listeJoueurs.get(0).getScore();
		for(int i=1;i < listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getScore() > max) {
				indexMax = i;
				max = listeJoueurs.get(i).getScore();
			}
		}
		log("Victoire de " + listeJoueurs.get(indexMax).getNom() + " avec " + max + " points.");
	}
	
	public void demarrerTourSuivant() {
		nbCartesJouées = 0;
		tour += 1;
		log("Début du tour " + tour + " !");
		for(int i = 0; i < listeJoueurs.size();i++) {
			listeJoueurs.get(i).getSocket().sendEvent("Ton tour");
		}

	}
	// Fonction responsable de la récupération des données depuis les fichiers JSON
	public void recupererDonnees() {
		FileReader reader = null;
		Gson gson = new Gson();
		
		// Stockage des cartes du jeu dans un tableau depuis le fichier JSON correspondant
		try {
			reader = new FileReader("./../Assets/cartes.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		log("Chargement des cartes...");
		c = gson.fromJson(reader, Carte[].class);
		try {
			reader.close();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		// Stockage des merveilles du jeu dans un tableau depuis le fichier JSON correspondant
		try {
			reader = new FileReader("./../Assets/merveilles.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		log("Chargement des merveilles...");
		m = gson.fromJson(reader, Merveille[].class);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	//Fonction responsable de de la distribution des ressources aux joueurs
	public void initPartie() {
		ArrayList<Carte> listeMain;
		Main main;
		// Construction d'une liste avec toutes les cartes
		ArrayList<Carte> listeCartes = new ArrayList<Carte>();
		for(int i=0;i<NB_CARTES;i++) {
			listeCartes.add(c[i]);
		}
		Collections.shuffle(listeCartes);
		
		// Pour chaque joueur
		for(int i=0;i<listeJoueurs.size();i++) {
			listeMain = new ArrayList<Carte>();
			main = new Main();
			// Construction de la main
			for(int j=0;j<7;j++) {
				listeMain.add(listeCartes.get(0));
				main.add(listeCartes.get(0));
				listeCartes.remove(0);
			}
			listeJoueurs.get(i).setM(main);
			// Envoi de la main au joueur 
			log("la main est distribuée à " + listeJoueurs.get(i).getNom() + ".");
			listeJoueurs.get(i).getSocket().sendEvent("main", listeMain);
		}
	}
	
	// Fonction qui vérifie si tous les joueurs sont prêts
	public boolean everyoneIsRdy() {
		boolean ret = true;
		for(int i =0;i<listeJoueurs.size();i++) {
			if(!listeJoueurs.get(i).isRdy()) {
				ret = false;
				break;
			}
		}
		return ret;
	}
	
	// Fonction qui récupère le nom du joueur à partir de son socket
	public int getIndexFromSocket(SocketIOClient socket) {
		for(int i =0;i<listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getSocket().equals(socket)) {
				return i;
			}
		}
		return -1;
	}
	
	public void log(String s) {
		System.out.println("Serveur : " + s);
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
