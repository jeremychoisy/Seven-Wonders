package org.partie;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Model.assets.*;
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
	private final int NB_JOUEURS = 7;
	private final int NB_CARTES = 148;
	private final int NB_MERVEILLES = 7;
	
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
	private boolean isEveryoneReadyStated;
    private boolean isGameOn ;

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
        isEveryoneReadyStated = false;
        isGameOn = false;
		if(!isData()){
			generateData();
		}
		// Récupération des cartes/merveilles via les fichiers Json
		recupererDonnees();
	}
	
	public void ajouterJoueur(String nom) {
		Joueur j = new Joueur(nom);
		log("Un joueur s'est identifié en tant que : " + nom + ".");
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
			listeJoueurs.get(i).addPièces(3);
			
			// Envoi de la main au joueur 
			s.sendEvent(i,"Main", main.getMain());
			// Envoi de la merveille au joueur
			s.sendEvent(i,"Merveille", merveille);
			// Envoi des 3 PO au joueur
			s.sendEvent(i,"Pièces", 3);
			
			log("Une main, une merveille ainsi que 3 pièces sont distribuées à " + listeJoueurs.get(i).getNom() + ".");
		}
        this.isGameOn = true;
	}
	
	public void construireListes() {
		// Construction d'une liste avec toutes les cartes
		ArrayList listeCarteGuilde  = new ArrayList<Carte>();
		for(int i=0;i<NB_CARTES;i++) {
			if(c[i].getConfigurationNumber() == 0) {
				listeCarteGuilde.add(c[i]);
			}
			else if(c[i].getConfigurationNumber() <= NB_JOUEURS) {
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
		for (int i = 0;i<NB_JOUEURS+2;i++){
			Collections.shuffle(listeCarteGuilde);
			cartesAgeIII.add((Carte) listeCarteGuilde.get(0));
			listeCarteGuilde.remove(0);
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
	
	// Fonction qui définit si la partie est terminée
	public boolean estFinie() {
		return (getAgeCourant()==3 && getTourCourant()==7);
	}
	
	// Fonction qui traite le readycheck d'un joueur, démarre le tour si tout le monde est prêt
	public void setRdy(int index) {
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
	public void jouerCarte(int index, Carte c) {
		String name = listeJoueurs.get(index).getNom();
		boolean isFree = false;
		GestionEffets.appliquerEffet(c.getEffet(), listeJoueurs.get(index));
		// MaJ du joueur côté serveur
		// Si le joueur a déjà posé une carte liée à celle-ci par chaînage => gratuite
		if(c.getEffet().get("chaînage") != null){
			for(int i = 0; i < listeJoueurs.get(index).getCartesPosees().size();i++){
				if(listeJoueurs.get(index).getCartesPosees().get(i).getNom().equals(c.getEffet().get("chainage"))){
					isFree = true;
					break;
				}
			}

		}
		if(!isFree) {
			if (c.getCout().get("pièces") != null) {
				listeJoueurs.get(index).substractPièces(c.getCout().get("pièces"));
			}
		}
		//supprime la carte jouée de la main du joueur
		listeJoueurs.get(index).getM().RemoveCardFromName(c.getNom());
		listeJoueurs.get(index).ajouterCartePosee(c);
		log(name + " a joué " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPointsVictoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) | " + listeJoueurs.get(index).getpointsMilitaires() + " points militaires.)");
		synchronized(this) { setNbCartesJouées(getNbCartesJouées() + 1); }
		goNext();
	}

	public void jouerCarteCommerce(int index, Carte c){
	    String name = listeJoueurs.get(index).getNom();
        for (Map.Entry<String,Integer> entry : c.getCout().entrySet()){
            String key = entry.getKey();
            Integer ressourceCarte = entry.getValue();
            Integer ressourceJoueur = listeJoueurs.get(index).getRessources().get(key);
            // Si le bot n'a pas assez de ressources, on regarde les ressources voisines
            // En partant de l'hypothèse que le bot ne triche pas.
            if (ressourceCarte > ressourceJoueur){
                // Si le voisin de gauche dispose de ce genre de ressource
                if(listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) != 0){
                    // Si le voisin de gauche a assez de ressource pour couvrir le besoin
                    if( listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) >= ressourceCarte - ressourceJoueur){
                        listeJoueurs.get(getIndexVoisinGauche(index)).addPièces((ressourceCarte - ressourceJoueur) * 2);
                        log("" + name + " achète " + (ressourceCarte - ressourceJoueur) + " [" + key + "] à son voisin de gauche pour " + ((ressourceCarte - ressourceJoueur) * 2) + " pièces.");
                        // Sinon, cela signifie que le voisin de droite est en mesure de combler le besoin restant.
                    } else {
                        listeJoueurs.get(getIndexVoisinGauche(index)).addPièces(listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) * 2);
                        log("" + name + " achète " + listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) + " [" + key + "] à son voisin de gauche pour " + (listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) * 2) + " pièces.");
                        listeJoueurs.get(getIndexVoisinDroite(index)).addPièces((ressourceCarte - ressourceJoueur - listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key)) * 2);
                        log("" + name + " achète " + (ressourceCarte - ressourceJoueur - listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key)) + " [" + key + "] à son voisin de droite pour " + (listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().get(key) * 2) + " pièces.");
                    }
                } else {
                    // Sinon, cela signifie que le voisin de droite comble entièrement le besoin
                    listeJoueurs.get(getIndexVoisinDroite(index)).addPièces((ressourceCarte - ressourceJoueur) * 2);
                    log("" + name + " achète " + (ressourceCarte - ressourceJoueur) + " [" + key + "] à son voisin de droite pour " + ((ressourceCarte - ressourceJoueur) * 2) + " pièces.");
                }
                // le bot doit payer le quantité totale de ressources achetées
                listeJoueurs.get(index).substractPièces((ressourceCarte - ressourceJoueur) * 2);
            }
        }
	    jouerCarte(index,c);
    }
	
	// Fonction qui traite la carte défaussée par un joueur.
	public void défausserCarte(int index,Carte c) {
		String name = listeJoueurs.get(index).getNom();
		listeJoueurs.get(index).getM().RemoveCardFromName(c.getNom());
		défausse.add(c);
        synchronized(this) { setNbCartesJouées(getNbCartesJouées() + 1); }
		if(ageEstFini()) {
				log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPointsVictoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) | " + listeJoueurs.get(index).getpointsMilitaires() + " points militaires (fin de l'âge).");
		} 
		else
		{
			s.sendEvent( index,"Pièces", 3);
			listeJoueurs.get(index).addPièces(3);
			log(name + " a défaussé " + c.getNom() + " ( score actuel : " + listeJoueurs.get(index).getPointsVictoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) | "  + listeJoueurs.get(index).getpointsMilitaires() + " points militaires.");
		}
		goNext();
	}

	// Fonction qui traite le cas d'un débloquage d'une étape d'une merveille
	public void débloquerMerveille(int index, Carte carte){
		this.listeJoueurs.get(index).getMerveille().etapeSuivante(this.listeJoueurs.get(index));
		String name = listeJoueurs.get(index).getNom();
		listeJoueurs.get(index).getM().RemoveCardFromName(carte.getNom());
		défausse.add(carte);
        synchronized(this) { setNbCartesJouées(getNbCartesJouées() + 1); }
		log(name + " a défaussé " + carte.getNom() + " pour debloquer une étape de sa merveille  ( score actuel : " + listeJoueurs.get(index).getPointsVictoire()  +" point(s) de victoire | " + listeJoueurs.get(index).getPièces() + " pièce(s) | " + listeJoueurs.get(index).getpointsMilitaires() + " points militaires (fin de l'âge).");
		goNext();
	}

	public void changerAge() {
		if(ageCourant<3) {
			ageCourant ++;
			tourCourant = 0;
			log("Début de l'âge " + ageCourant + " !");
			
			Main main;
			for(int i=0;i<listeJoueurs.size();i++) {
				main = new Main();
				ArrayList<Carte> listeCartes;
				if (ageCourant == 2){
					listeCartes = cartesAgeII;
				}
				else{
					listeCartes = cartesAgeIII;
				}
				// Construction de la main
				for(int j=0;j<7;j++) {
					main.add(listeCartes.get(0));
					listeCartes.remove(0);
				}
			
				// Mise à jour du joueur côté serveur
				listeJoueurs.get(i).setM(main);
			
				
				// Envoi de la main au joueur 
				s.sendEvent(i,"Main", main.getMain());
			
				log("Une nouvelle main (age " + ageCourant + ") est distribuée à " + listeJoueurs.get(i).getNom() + ". " + main.toString());
				
			}
			demarrerTourSuivant();
		}
	
	}
	
	
	// Fonction qui permet de passer au tour suivant.
	public void demarrerTourSuivant() {
		Main buffer = new Main();
		nbCartesJouées = 0;
		tourCourant += 1;
	
		log("Début du tour " + tourCourant + " !");
		
		
		for(int i =0; i < listeJoueurs.size();i++) {
			if(tourCourant>1) {
				if(i==0) {
					buffer=listeJoueurs.get(i).getM();
					listeJoueurs.get(i).setM(listeJoueurs.get(i+1).getM());
				}
				else if (i==listeJoueurs.size()-1){
					listeJoueurs.get(i).setM(buffer);
				}
				else {
					listeJoueurs.get(i).setM(listeJoueurs.get(i+1).getM());
				}
			}

			s.sendEvent(i,"Ton tour", buildRessourcesVoisinsList(i),listeJoueurs.get(i).getM().getMain());
        }
	}

	public Map<String,Integer> buildRessourcesVoisinsList(int index) {
        Map<String, Integer> ressourcesVoisinList = new HashMap<>();
        for (Map.Entry<String, Integer> entry : listeJoueurs.get(getIndexVoisinGauche(index)).getRessources().entrySet()) {
            ressourcesVoisinList.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : listeJoueurs.get(getIndexVoisinDroite(index)).getRessources().entrySet()) {
            if (ressourcesVoisinList.get(entry.getKey()) == null) {
                ressourcesVoisinList.put(entry.getKey(), entry.getValue());
            } else {
                ressourcesVoisinList.put(entry.getKey(), ressourcesVoisinList.get(entry.getKey()) + entry.getValue());
            }
        }
        ressourcesVoisinList.remove("pièces");
        return ressourcesVoisinList;
    }
	
	// Fonction qui permet de passer au tour suivant.
	public void demarrerDernierTour() {
		nbCartesJouées = 0;
		tourCourant += 1;
		log("Début du dernier Tour (défausse) !");
		s.broadcast("Ton dernier tour");
	}

	//Fonction pour résoudre les conflits militaires
	public void conflitsMilitaires(){
		for(int i=0;i<listeJoueurs.size();i++) {
			log("Points de boucliers de "+listeJoueurs.get(i).getNom()+": "+listeJoueurs.get(i).getBouclier());
			//conflit voisin de droite
			if (listeJoueurs.get(getIndexVoisinDroite(i)).getBouclier() < listeJoueurs.get(i).getBouclier()){
				listeJoueurs.get(i).addpointsMilitaires(pointsMilitairesSelonAge());
				log(listeJoueurs.get(i).getNom()+" a gagné " + pointsMilitairesSelonAge() + " point(s) militaire(s) contre " + listeJoueurs.get(getIndexVoisinDroite(i)).getNom() + ".");
			}
			if (listeJoueurs.get(getIndexVoisinDroite(i)).getBouclier() > listeJoueurs.get(i).getBouclier()){
				listeJoueurs.get(i).delpointsMilitaires();
				log(listeJoueurs.get(i).getNom()+" a perdu 1 point militaire contre " + listeJoueurs.get(getIndexVoisinDroite(i)).getNom() +".");
			}
			if (listeJoueurs.get(getIndexVoisinDroite(i)).getBouclier() == listeJoueurs.get(i).getBouclier()){
				log(listeJoueurs.get(i).getNom()+" a autant de points de bouclier que " + listeJoueurs.get(getIndexVoisinDroite(i)).getNom() + " , il ne perd donc pas de points militaires après la bataille.");
			}

			//conflit voisin de gauche
			if (listeJoueurs.get(getIndexVoisinGauche(i)).getBouclier() < listeJoueurs.get(i).getBouclier()){
				listeJoueurs.get(i).addpointsMilitaires(pointsMilitairesSelonAge());
				log(listeJoueurs.get(i).getNom()+" a gagné " + pointsMilitairesSelonAge() + " point(s) militaire(s) contre " +  listeJoueurs.get(getIndexVoisinGauche(i)).getNom() + ".");
			}
			if (listeJoueurs.get(getIndexVoisinGauche(i)).getBouclier() > listeJoueurs.get(i).getBouclier()){
				listeJoueurs.get(i).delpointsMilitaires();
				log(listeJoueurs.get(i).getNom()+" a perdu 1 point militaire contre " +  listeJoueurs.get(getIndexVoisinGauche(i)).getNom() + ".");
			}
			if (listeJoueurs.get(getIndexVoisinGauche(i)).getBouclier() == listeJoueurs.get(i).getBouclier()){
				log(listeJoueurs.get(i).getNom()+" a autant de points de bouclier que " +  listeJoueurs.get(getIndexVoisinGauche(i)).getNom() + ", il ne perd donc pas de points militaires après la bataille.");
			}
		}
	}

	//Fonction pour attribution points militaires selon l'âge
	public int pointsMilitairesSelonAge(){
		int pointsMilitaires = 0;
		switch (getAgeCourant()){
			case 1:
				pointsMilitaires = 1;
				break;
			case 2:
				pointsMilitaires = 3;
				break;
			case 3:
				pointsMilitaires = 5;
				break;
		}
		return pointsMilitaires;
	}

	//Fonction qui retourne l'index du voisin de droite
	public int getIndexVoisinDroite(int currentIndex){
		if (currentIndex == (listeJoueurs.size()-1) ){
			return 0;
		}
		else{
			return 	currentIndex + 1;
		}
	}
	//Fonction qui retourne l'index du voisin de gauche
	public int getIndexVoisinGauche(int currentIndex){
		if (currentIndex == (0) ){
			return (listeJoueurs.size()-1);
		}
		else{
			return 	currentIndex-1;
		}
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
					log("---- Conflits militaires ----");
					conflitsMilitaires();
                    int index = getIndexGagnant();
					Joueur JoueurGagnant = listeJoueurs.get(index);
					int score = JoueurGagnant.getPointsVictoire() + JoueurGagnant.getPièces() + JoueurGagnant.getpointsMilitaires();
					log("Victoire de " + JoueurGagnant.getNom() + " avec " + score + " points de civilisation.");
					this.isGameOn = false;
					s.stop();
				}
				else if(ageEstFini())
				{
                    log("---- Conflits militaires ----");
					conflitsMilitaires();
					changerAge();
				}
				else if(getTourCourant() == 6){
					demarrerDernierTour();
				}
				else
				{
					demarrerTourSuivant();
				}
		}

	}
	
	// Fonction qui détermine et afficher le gagnant de la partie
	public int getIndexGagnant() {
		int indexMax = 0;
		// là on applique les effets des cartes guildes posées
		for(int i=0;i < listeJoueurs.size();i++) {
			for (int k = 0; k < listeJoueurs.get(i).getCartesPosees().size(); k++) {
				if(listeJoueurs.get(i).getCartesPosees().get(k).getEffet().get("nomEffetFinDePartie") != null)
					GestionEffets.appliquerEffetFinDePartie(listeJoueurs.get(i).getCartesPosees().get(k).getEffet(), listeJoueurs.get(i),listeJoueurs.get(getIndexVoisinGauche(i)).getCartesPosees(),listeJoueurs.get(getIndexVoisinDroite(i)).getCartesPosees(),listeJoueurs.get(getIndexVoisinGauche(i)),listeJoueurs.get(getIndexVoisinDroite(i)));
			}
		}
		int max = listeJoueurs.get(0).getPointsVictoire() + listeJoueurs.get(0).getPièces() + listeJoueurs.get(0).getpointsMilitaires();
		for(int i=0;i < listeJoueurs.size();i++) {
			if(listeJoueurs.get(i).getPointsVictoire() + listeJoueurs.get(i).getPièces() + listeJoueurs.get(0).getpointsMilitaires()> max) {
				indexMax = i;
				max = listeJoueurs.get(i).getPointsVictoire() + listeJoueurs.get(i).getPièces() + + listeJoueurs.get(0).getpointsMilitaires();
			}
		}
		return indexMax;

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

    public boolean isGameOn() {
        return isGameOn;
    }
	
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

	public synchronized int getNbCartesJouées() {
		return nbCartesJouées;
	}

	public synchronized void  setNbCartesJouées(int nbCartesJouées) {
		this.nbCartesJouées = nbCartesJouées;
	}

	public boolean isEveryoneReadyStated() {
		return isEveryoneReadyStated;
	}

	public void setEveryoneReadyStated(boolean isEveryoneReadyStated) {
		this.isEveryoneReadyStated = isEveryoneReadyStated;
	}

}
