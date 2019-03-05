package partie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.corundumstudio.socketio.SocketIOClient;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Serveur.Serveur;


@ExtendWith(MockitoExtension.class)

public class PartieTest {
	
	Partie p;
	
	@Mock
	SocketIOClient socket;
	
	@Mock
	Serveur s;
			
	@BeforeEach
	@Test
	void constructeurEtrecuperationDonnéesTest() {
		p = new Partie(s);
		
		assertEquals(98, p.getC().length, "Il y a normalement 98 cartes dans le tableau");
	}
	
	@Test
	void ajouterJoueurTest() {
		ArrayList<Joueur> listeJoueur = new ArrayList<Joueur>();
		Joueur j = new Joueur("bot_1",socket);
		listeJoueur.add(j);
		
		p.ajouterJoueur("bot_1", socket);
		
		assertEquals( listeJoueur.get(0).toString() , p.getListeJoueurs().get(0).toString(), "Le joueur a été correctement ajouté à la partie");		
	}
	
	@Test
	void construireTest() {
		p.construireListes();
		
		assertEquals(28,p.getCartesAgeI().size(),"Il y a normalement 28 cartes dans le paquet de cartes de l'âge I");
		assertEquals(28,p.getCartesAgeII().size(),"Il y a normalement 28 cartes dans le paquet de cartes de l'âge II");
		assertEquals(4,p.getMerveilles().size(),"Il y a normalement 4 plateaux merveille dans le tas de plateaux de merveilles.");
	}
	
	@Test
	void initPartieTest() {
		
		p.ajouterJoueur("Bot_1", socket);
		p.ajouterJoueur("Bot_2", socket);
		p.ajouterJoueur("Bot_3", socket);
		p.ajouterJoueur("Bot_4", socket);
		
		p.initPartie();
		
		for(Joueur j:p.getListeJoueurs()) {
			assertEquals(7,j.getM().size(),"Il y a normalement 7 cartes dans la main de chaque joueur");
			assertEquals(3,j.GetRessources().get("pièces"),"Chaque joueur dispose normalement de 3 pièces");
		}
	}
	
	@Test
	void estFinieTest() {
		p.setAgeCourant(2);
		p.setTourCourant(7);
		
		assertEquals(true, p.estFinie(), "La partie est normalement terminée.");
		
	}
	
	@Test
	void setRdyTest() {
		p.ajouterJoueur("Bot_1", socket);
		p.setRdy(socket);
		
		assertEquals(true,p.getListeJoueurs().get(0).isRdy(),"Le joueur est normalement prêt.");
	}
	
	@Test
	void ageEstFinieTest() {
		p.setTourCourant(7);
		
		assertEquals(true,p.ageEstFini(),"L'âge est normalement terminé.");
	}
	
	@Test
	void jouerCarteTest() {
		p.ajouterJoueur("Bot_1", socket);
		Joueur j = p.getListeJoueurs().get(0);
		j.setPièces(3);
		
		Map<String,String> effet = new HashMap<String,String>();
		Map<String,Integer> ressources = new HashMap<String,Integer>();
		
		ressources.put("pièces",1);
					
		effet.put("nomEffet", "gain_points_victoire");
		effet.put("valeurEffet","3");
		
		Carte c = new Carte("Prêteur sur gage","Batiment Civil",effet,ressources, 4,1);
		j.getM().add(c);
		p.jouerCarte(socket, c);
		
		assertEquals(2,j.getPièces(),"Le joueur ne dispose normalement plus que de 2 pièces.");
		assertEquals(3,j.getPoints_victoire(),"Le joueur a normalement gagné 3 points de victoires.");
		assertEquals(1,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 1.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");		
	}
	
	@Test
	void défausserCarteTest() {
		p.ajouterJoueur("Bot_1", socket);
		
		// Cas général
		Joueur j = p.getListeJoueurs().get(0);
		j.setPièces(3);
		
		Map<String,String> effet = new HashMap<String,String>();
		Map<String,Integer> ressources = new HashMap<String,Integer>();
		
		ressources.put("pièces",1);
					
		effet.put("nomEffet", "gain_points_victoire");
		effet.put("valeurEffet","3");
		
		Carte c = new Carte("Prêteur sur gage","Batiment Civil",effet,ressources, 4,1);
		j.getM().add(c);
		p.défausserCarte(socket, c);
		
		assertEquals(6,j.getPièces(),"Le joueur dispose normalement de 6 pièces.");
		assertEquals(1,p.getDéfausse().size(),"Il y'a normalement maintenant une carte dans la défausse.");
		assertEquals(1,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 1.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");	
		
		// Cas de la fin d'un âge
		
		j.getM().add(c);
		j.setPièces(3);
		p.setTourCourant(7);
		p.défausserCarte(socket, c);
		
		assertEquals(3,j.getPièces(),"Le joueur dispose normalement de 3 pièces.");
		assertEquals(2,p.getDéfausse().size(),"Il y'a normalement maintenant deux carte dans la défausse.");
		assertEquals(2,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement maintenant de 2.");
		assertEquals(0,j.getM().size(),"La main du joueur est normalement vide.");		
	}
	
	@Test
	void changerAgeTest() {
		p.ajouterJoueur("bot_1", socket);
		p.construireListes();
		p.changerAge();
		
		assertEquals(2,p.getAgeCourant(),"L'âge courant est normalement maintenant de 2.");
		assertEquals(1,p.getTourCourant(),"Il y'a normalement maintenant une carte dans la défausse.");
		assertEquals(7,p.getListeJoueurs().get(0).getM().size(),"Le nombre de cartes dans la main du joueur est normalement de 7.");		
	}
	
	@Test
	void demarrerTourSuivantTest() {
		p.setNbCartesJouées(4);
		p.setTourCourant(2);
		
		p.demarrerTourSuivant();
		
		assertEquals(0,p.getNbCartesJouées(),"Le nombre de cartes jouées est normalement de 0.");
		assertEquals(3,p.getTourCourant(),"Le tour courant est normalement de 3.");
	}
	
	@Test
	void everyoneIsRdyTest() {
		p.ajouterJoueur("bot_1", socket);
		p.ajouterJoueur("bot_2", socket);
		
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
	void getIndexGagnantTest() {
		p.ajouterJoueur("bot_1", socket);
		p.getListeJoueurs().get(0).setPièces(2);
		p.getListeJoueurs().get(0).setPoints_victoire(4);
		p.ajouterJoueur("bot_2", socket);
		p.getListeJoueurs().get(1).setPièces(10);
		p.getListeJoueurs().get(1).setPoints_victoire(2);
		p.ajouterJoueur("bot_3", socket);
		p.getListeJoueurs().get(2).setPièces(8);
		p.getListeJoueurs().get(2).setPoints_victoire(3);

		
		assertEquals(1,p.getIndexGagnant(),"Le joueur gagnant correspond normalement à l'index 1.");
				
	}
}
