package org.partie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Main;
import org.Model.assets.Merveille;
import org.Model.tools.CouleurSorties;
import org.Model.tools.GestionEffets;
import org.Serveur.Serveur;

import com.corundumstudio.socketio.SocketIOClient;

import static org.Model.tools.GestionPersistance.generateData;
import static org.Model.tools.GestionPersistance.isData;

public class Partie {
	private boolean displayLogs;
	private ArrayList<Joueur> listeJoueurs;
	// variable serveur
	private Serveur s;
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
	private ArrayList<Merveille> Merveilles;
	
	// variables utiles pour le déroulement du jeu
	private ArrayList<Carte> défausse;
	private int tourCourant;
	private int ageCourant;
	private int nbCartesJouées;
	private boolean isEveryoneReadyStated = false;

	public Partie() {}
	
	public Partie(Serveur s, boolean displayLogs) {
		this.s = s;
		this.displayLogs = displayLogs;

		listeJoueurs = new ArrayList<Joueur>();
		c = new Carte[NB_CARTES];
		m = new Merveille[NB_MERVEILLES];
		cartesAgeI = new ArrayList<Carte>();
		cartesAgeII = new ArrayList<Carte>();
		cartesAgeIII = new ArrayList<Carte>();
		défausse = new ArrayList<Carte>();
		tourCourant = 0;
		ageCourant = 1;
		if(!isData()){
			generateData();
		}
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
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

		construireListes();
		
		// Pour chaque joueur
		for(int i=0;i<listeJoueurs.size();i++) {
			main = new Main();
			// Construction de la main
			for(int j=0;j<7;j++) {
				main.add(cartesAgeI.get(0));
				cartesAgeI.remove(0);
			}
			// Pioche de la merveille
			merveille = Merveilles.get(0);
			Merveilles.remove(0);
			// Mise à jour du joueur côté serveur
			listeJoueurs.get(i).setM(main);
			listeJoueurs.get(i).setMerveille(merveille);
			listeJoueurs.get(i).setPièces(3);
			
			// Envoi de la main au joueur 
			s.sendEvent(listeJoueurs.get(i).getSocket(),"Main", main.getMain());
			// Envoi de la merveille au joueur
			s.sendEvent(listeJoueurs.get(i).getSocket(),"Merveille", merveille);
			// Envoi des 3 PO au joueur
			s.sendEvent(listeJoueurs.get(i).getSocket(),"Pièces", 3);
			
			log("Une main, une merveille ainsi que 3 pièces sont distribuées à " + listeJoueurs.get(i).getNom() + ".");
			
		}
	}
	
	public void construireListes() {
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
		Merveilles = new ArrayList<Merveille>();
		for(int i=0;i<NB_MERVEILLES;i++) {
			Merveilles.add(m[i]);
		}
		
		// Mélange des listes
		Collections.shuffle(cartesAgeI);
		Collections.shuffle(cartesAgeII);
		Collections.shuffle(cartesAgeIII);
		Collections.shuffle(Merveilles);
	}
	
	// Fonction qui définit si la partie est terminée (pour l'âge 2 actuellement)
	public boolean estFinie() {
		return (getAgeCourant()==2 && getTourCourant()==7);
	}
	
	// Fonction qui traite le readycheck d'un joueur, démarre le tour si tout le monde est prêt
	public void setRdy(SocketIOClient socket) {
		int index = getIndexFromSocket(socket);
		listeJoueurs.get(index).setRdy(true);
		log(listeJoueurs.get(index).getNom() + " est prêt !");
		if(isEveryoneRdy() && isEveryoneReadyStated == false) {
			isEveryoneReadyStated = true;
			log("Tous les joueurs sont prêts.");
			demarrerTourSuivant();
		}
	}

	// Fonction qui retourne l'état de l'Age (en cours ou finie).
	public boolean ageEstFini() {
			if(tourCourant == 7) {
				return true;
			}
		return false;
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
		log(name + " a joué " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s)" + listeJoueurs.get(0).getPoint_militaires() + "points militaires.");
		nbCartesJouées += 1;
		goNext();
	}
	
	// Fonction qui traite la carte défaussée par un joueur.
	public void défausserCarte(SocketIOClient socket,Carte c) {
		int index = getIndexFromSocket(socket);
		String name = listeJoueurs.get(index).getNom();
		listeJoueurs.get(index).getM().RemoveCardFromName(c.getNom());
		défausse.add(c);
		nbCartesJouées += 1;
		if(ageEstFini()) {
				log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s)" + listeJoueurs.get(0).getPoint_militaires() + "points militaires (fin de l'âge).");
		} 
		else
		{
			s.sendEvent(listeJoueurs.get(index).getSocket(),"Pièces", 3);
			listeJoueurs.get(index).setPièces(listeJoueurs.get(index).getPièces() + 3);
			log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPoints_victoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) "  + listeJoueurs.get(0).getPoint_militaires() + "points militaires." + listeJoueurs.get(index).getM().toString());
		}
		goNext();
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
				s.sendEvent(listeJoueurs.get(i).getSocket(),"Main", main.getMain());
			
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
		s.broadcast("Ton tour");
	}
	
	// Fonction qui permet de passer au tour suivant.
	public void demarrerDernierTour() {
		nbCartesJouées = 0;
		tourCourant += 1;
		log("Début du dernier Tour (défausse) !");
		s.broadcast("Ton dernier tour");
	}
	
	// Fonction qui vérifie si tous les joueurs sont prêts
	public boolean isEveryoneRdy() {
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
	
	public void goNext() {
		if(tourEstFini()) {
				if(estFinie()) { 
					int index = getIndexGagnant();
					Joueur JoueurGagnant = listeJoueurs.get(index);
					int score = JoueurGagnant.getPoints_victoire() + JoueurGagnant.getPièces() + JoueurGagnant.getPoint_militaires();
					log("Victoire de " + JoueurGagnant.getNom() + " avec " + score + " points de civilisation.");;
					s.stop();
				}
				else if(ageEstFini())
				{
					changerAge();

				}
				else if(getTourCourant() == 6){ 
					demarrerDernierTour();
				}
				else
				{
					if(tourEstFini())
						demarrerTourSuivant();
				}
		}

	}
	
	// Fonction qui détermine et afficher le gagnant de la partie
	public int getIndexGagnant() {
		int indexMax = 0;
		int max = listeJoueurs.get(0).getPoints_victoire() + listeJoueurs.get(0).getPièces() + listeJoueurs.get(0).getPoint_militaires();
		for(int i=1;i < listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getPoints_victoire() + listeJoueurs.get(i).getPièces() + listeJoueurs.get(0).getPoint_militaires()> max) {
				indexMax = i;
				max = listeJoueurs.get(i).getPoints_victoire() + listeJoueurs.get(i).getPièces() + + listeJoueurs.get(0).getPoint_militaires();
			}
		}
		return indexMax;

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
		ObjectMapper mapper = new ObjectMapper();

		// Stockage des cartes du jeu dans un tableau depuis le fichier JSON correspondant
		log("Chargement des cartes...");

		try {
			c  = mapper.readValue(new File("./../Assets/cartes.json"), Carte[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Stockage des merveilles du jeu dans un tableau depuis le fichier JSON correspondant
		log("Chargement des merveilles...");
		try {
			m = mapper.readValue(new File("./../Assets/merveilles.json"), Merveille[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setDisplayLogs(boolean b){
		displayLogs = b;
	}
	// Formatage des sorties textes
	public void log(String s) {
		if(displayLogs) {
			System.out.println(CouleurSorties.ANSI_GREEN + "[Annonce Partie] " + s + CouleurSorties.ANSI_RESET);
		}
	}
	
	// Getters & Setters 
	
	public Carte[] getC() {
		return c;
	}

	public void setC(Carte[] c) {
		this.c = c;
	}

	public ArrayList<Carte> getCartesAgeI() {
		return cartesAgeI;
	}

	public void setCartesAgeI(ArrayList<Carte> cartesAgeI) {
		this.cartesAgeI = cartesAgeI;
	}

	public ArrayList<Carte> getCartesAgeII() {
		return cartesAgeII;
	}

	public void setCartesAgeII(ArrayList<Carte> cartesAgeII) {
		this.cartesAgeII = cartesAgeII;
	}

	public ArrayList<Carte> getCartesAgeIII() {
		return cartesAgeIII;
	}

	public void setCartesAgeIII(ArrayList<Carte> cartesAgeIII) {
		this.cartesAgeIII = cartesAgeIII;
	}

	public ArrayList<Carte> getDéfausse() {
		return défausse;
	}

	public void setDéfausse(ArrayList<Carte> défausse) {
		this.défausse = défausse;
	}

	public void setAgeCourant(int ageCourant) {
		this.ageCourant = ageCourant;
	}
	
	public ArrayList<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}

	public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
		this.listeJoueurs = listeJoueurs;
	}
	public ArrayList<Merveille> getMerveilles() {
		return Merveilles;
	}

	public void setMerveilles(ArrayList<Merveille> merveilles) {
		Merveilles = merveilles;
	}
	
	public int getAgeCourant() {
		return ageCourant;
	}
	public int getTourCourant() {
		return tourCourant;
	}

	public void setTourCourant(int tourCourant) {
		this.tourCourant = tourCourant;
	}

	public Merveille[] getM() {
		return m;
	}

	public void setM(Merveille[] m) {
		this.m = m;
	}

	public int getNbCartesJouées() {
		return nbCartesJouées;
	}

	public void setNbCartesJouées(int nbCartesJouées) {
		this.nbCartesJouées = nbCartesJouées;
	}

	public boolean isEveryoneReadyStated() {
		return isEveryoneReadyStated;
	}

	public void setEveryoneReadyStated(boolean isEveryoneReadyStated) {
		this.isEveryoneReadyStated = isEveryoneReadyStated;
	}

}
