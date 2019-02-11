package org.Launcher;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.Client.Client;
import org.Serveur.Serveur;


public class Launcher
{
    public static void main( String[] args )
    {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Thread serveur = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
		        Serveur.main(null);
			}
        	
        });
        
        Thread client = new Thread(new Runnable() {

			@Override
			public void run() {
				Client.main(null);
			}
        	
        });
        
        

        serveur.start();
        client.start();
    }

}