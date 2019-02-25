package org.Serveur;

import java.io.UnsupportedEncodingException;
import org.Model.assets.Carte;
import org.Model.assets.Id;
import org.Model.assets.Partie;
import org.Model.tools.CouleurSorties;
import org.Model.tools.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;


public class Serveur {
	// variables client/serveur
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	
	private Partie p;
	
	public Serveur(Configuration config){
		// Initialisation
		serveur = new SocketIOServer(config);
		p = new Partie();

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
				// On ajoute le joueur à la partie
				p.ajouterJoueur(data.getNom(), client);

			}
			
		});
		// Ajout de l'écouteur traitant le message "Prêt" de la part d'un joueur
		serveur.addEventListener("readyCheck", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Prêt")) {
					p.setRdy(client);
				}
			}
			
		});
		// Ajout de l'écouteur traitant l'événement d'une carte jouée de la part d'un joueur
		serveur.addEventListener("Carte Jouée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				System.out.println(data.getNom());
				p.jouerCarte(client, data);
				if(p.estGagnant(client) || p.estFinie()) {
					p.afficherResultats(client);
					synchronized(attenteConnexion) {
						attenteConnexion.notify();
					}
				}
				else
				{
					if(p.tourEstFini())
						p.demarrerTourSuivant();
				}
				
			}
			
		});
		
	}
	
	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_BLUE + "Serveur : " + s + CouleurSorties.ANSI_RESET);
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
		log("fin de la connexion");
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
