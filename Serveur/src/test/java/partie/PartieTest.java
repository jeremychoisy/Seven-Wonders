package partie;

import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Main;
import org.Model.assets.Merveille;
import org.Serveur.Serveur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.partie.Partie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
@ExtendWith(MockitoExtension.class)

public class PartieTest {
	
	Partie p;

	@Mock
	Serveur s;

	@BeforeEach
	@Test
	void constructeurEtrecuperationDonnéesTest() {
		p = new Partie(s, false);
		
		assertEquals(148, p.getC().length, "Il y a normalement 148 cartes dans le tableau");
	}
	
	@Test
	void ajouterJoueurTest() {
		Joueur j = new Joueur("bot_1");

		p.ajouterJoueur("bot_1");
		
		assertEquals( j.toString() , p.getListeJoueurs().get(0).toString(), "Le joueur a été correctement ajouté à la partie");

		p.ajouterJoueur("bot_2");

		assertEquals( 2, p.getListeJoueurs().size(), "Les deux joueurs ont été correctement ajouté à la partie");

		p.ajouterJoueur("bot_3");
		p.ajouterJoueur("bot_4");
		p.ajouterJoueur("bot_5");
		p.ajouterJoueur("bot_6");
		p.ajouterJoueur("bot_7");

		assertEquals(true, p.isGameOn(), "Une fois les quatres joueurs ajoutés, la partie s'est initialisé");
	}

	@Test
	void construireTest() {
		p.construireListes();
		
		assertEquals(28,p.getCartesAgeI().size(),"Il y a normalement 49 cartes dans le paquet de cartes de l'âge I");
		assertEquals(28,p.getCartesAgeII().size(),"Il y a normalement 49 cartes dans le paquet de cartes de l'âge II");
		assertEquals(28,p.getCartesAgeIII().size(),"Il y a normalement 49 cartes dans le paquet de cartes de l'âge II");
		assertEquals(4,p.getMerveilles().size(),"Il y a normalement 7 plateaux merveille dans le tas de plateaux de merveilles.");
	}
	
	@Test
	void initPartieTest() {
		p.ajouterJoueur("Bot_1");
		p.ajouterJoueur("Bot_2");
		p.ajouterJoueur("Bot_3");
		p.ajouterJoueur("Bot_4");


		// 4 Joueurs ont rejoint la partie, initPartie() est appelée automatiquement.
		
		for(Joueur j:p.getListeJoueurs()) {
			assertEquals(7,j.getM().size(),"Il y a normalement 7 cartes dans la main de chaque joueur");
			assertEquals(3,j.getRessources().get("pièces"),"Chaque joueur dispose normalement de 3 pièces");
		}
	}
	
	@Test
	void estFinieTest() {
		p.setAgeCourant(3);
		p.setTourCourant(7);
		
		assertEquals(true, p.estFinie(), "La partie est normalement terminée.");
		
	}
	
	@Test
	void setRdyTest() {
		p.ajouterJoueur("Bot_1");
		p.setRdy(0);
		
		assertEquals(true,p.getListeJoueurs().get(0).isRdy(),"Le joueur est normalement prêt.");
	}
	
	@Test
	void ageEstFinieTest() {
		p.setTourCourant(7);
		
		assertEquals(true,p.ageEstFini(),"L'âge est normalement terminé.");
	}
	
	@Test
	void jouerCarteTest() {
		p.ajouterJoueur("Bot_1");
		Joueur j = p.getListeJoueurs().get(0);
		j.addPièces(3);
		
		Map<String,String> effet = new HashMap<String,String>();
		Map<String,Integer> ressources = new HashMap<String,Integer>();
		
		ressources.put("pièces",1);
					
		effet.put("nomEffet", "gain_pointsVictoire");
		effet.put("valeurEffet","3");
		
		Carte c = new Carte("Prêteur sur gage","Bâtiment Civil",effet,ressources, 4,1);
		j.getM().add(c);
		p.jouerCarte(0, c);
		
		assertEquals(2,j.getPièces(),"Le joueur ne dispose normalement plus que de 2 pièces.");
		assertEquals(3,j.getPointsVictoire(),"Le joueur a normalement gagné 3 points de victoires.");
		assertEquals(1,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 1.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");
	}
	
	@Test
	void défausserCarteTest() {
		p.ajouterJoueur("Bot_1");
		
		// Cas général
		Joueur j = p.getListeJoueurs().get(0);
		j.addPièces(3);
		
		Map<String,String> effet = new HashMap<String,String>();
		Map<String,Integer> ressources = new HashMap<String,Integer>();
		
		ressources.put("pièces",1);
					
		effet.put("nomEffet", "gain_pointsVictoire");
		effet.put("valeurEffet","3");
		
		Carte c = new Carte("Prêteur sur gage","Bâtiment Civil",effet,ressources, 4,1);
		j.getM().add(c);
		p.défausserCarte(0, c);

		assertEquals(6,j.getPièces(),"Le joueur dispose normalement de 6 pièces.");
		assertEquals(1,p.getDéfausse().size(),"Il y'a normalement maintenant une carte dans la défausse.");
		assertEquals(1,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 1.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");	
		
		// Cas de la fin d'un âge
		
		j.getM().add(c);
		p.setTourCourant(7);
		p.défausserCarte(0, c);
		
		assertEquals(6,j.getPièces(),"Le joueur dispose normalement de 6 pièces.");
		assertEquals(2,p.getDéfausse().size(),"Il y'a normalement maintenant deux carte dans la défausse.");
		assertEquals(2,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement maintenant de 2.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");		
	}
	
	@Test
	void changerAgeTest() {
		p.ajouterJoueur("bot_1");
		p.construireListes();

		// Passage vers l'âge II
		p.changerAge();
		
		assertEquals(2,p.getAgeCourant(),"L'âge courant est normalement maintenant de 2.");
		assertEquals(1,p.getTourCourant(),"Le tour courant est normalement le premier.");
		assertEquals(7,p.getListeJoueurs().get(0).getM().size(),"Le nombre de cartes dans la main du joueur est normalement de 7.");

		// Passage vers l'âge III
		p.changerAge();
		assertEquals(3,p.getAgeCourant(),"L'âge courant est normalement maintenant de 2.");
		assertEquals(1,p.getTourCourant(),"Le tour courant est normalement le premier.");
		assertEquals(7,p.getListeJoueurs().get(0).getM().size(),"Le nombre de cartes dans la main du joueur est normalement de 7.");
	}
	
	@Test
	void demarrerTourSuivantTest() {
		p.setNbCartesJouées(4);
		p.setTourCourant(2);
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		Main mainBot1 = new Main();
		Main mainBot2 = new Main();
		Carte un = new Carte();
		Carte deux = new Carte();
		Carte trois = new Carte();
		Carte quatre = new Carte();
		mainBot1.add(un);
		mainBot1.add(deux);
		mainBot2.add(trois);
		mainBot2.add(quatre);
		
		p.getListeJoueurs().get(0).setM(mainBot1);
		p.getListeJoueurs().get(1).setM(mainBot2);
		
		assertEquals(mainBot2,p.getListeJoueurs().get(1).getM());
		assertEquals(mainBot1,p.getListeJoueurs().get(0).getM());
		
		p.demarrerTourSuivant();
		
		assertEquals(mainBot2,p.getListeJoueurs().get(0).getM());
		assertEquals(mainBot1,p.getListeJoueurs().get(1).getM());
		assertEquals(0,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 0.");
		assertEquals(3,p.getTourCourant(),"Le tour courant est normalement de 3.");
	}
	
	@Test
	void everyoneIsRdyTest() {
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		
		p.getListeJoueurs().get(0).setRdy(true);
		
		assertEquals(false,p.isEveryoneRdy(),"Tout le monde n'est normalement pas prêt.");
		
		p.getListeJoueurs().get(1).setRdy(true);
		
		assertEquals(true,p.isEveryoneRdy(),"Tout le monde est normalement prêt.");
	}
	
	@Test
	void tourEstFiniTest() {
		p.setNbCartesJouées(3);
		
		assertEquals(false, p.tourEstFini(), "Le tour n'est normalement pas fini.");
		
		p.setNbCartesJouées(4);
		
		assertEquals(true, p.tourEstFini(), "Le tour est normalement fini.");
	}

	@Test
	void débloquerMerveilleTest(){
		p.ajouterJoueur("bot_1");
		Joueur j = p.getListeJoueurs().get(0);

		Map<String,Integer> ressourceEtapeUne = new HashMap<String,Integer>();
		ressourceEtapeUne.put("bois", 2);
		Map<String,Integer> ressourceEtapeDeux = new HashMap<String,Integer>();
		ressourceEtapeDeux.put("pierre", 2);
		Map<String,Integer> ressourceEtapeTrois = new HashMap<String,Integer>();
		ressourceEtapeTrois.put("minerai", 2);

		Map<String,String> effetEtapeUne = new HashMap<String,String>();
		Map<String,String> effetEtapeDeux = new HashMap<String,String>();
		Map<String,String> effetEtapeTrois = new HashMap<String,String>();

		effetEtapeUne.put("nomEffet", "gain_pointsVictoire");
		effetEtapeUne.put("valeurEffet", "3");
		effetEtapeDeux.put("nomEffet", "Bâtiment_gratuit");
		effetEtapeTrois.put("nomEffet", "gain_pointsVictoire");
		effetEtapeTrois.put("valeurEffet", "7");

		Merveille m = new Merveille("Olympia","bois",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);

		Map<String,String> effet = new HashMap<String,String>();
		Map<String,Integer> ressources = new HashMap<String,Integer>();

		ressources.put("pièces",1);

		effet.put("nomEffet", "gain_pointsVictoire");
		effet.put("valeurEffet","3");

		Carte c = new Carte("Prêteur sur gage","Bâtiment Civil",effet,ressources, 4,1);

		j.setMerveille(m);

		for(int i = 0; i < 7; i++) {
			j.getM().add(c);
		}

		j.ajouterRessources("bois",2);

		p.débloquerMerveille(0,j.getM().get(0));

		assertEquals(1, j.getMerveille().getEtapeCourante(), "L'étape courante de la merveille devrait être 1.");
		assertEquals( 6, j.getM().size(),"La taille de la main du joueur devrait maintenant être de 6");
		assertEquals( 3, j.getPointsVictoire(), "Le joueur devrait maintenant avoir 3 points de victoire.");
	}

	@Test
	void getIndexVoisinTest(){
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		p.ajouterJoueur("bot_3");
		p.ajouterJoueur("bot_4");

		assertEquals(3, p.getIndexVoisinGauche(0), "Le voisin de gauche du premier joueur devrait être bot_4 (indice 3)");
		assertEquals(1, p.getIndexVoisinDroite(0), "Le voisin de droite du premier joueur devrait être bot_2 (indice 1)");

		assertEquals(2, p.getIndexVoisinGauche(3), "Le voisin de gauche du dernier joueur devrait être bot_3 (indice 2)");
		assertEquals(0, p.getIndexVoisinDroite(3), "Le voisin de droite du dernier joueur devrait être bot_1 (indice 0)");
	}

	@Test
	void conflitsMilitairesTest(){
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		p.ajouterJoueur("bot_3");
		p.ajouterJoueur("bot_4");

		p.getListeJoueurs().get(0).setBouclier(1);
		p.getListeJoueurs().get(1).setBouclier(1);

		p.conflitsMilitaires();

		assertEquals(1, p.getListeJoueurs().get(0).getpointsMilitaires(), "bot_1 devrait avoir 0 points militaires (1 victoire / 1 défaite");
		assertEquals(1, p.getListeJoueurs().get(1).getpointsMilitaires(), "bot_2 devrait avoir 0 points militaires (1 victoire / 1 défaite");
		assertEquals(-1, p.getListeJoueurs().get(2).getpointsMilitaires(), "bot_3 devrait avoir -1 points militaires (0 victoire / 1 défaite");
		assertEquals(-1, p.getListeJoueurs().get(3).getpointsMilitaires(), "bot_4 devrait avoir -1 points militaires (0 victoire / 1 défaite");

	}

	@Test
	void pointsMilitairesSelonAgeTest(){
		assertEquals(1, p.pointsMilitairesSelonAge(), "les points militaires devraient valoir 1.");
		p.setAgeCourant(2);
		assertEquals(3, p.pointsMilitairesSelonAge(), "les points militaires devraient valoir 3.");
		p.setAgeCourant(3);
		assertEquals(5, p.pointsMilitairesSelonAge(), "les points militaires devraient valoir 5.");
	}

	
	@Test
	void getIndexGagnantTest() {
		p.ajouterJoueur("bot_1");
		p.getListeJoueurs().get(0).addPièces(2);
		p.getListeJoueurs().get(0).setPointsVictoire(4);
		p.ajouterJoueur("bot_2");
		p.getListeJoueurs().get(1).addPièces(10);
		p.getListeJoueurs().get(1).setPointsVictoire(2);
		p.ajouterJoueur("bot_3");
		p.getListeJoueurs().get(2).addPièces(8);

		p.getListeJoueurs().get(2).setPointsVictoire(3);

		
		assertEquals(1,p.getIndexGagnant(),"Le joueur gagnant correspond normalement à l'index 1.");
				
	}


	@Test
	void jouerCarteCommerceTest(){
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		p.ajouterJoueur("bot_3");

		p.getListeJoueurs().get(1).ajouterRessources("bois",2);
		p.getListeJoueurs().get(0).addPièces(4);

		Map effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_boucliers");
		effet.put("valeurEffet", "1");

		Map ressources = new HashMap<String,Integer>();
		ressources.put("bois",1);

		Carte c = new Carte("Palissade","Conflit Militaire",effet,ressources, 7,1);

		p.jouerCarteCommerce(0,c);

		assertEquals(2, p.getListeJoueurs().get(0).getPièces(),"Le joueur 1 devrait maintenant avoir 3 pièces restantes.");
		assertEquals(2, p.getListeJoueurs().get(1).getPièces(),"Le joueur 2 devrait maintenant avoir gagné 2 pièces.");

		ressources = new HashMap<String,Integer>();
		ressources.put("bois",3);

		c = new Carte("Palissade","Conflit Militaire",effet,ressources, 7,1);

		p.getListeJoueurs().get(2).ajouterRessources("bois",1);
		p.getListeJoueurs().get(0).addPièces(4);

		p.jouerCarteCommerce(0, c);

		assertEquals(0, p.getListeJoueurs().get(0).getPièces(),"Le joueur 1 devrait maintenant avoir 0 pièces restantes.");
		assertEquals(6, p.getListeJoueurs().get(1).getPièces(),"Le joueur 2 devrait maintenant avoir gagné 6 pièces.");
		assertEquals(2, p.getListeJoueurs().get(2).getPièces(),"Le joueur 3 devrait maintenant avoir gagné 2 pièces.");
	}

	@Test
	void goNextTest(){
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		p.ajouterJoueur("bot_3");
		p.ajouterJoueur("bot_4");


		p.construireListes();

		doAnswer(new Answer(){
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String s = invocation.getArgument(0);
				assertEquals("Ton dernier tour",s,"La classe Partie a normalement envoyé l'évènement 'Ton dernier Tour' en broadcast aux clients via la classe Serveur.");
				return null;
			}
		}).when(s).broadcast("Ton dernier tour");


		p.setTourCourant(6);
		p.setAgeCourant(2);
		p.setNbCartesJouées(4);

		p.goNext();

		assertEquals(7, p.getTourCourant(),"Le tour courant devrait normalement être le 7ème");

		p.setNbCartesJouées(4);
		p.goNext();

		assertEquals(3, p.getAgeCourant(), "La partie se déroule maintenant normalement au 3ème âge.");
		assertEquals(1, p.getTourCourant(), "La partie commence maintenant normalement le 1er tour.");

		p.setNbCartesJouées(4);
		p.setTourCourant(7);
		p.goNext();

		assertEquals(false, p.isGameOn(), "La partie est normalement maintenant terminée.");

	}

	@Test
	void appliquerEffetFinDePartie(){
		p.ajouterJoueur("bot_1");
		p.ajouterJoueur("bot_2");
		p.ajouterJoueur("bot_3");



		Map effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_ressources");
		effet.put("ressourceEffet", "bois");
		effet.put("valeurEffet", "2");

		Map ressources = new HashMap<String,Integer>();
		ressources.put("pièces",1);


		Carte carteMatièresPremières= new Carte("Scierie","Matières Premières",effet,ressources,4,2);

		ressources = new HashMap<String, Integer>();
		ressources.put("minerai", 2);
		ressources.put("argile", 1);
		ressources.put("pierre", 1);
		ressources.put("bois", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Matières Premières");
		effet.put("valeurEffetFinDePartie", "1");


		Carte guilde = new Carte("Guilde des Travailleurs","Guilde",effet,ressources,0, 3);

		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteMatièresPremières);
		p.jouerCarte(2, carteMatièresPremières);


		p.getIndexGagnant();

		assertEquals(2, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");

		ressources = new HashMap<String,Integer>();

		effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_ressources");
		effet.put("ressourceEffet", "papier");
		effet.put("valeurEffet", "1");

		Carte carteProduitsManufacturés= new Carte("Presse","Produit Manufacturé",effet,ressources, 3,2);

		ressources = new HashMap<String, Integer>();
		ressources.put("minerai", 2);
		ressources.put("pierre", 2);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Produit Manufacturé");
		effet.put("valeurEffetFinDePartie", "2");

		guilde = new Carte("Guilde des Artisans","Guilde",effet,ressources,0, 3);

		p.getListeJoueurs().get(1).setPointsVictoire(0);

		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());


		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteProduitsManufacturés);
		p.jouerCarte(2, carteProduitsManufacturés);


		p.getIndexGagnant();

		assertEquals(4, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 4 points de victoires supplémentaires");

		effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_ressource_multiples");
		effet.put("valeurEffet", "1");
		effet.put("ressourceEffet1", "bois");
		effet.put("ressourceEffet2","minerai");
		effet.put("ressourceEffet3", "pierre");
		effet.put("ressourceEffet4","argile");

		ressources = new HashMap<String,Integer>();
		ressources.put("bois", 2);

		Carte carteCommerce = new Carte("Arène","Bâtiment Commercial",effet,ressources, 3,3);

		ressources = new HashMap<String, Integer>();
		ressources.put("papier", 1);
		ressources.put("tissu", 1);
		ressources.put("verre", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Bâtiment Commercial");
		effet.put("valeurEffetFinDePartie", "1");

		guilde = new Carte("Guilde des Commerçants","Guilde",effet,ressources,0, 3);

		p.getListeJoueurs().get(1).setPointsVictoire(0);

		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());

		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteCommerce);
		p.jouerCarte(2, carteCommerce);


		p.getIndexGagnant();

		assertEquals(2, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");

		ressources = new HashMap<String, Integer>();
		ressources.put("argile", 3);
		ressources.put("tissu", 1);
		ressources.put("papier", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Bâtiment Scientifique");
		effet.put("valeurEffetFinDePartie", "1");

		Carte carteScientifique = new Carte("Université","Bâtiment Scientifique",effet,ressources, 3,3);

		ressources = new HashMap<String, Integer>();
		ressources.put("papier", 1);
		ressources.put("tissu", 1);
		ressources.put("verre", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Bâtiment Scientifique");
		effet.put("valeurEffetFinDePartie", "1");

		guilde = new Carte("Guilde des Philosophes","Guilde",effet,ressources,0, 3);

		p.getListeJoueurs().get(1).setPointsVictoire(0);

		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());

		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteScientifique);
		p.jouerCarte(2, carteScientifique);


		p.getIndexGagnant();

		assertEquals(2, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");


		ressources = new HashMap<String, Integer>();
		ressources.put("pierre", 1);
		ressources.put("minerai", 3);

		effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_boucliers");
		effet.put("valeurEffet", "3");

		Carte carteMilitaire = new Carte("Fortifications","Conflit Militaire",effet,ressources, 3,3);


		ressources = new HashMap<String, Integer>();
		ressources.put("argile", 3);
		ressources.put("verre", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Conflit Militaire");
		effet.put("valeurEffetFinDePartie", "1");

		guilde = new Carte("Guilde des Espions","Guilde",effet,ressources,0, 3);

		p.getListeJoueurs().get(1).setPointsVictoire(0);

		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());

		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteMilitaire);
		p.jouerCarte(2, carteMilitaire);


		p.getIndexGagnant();

		assertEquals(2, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");


		effet = new HashMap<String,String>();
		effet.put("nomEffet", "gain_pointsVictoire");
		effet.put("valeurEffet", "7");

		ressources = new HashMap<String,Integer>();
		ressources.put("argile",2);
		ressources.put("minerai",1);
		ressources.put("papier",1);
		ressources.put("tissu",1);
		ressources.put("verre",1);

		Carte carteCivil = new Carte("Panthéon","Bâtiment Civil",effet,ressources, 3,3);

		ressources = new HashMap<String, Integer>();
		ressources.put("bois", 3);
		ressources.put("pierre", 1);
		ressources.put("tissu", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_types_cartes");
		effet.put("TypeEffetFinDePartie", "Bâtiment Civil");
		effet.put("valeurEffetFinDePartie", "1");

		guilde = new Carte("Guilde des Magistrats","Guilde",effet,ressources, 0, 3);

		p.getListeJoueurs().get(1).setPointsVictoire(0);

		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());

		p.jouerCarte(1,guilde);
		p.jouerCarte(0, carteCivil);
		p.jouerCarte(2, carteCivil);


		p.getIndexGagnant();

		assertEquals(2, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");


		p.getListeJoueurs().get(1).setPointsVictoire(0);


		ressources = new HashMap<String, Integer>();
		ressources.put("minerai", 2);
		ressources.put("pierre", 1);
		ressources.put("tissu", 1);

		effet = new HashMap<String,String>();
		effet.put("nomEffetFinDePartie", "gain_pointsVictoire_par_jetons_défaites");
		effet.put("ValeurEffetFinDePartie", "1");

		guilde = new Carte("Guilde des Stratèges","Guilde",effet,ressources,0, 3);

		p.getListeJoueurs().get(1).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(0).setCartesPosees(new ArrayList<>());
		p.getListeJoueurs().get(2).setCartesPosees(new ArrayList<>());

		p.jouerCarte(1,guilde);
		//p.jouerCarte(0,carteMilitaire);
		//p.jouerCarte(2,carteCommerce);
		p.getListeJoueurs().get(0).setJetonsDefaites(2);
		p.getListeJoueurs().get(2).setJetonsDefaites(1);

		p.getIndexGagnant();

		assertEquals(3, p.getListeJoueurs().get(1).getPointsVictoire(), "Le joueur 2 doit avoir 2 points de victoires supplémentaires");


	}
}
