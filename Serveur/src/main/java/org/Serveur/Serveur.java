package org.Serveur;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.Model.assets.Carte;
import org.Model.assets.ClientID;
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

	private ArrayList<ClientID> clients;
	private int id;
	
	private Partie p;
	
	public Serveur(Configuration config){
		// Initialisation
		serveur = new SocketIOServer(config);
		clients = new ArrayList<ClientID>();
		id = 0;
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
				// On ajoute le client à la liste des clients
				clients.add(new ClientID(id,client));
				id++;
				// On ajoute le joueur à la partie
				p.ajouterJoueur(data);
			}
			
		});
		// Ajout de l'écouteur traitant le message "Prêt" de la part d'un joueur
		serveur.addEventListener("ReadyCheck", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Prêt")) {
					p.setRdy(getIndexFromSocket(client));
				}
			}
			
		});
		
	
		// Ajout de l'écouteur traitant l'événement d'une carte jouée de la part d'un joueur.
		serveur.addEventListener("Carte Jouée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {	
				p.jouerCarte(getIndexFromSocket(client), data);
				
			}
			
		});
		
		// Ajout de l'écouteur traitant de l'événement de la défausse d'une carte.
		serveur.addEventListener("Carte Défaussée", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				p.défausserCarte(getIndexFromSocket(client),data);
			}
			
		});

		// Ajout de l'écouteur traitant de l'évènement du débloquage d'une étape d'une merveille
		serveur.addEventListener("Etape Merveille", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte carte, AckRequest ackSender) throws Exception {
				p.débloquerMerveille(getIndexFromSocket(client), carte);
			}

		});
		// Ajout de l'écouteur traitant de l'évènement d'une carte jouée en commerçant avec des bots voisins.
		serveur.addEventListener("Carte Jouée avec commerce", Carte.class	, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte carte, AckRequest ackSender) throws Exception {
				p.jouerCarteCommerce(getIndexFromSocket(client), carte);
			}

		});
		
	}

	// Fonction qui récupère l'indice du joueur à partir de son socket
	public int getIndexFromSocket(SocketIOClient socket) {
		for(int i =0;i<clients.size();i++) {
			if(clients.get(i).getSocket().equals(socket)) {
				return i;
			}
		}
		return -1;
	}


	public void broadcast(String message) {
		for(int i = 0; i < clients.size();i++) {
			clients.get(i).getSocket().sendEvent(message);
		}
	}

	public void broadcast(String message, Object data){
		for(int i = 0; i < clients.size();i++) {
			clients.get(i).getSocket().sendEvent(message, data);
		}
	}
	public void sendEvent(int index, String event, Object...data) {
		clients.get(index).getSocket().sendEvent(event, data);
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
