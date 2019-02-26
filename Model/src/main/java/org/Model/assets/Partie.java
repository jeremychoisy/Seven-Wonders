package org.Model.assets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.Model.tools.CouleurSorties;
import org.Model.tools.GestionEffets;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.gson.Gson;

public class Partie {
	private ArrayList<Joueur> listeJoueurs;
	
	// variables constantes de configuration d'une partie
	private final int NB_JOUEURS = 4;
	private final int NB_CARTES = 29;
	private final int NB_MERVEILLES = 3;
	private final int POINTS_TO_SCORE = 10;
	
	// variables nécessaires au chargements des ressources
	private Carte[] c; 
	private Merveille[] m;
	
	// variables utiles pour un tour de jeu
	private int tourCourant;
	private int nbCartesJouées;

	
	public Partie() {
		listeJoueurs = new ArrayList<Joueur>();
		c = new Carte[NB_CARTES];
		m = new Merveille[NB_MERVEILLES];
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
		tourCourant = 0;
	}
	
	public void ajouterJoueur(String nom, SocketIOClient socket) {
		Joueur j = new Joueur(nom,socket);
		log("le joueur : " + socket.getRemoteAddress() + " s'est identifié en tant que : " + nom + ".");
		listeJoueurs.add(j);
		// Si le nombre de joueurs correspond au nombre de joueurs attendu
		if(listeJoueurs.size() == NB_JOUEURS) {
			log("le lobby est complet, préparation de la partie en cours...");
			// Initialisation de la partie : distribution des ressources aux joueurs
			initPartie();
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
			listeJoueurs.get(i).setPièces(3);
			// Envoi de la main au joueur 
			log("la main est distribuée à " + listeJoueurs.get(i).getNom() + ".");
			listeJoueurs.get(i).getSocket().sendEvent("main", listeMain);
			// Envoi des 3 PO au joueur
			listeJoueurs.get(i).getSocket().sendEvent("pièces", 3);
			
		}
	}
	
	// Fonction qui traite le readycheck d'un joueur, démarre le tour si tout le monde est prêt
	public void setRdy(SocketIOClient socket) {
		int index = getIndexFromSocket(socket);
		listeJoueurs.get(index).setRdy(true);
		log(listeJoueurs.get(index).getNom() + " est prêt !");

		if(everyoneIsRdy()) {
			log("Tous les joueurs sont prêts.");
			demarrerTourSuivant();
		}
	}
	
	// Fonction qui détermine si le joueur correspondant au socket a gagné la partie.
	public boolean estGagnant(SocketIOClient socket) {
		int index = getIndexFromSocket(socket);
		if(listeJoueurs.get(index).getPoints_victoire() >= POINTS_TO_SCORE) {
			return true;
		}
		return false;
	}
	
	// Fonction qui retourne l'état de la partie (en cours ou finie).
	public boolean estFinie() {
		if(tourCourant == 6) {
			return true;
		}
		return false;
	}
	
	// Fonction qui afficher les résultats quand la partie est finie.
	public void afficherResultats(SocketIOClient socket) {
		int index = getIndexFromSocket(socket);
		if(estGagnant(socket)) {
			log("Victoire de " + listeJoueurs.get(index).getNom() + "!");
		}
		else
		{
			if(tourEstFini()) {
				afficherGagnant();
			}
				
		}
	}
	
	// Fonction qui traite la carte joué par un joueur
	public void jouerCarte(SocketIOClient socket, Carte c) {
		int index = getIndexFromSocket(socket);
		String name = listeJoueurs.get(index).getNom();
		//Effet e = GestionEffets.FabriquerEffet(c, c.getNomEffet());
		GestionEffets.appliquerEffetCarte(c, listeJoueurs.get(index));
		log(name + " a joué " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s).");
		nbCartesJouées += 1;
	}
	
	// Fonction qui permet de passer au tour suivant 
	public void demarrerTourSuivant() {
		nbCartesJouées = 0;
		tourCourant += 1;
		log("Début du tour " + tourCourant + " !");
		for(int i = 0; i < listeJoueurs.size();i++) {
			listeJoueurs.get(i).getSocket().sendEvent("Ton tour");
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
	// Fonction qui retourne l'état du tour (en cours ou fini)
	public boolean tourEstFini() {
		if(nbCartesJouées == NB_JOUEURS) {
			return true;
		}
		return false;
	}
	
	// Fonction qui détermine et afficher le gagnant de la partie
	public void afficherGagnant() {
		int indexMax = 0;
		int max = listeJoueurs.get(0).getPoints_victoire();
		for(int i=1;i < listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getPoints_victoire() > max) {
				indexMax = i;
				max = listeJoueurs.get(i).getPoints_victoire();
			}
		}
		log("Victoire de " + listeJoueurs.get(indexMax).getNom() + " avec " + max + " points.");
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
	
	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_GREEN + "[Annonce Partie] " + s + CouleurSorties.ANSI_RESET);
	}	
}
