package org.Launcher;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.Client.Client;
import org.Model.tools.GestionPersistance;
import org.Serveur.Serveur;


public class Launcher
{
	
	public Launcher() {}
    public static void main( String[] args )
    {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        

        if(!GestionPersistance.isData()) {
        	System.out.println("Launcher : génération des données...");
        	GestionPersistance.generateData();
        }
        
        // Serveur
        
        Thread serveur = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
		        Serveur.main(null);
			} 
        	
        });
        
        // Bots
        
        Thread bot_1 = new Thread(new Runnable() {

			@Override
			public void run() {
				Client.main(new String[] {"bot_1"});
			}
        	
        });
        
        Thread bot_2 = new Thread(new Runnable() {

			@Override
			public void run() {
				Client.main(new String[] {"bot_2"});
			}
        	
        });
        
        Thread bot_3 = new Thread(new Runnable() {

			@Override
			public void run() {
				Client.main(new String[] {"bot_3"});
			}
        	
        });
        
        Thread bot_4 = new Thread(new Runnable() {
			@Override
			public void run() {
				Client.main(new String[] {"bot_4"});
			}        	
        });

        Thread bot_5 = new Thread(new Runnable() {

            @Override
            public void run() {
                Client.main(new String[] {"bot_5"});
            }

        });

        Thread bot_6 = new Thread(new Runnable() {

            @Override
            public void run() {
                Client.main(new String[] {"bot_6"});
            }

        });

        Thread bot_7 = new Thread(new Runnable() {
            @Override
            public void run() {
                Client.main(new String[] {"bot_7"});
            }
        });
        

        serveur.start();
        bot_1.start();
        bot_2.start();
        bot_3.start();
        bot_4.start();
        bot_5.start();
        bot_6.start();
        bot_7.start();
        
    }
}
