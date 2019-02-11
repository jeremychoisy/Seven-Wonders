package org.Serveur;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.Model.Carte;
import org.Model.Id;
import org.Model.MyPrintStream;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

public class Serveur {
	private SocketIOServer serveur;
	private final Object attenteConnexion = new Object(); 
	private Id joueurID;

	public Serveur(Configuration config) {
		serveur = new SocketIOServer(config);
		System.out.println("******************");
		System.out.println("Début de la partie");
		System.out.println("******************");
		System.out.println("Serveur : Attente de connexion du client");
		serveur.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient socketIOClient) {
				System.out.println("Serveur : Connexion de " + socketIOClient.getRemoteAddress());
			}	
		});
		
		serveur.addEventListener("id", Id.class, new DataListener<Id>(){

			@Override
			public void onData(SocketIOClient client, Id data, AckRequest ackSender) throws Exception {
				System.out.println("Server : le client " + client.getRemoteAddress() + " s'est identifié en tant que : " + data.getNom());
				joueurID = new Id(data.getNom());
				Carte c = new Carte("Marché","rouge");
				client.sendEvent("Carte",c);
			}
			
		});
		
		serveur.addEventListener("reponse", Carte.class, new DataListener<Carte>(){

			@Override
			public void onData(SocketIOClient client, Carte data, AckRequest ackSender) throws Exception {
				System.out.println("Server : la carte jouée par " + joueurID.getNom() + " est : " + data.getNom());
				synchronized(attenteConnexion) {
					attenteConnexion.notify();
				}
				
			}
			
		});
		
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
        
        Serveur serveur = new Serveur(config);
        serveur.demarrer();
	}
	
}
