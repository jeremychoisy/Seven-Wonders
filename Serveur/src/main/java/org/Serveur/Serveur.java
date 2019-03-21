package org.Serveur;

import java.io.UnsupportedEncodingException;
import org.Model.assets.Carte;
import org.Model.tools.CouleurSorties;
import org.Model.tools.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

import org.partie.Partie;


public class Serveur {
	// variables client/serveur
	private SocketIOServer serveur;
	
	private Partie p;
	
	public Serveur(Configuration config){
		// Initialisation
		serveur = new SocketIOServer(config);
		p = new Partie(this, true);

		log("Attente de connexion des joueur...");
		// Ajout de l'écouteur gérant la connexion d'un client
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				log("Connexion de " + socketIOClient.getRemoteAddress());
				log("Attente de l'authentification de : " + socketIOClient.getRemoteAddress());
			}	
		});
		// Ajout de l'écouteur traitant le message d'identification du client
		serveur.addEventListener("id", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				// On ajoute le joueur à la partie
				p.ajouterJoueur(data, client);
			}
			
		});
		// Ajout de l'écouteur traitant le message "Prêt" de la part d'un joueur
		serveur.addEventListener("ReadyCheck", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Prêt")) {
					p.setRdy(client);
				}
			}
			
		});
		// Ajout de l'écouteur traitant l'événement d'une carte jouée de la part d'un joueur.
		serveur.addEventListener("Carte Jouée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {	
				p.jouerCarte(client, data);
				
			}
			
		});
		
		// Ajout de l'écouteur traitant de l'événement de la défausse d'une carte.
		serveur.addEventListener("Carte Défaussée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				p.défausserCarte(client,data);		
			}
			
		});

		serveur.addEventListener("Etape Merveille", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte carte, AckRequest ackSender) throws Exception {
				p.débloquerMerveille(client, carte);
			}

		});
		
	}


	
	public void broadcast(String message) {
		for(int i = 0; i < p.getListeJoueurs().size();i++) {
			p.getListeJoueurs().get(i).getSocket().sendEvent(message);
		}
	}
	
	public void sendEvent(SocketIOClient s, String event, Object...data) {
		s.sendEvent(event, data);
	}
	
	public void stop() {
		log("Fin de la connexion.");
		serveur.stop();
	}
	

	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_BLUE + "Serveur : " + s + CouleurSorties.ANSI_RESET);
	}

	
	public void demarrer() {
		serveur.start();
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
