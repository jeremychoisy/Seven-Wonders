package org.Serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.Model.assets.Carte;
import org.Model.assets.Id;
import org.Model.assets.Joueur;
import org.Model.tools.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

public class Serveur {
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	private ArrayList<Joueur> listeJoueur;
	private final int nbJoueur = 1;
	private final int nbCartes = 7;
	private int idJoueur = 1;
	private Carte[] c; 

	public Serveur(Configuration config){
		serveur = new SocketIOServer(config);
		listeJoueur = new ArrayList<Joueur>();
		c = new Carte[nbCartes];
		recupererDonnees();
		System.out.println("Serveur : Attente de connexion du joueur...");
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				System.out.println("Serveur : Connexion de " + socketIOClient.getRemoteAddress());
				System.out.println("Attente de l'authentification de : " + socketIOClient.getRemoteAddress());
			}	
		});
		
		serveur.addEventListener("id", Id.class, new DataListener<Id>(){

			@Override
			public void onData(SocketIOClient client, Id data, AckRequest ackSender) throws Exception {
				Joueur j = new Joueur(data.getNom(),idJoueur,client);
				System.out.println("Serveur : le joueur : " + client.getRemoteAddress() + " s'est identifié en tant que : " + data.getNom() + ".");
				listeJoueur.add(j);
				if(listeJoueur.size() == nbJoueur) {
					System.out.println("Serveur : le lobby est complet, préparation de la partie en cours...");
					initPartie();
				}

			}
			
		});
		
		serveur.addEventListener("reponse", String.class, new DataListener<String>(){

			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				System.out.println("Serveur : statut du client :" + data);
				synchronized(attenteConnexion) {
					attenteConnexion.notify();
				}
				
			}
			
		});
		
	}

	
	public void recupererDonnees() {
		FileReader reader = null;
		Gson gson = new Gson();
		try {
			reader = new FileReader("/home/jeremy/eclipse-workspace/seven-wonders/Assets/cartes.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Serveur : Chargement des données...");
		c = gson.fromJson(reader, Carte[].class);

	}
	public void initPartie() {
		for(int i=0;i<listeJoueur.size();i++) {
			// Opération de préparation de la partie
			ArrayList<Carte> main = new ArrayList<Carte>();
			for(int j=0;j<7;j++) {
				main.add(c[j]);
			}
			
			listeJoueur.get(i).getSocket().sendEvent("main", main);
		}
	}
	
	public void demarrer() {
		serveur.start();
		
		synchronized(attenteConnexion) {
			try {
				attenteConnexion.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Serveur : fin de la connexion");
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
