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
	private final int NB_CARTES = 98;
	private final int NB_MERVEILLES = 4;
	
	// variables nécessaires au chargements des ressources
	private Carte[] c; 
	private Merveille[] m;
	private ArrayList<Carte> cartesAgeI;
	private ArrayList<Carte> cartesAgeII;
	private ArrayList<Carte> cartesAgeIII;
	
	// variables utiles pour le déroulement du jeu
	private ArrayList<Carte> défausse;
	private int tourCourant;
	private int ageCourant;
	private int nbCartesJouées;
	private boolean isEveryoneReadyStated = false;

	
	public Partie() {
		listeJoueurs = new ArrayList<Joueur>();
		c = new Carte[NB_CARTES];
		m = new Merveille[NB_MERVEILLES];
		cartesAgeI = new ArrayList<Carte>();
		cartesAgeII = new ArrayList<Carte>();
		cartesAgeIII = new ArrayList<Carte>();
		défausse = new ArrayList<Carte>();
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
		tourCourant = 0;
		ageCourant = 1;
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
		Main main;
		Merveille merveille;
		
		// Construction d'une liste avec toutes les cartes
		for(int i=0;i<NB_CARTES;i++) {
			if(c[i].getConfigurationNumber() <= NB_JOUEURS) {
				switch(c[i].getAge()) {
					case 1:
						cartesAgeI.add(c[i]);
						break;
					case 2:
						cartesAgeII.add(c[i]);
						break;
					case 3:
						cartesAgeIII.add(c[i]);
						break;
				}
			}
		}
		
		//Construction d'une liste avec toutes les merveilles
		ArrayList<Merveille> listeMerveilles = new ArrayList<Merveille>();
		for(int i=0;i<NB_MERVEILLES;i++) {
			listeMerveilles.add(m[i]);
		}
		
		// Mélange des listes
		Collections.shuffle(cartesAgeI);
		Collections.shuffle(cartesAgeII);
		log("" + cartesAgeII.size());
		Collections.shuffle(cartesAgeIII);
		Collections.shuffle(listeMerveilles);
		
		// Pour chaque joueur
		for(int i=0;i<listeJoueurs.size();i++) {
			main = new Main();
			// Construction de la main
			for(int j=0;j<7;j++) {
				main.add(cartesAgeI.get(0));
				cartesAgeI.remove(0);
			}
			// Pioche de la merveille
			merveille = listeMerveilles.get(0);
			listeMerveilles.remove(0);
			// Mise à jour du joueur côté serveur
			listeJoueurs.get(i).setM(main);
			listeJoueurs.get(i).setMerveille(merveille);
			listeJoueurs.get(i).setPièces(3);
			
			// Envoi de la main au joueur 
			listeJoueurs.get(i).getSocket().sendEvent("Main", main.getMain());
			// Envoi de la merveille au joueur
			listeJoueurs.get(i).getSocket().sendEvent("Merveille", merveille);
			// Envoi des 3 PO au joueur
			listeJoueurs.get(i).getSocket().sendEvent("Pièces", 3);
			
			log("Une main, une merveille ainsi que 3 pièces sont distribuées à " + listeJoueurs.get(i).getNom() + ".");
			
		}
	}
	 
	public boolean estFinie() { //méthode qui définit si la partie est terminée (pour l'âge 2 actuellement)
		return (getAgeCourant()==2 && getTourCourant()==7);
	}
	
	public int getTourCourant() {
		return tourCourant;
	}

	public void setTourCourant(int tourCourant) {
		this.tourCourant = tourCourant;
	}

	// Fonction qui traite le readycheck d'un joueur, démarre le tour si tout le monde est prêt
	public void setRdy(SocketIOClient socket) {
		int index = getIndexFromSocket(socket);
		listeJoueurs.get(index).setRdy(true);
		log(listeJoueurs.get(index).getNom() + " est prêt !");
		if(everyoneIsRdy() && isEveryoneReadyStated == false) {
			isEveryoneReadyStated = true;
			log("Tous les joueurs sont prêts.");
			demarrerTourSuivant();
		}
	}
		
	// Fonction qui retourne l'état de l'Age (en cours ou finie).
	public boolean AgeEstFini() {
			if(tourCourant == 7) {
				return true;
			}
		return false;
	}
	
	public int getAgeCourant() {
		return ageCourant;
	}
	// Fonction qui traite la carte joué par un joueur.
	public void jouerCarte(SocketIOClient socket, Carte c) {
		int index = getIndexFromSocket(socket);
		String name = listeJoueurs.get(index).getNom();
		GestionEffets.appliquerEffet(c.getEffet(), listeJoueurs.get(index));
		// MaJ du joueur côté serveur
		if(c.getCout().get("pièces") != null) {
			listeJoueurs.get(index).setPièces(listeJoueurs.get(index).getPièces() - c.getCout().get("pièces"));
		}
		listeJoueurs.get(index).getM().RemoveCardFromName(c.getNom());
		log(name + " a joué " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s).");
		nbCartesJouées += 1;
	}
	
	// Fonction qui traite la carte défaussée par un joueur.
	public void défausserCarte(SocketIOClient socket,Carte c) {
		int index = getIndexFromSocket(socket);
		String name = listeJoueurs.get(index).getNom();
		listeJoueurs.get(index).getM().RemoveCardFromName(c.getNom());
		défausse.add(c);
		nbCartesJouées += 1;
		if(AgeEstFini()) {
				log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) (fin de l'âge).");
		} 
		else
		{
			listeJoueurs.get(index).getSocket().sendEvent("Pièces", 3);
			listeJoueurs.get(index).setPièces(listeJoueurs.get(index).getPièces() + 3);
			log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s)." + listeJoueurs.get(index).getM().toString());
		}
	}
	
	public void changerAge() {
		if(ageCourant<2) {
			ageCourant ++;
			tourCourant = 0;
			log("Début de l'âge " + ageCourant + " !");
			


			Main main;
			for(int i=0;i<listeJoueurs.size();i++) {
				main = new Main();
				// Construction de la main
				for(int j=0;j<7;j++) {
					main.add(cartesAgeII.get(0));
					cartesAgeII.remove(0);
				}
			
				// Mise à jour du joueur côté serveur
				listeJoueurs.get(i).setM(main);
			
				
				// Envoi de la main au joueur 
				listeJoueurs.get(i).getSocket().sendEvent("Main", main.getMain());
			
				log("Une nouvelle main (age 2) est distribuée à " + listeJoueurs.get(i).getNom() + "." + main.toString());
				
			}
			demarrerTourSuivant();
		}
	
	}
	
	
	// Fonction qui permet de passer au tour suivant.
	public void demarrerTourSuivant() {
		nbCartesJouées = 0;
		tourCourant += 1;
		log("Début du tour " + tourCourant + " !");
		for(int i = 0; i < listeJoueurs.size();i++) {
			listeJoueurs.get(i).getSocket().sendEvent("Ton tour");
		}
	}
	
	// Fonction qui permet de passer au tour suivant.
	public void demarrerDernierTour() {
		nbCartesJouées = 0;
		tourCourant += 1;
		log("Début du dernier Tour (défausse) !");
		for(int i = 0; i < listeJoueurs.size();i++) {
			listeJoueurs.get(i).getSocket().sendEvent("Ton dernier tour");
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
		int max = listeJoueurs.get(0).getPoints_victoire() + listeJoueurs.get(0).getPièces();
		for(int i=1;i < listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getPoints_victoire() + listeJoueurs.get(i).getPièces() > max) {
				indexMax = i;
				max = listeJoueurs.get(i).getPoints_victoire() + listeJoueurs.get(i).getPièces();
			}
		}
		log("Victoire de " + listeJoueurs.get(indexMax).getNom() + " avec " + max + " points de civilisation.");
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
