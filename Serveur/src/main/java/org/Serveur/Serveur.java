package org.Serveur;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import org.Model.assets.Carte;
import org.Model.assets.ClientID;
import org.Model.tools.CouleurSorties;
import org.Model.tools.MyPrintStream;
import org.partie.Partie;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Serveur {
	// variables client/serveur
	private SocketIOServer serveur;

	private ArrayList<ClientID> clients;
	private int id;
	
	private Partie p;
	private boolean loop;
	int loopNumber = 0;
	int loops;
	int rdycheck = 0;
	
	public Serveur(Configuration config, boolean loop, int loops){
		// Initialisation
		serveur = new SocketIOServer(config);
		clients = new ArrayList<ClientID>();
		this.loop = loop;
		this.loops = loops;
		id = 0;
		p = new Partie(this, !loop);


		log("Attente de connexion des joueurs...");
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

					clients.add(new ClientID(id, data, client));
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

		serveur.addEventListener("Reset", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				if(data.equals("Done")) {
					synchronized (this) {
						rdycheck += 1;
						if (rdycheck == clients.size()) {
							rdycheck = 0;
							newGame();
						}
					}
				}
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

	public void sendEvent(int index, String event, Object...data) {
		clients.get(index).getSocket().sendEvent(event, data);
	}
	
	public void stop() {
		if(!loop || loopNumber == loops) {
			if(loop){
				log("**** BILAN ****");
				for(int i=0;i<clients.size();i++){
					log("" + clients.get(i).getName() + " : ");
					log("\tVictoires : " + clients.get(i).getVictoires());
					log("\tMoyenne des scores : " + clients.get(i).getScoresAverage());
				}
			}
			log("Fin de la connexion.");
			serveur.stop();
		} else {
			clients.get(p.getIndexGagnant()).addVictoire(p.getScoreJoueurGagnant());
			broadcast("Reset");
		}
	}
	

	// Formatage des sorties textes
	public void log(String s) {
		System.out.println(CouleurSorties.ANSI_BLUE + "Serveur : " + s + CouleurSorties.ANSI_RESET);
	}

	public void newGame(){
		loopNumber += 1;
		log("Partie n°= " + (loopNumber+1));
		this.p = new Partie(this,false);
		for(int i=0;i<clients.size();i++){
			p.ajouterJoueur(clients.get(i).getName());
		}
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
        boolean loop = false;
        int loops = Integer.parseInt(args[1]);
        if(Integer.parseInt(args[0]) == 1){
        	loop = true;
		}
        
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);
        SocketConfig s = config.getSocketConfig();
        s.setReuseAddress(true);

        
        Serveur serveur = new Serveur(config,loop, loops);
        serveur.demarrer();
	}
	
}
