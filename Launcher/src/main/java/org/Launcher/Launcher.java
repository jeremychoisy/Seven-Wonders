package org.Launcher;

import org.Client.Client;
import org.Model.tools.GestionPersistance;
import org.Serveur.Serveur;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;


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

        Thread serveur = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
		        Serveur.main(new String[]{args[0],args[1]});
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



        serveur.start();
        bot_1.start();
        bot_2.start();
        bot_3.start();
        bot_4.start();

    }
}
