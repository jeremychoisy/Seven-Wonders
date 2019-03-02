package org.Model.assets;

import org.Model.tools.GestionPersistance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

import java.util.HashMap;
import java.util.Map;

public class Bot {
	private Joueur j;
	
	public Bot(String name) {
		this.j = new Joueur(name);
	}
	
	public void setMain(JSONArray Json) {
	    for(int i=0;i<7;i++) {
	    	Carte c = null;
			try {
				c = new Carte(Json.getJSONObject(i).getString("nom"),Json.getJSONObject(i).getString("type"),GestionPersistance.JSONToMapEffet((JSONObject)Json.getJSONObject(i).get("effet")),GestionPersistance.JSONToMapRessource((JSONObject)Json.getJSONObject(i).get("cout")),Json.getJSONObject(i).getInt("configurationNumber"),Json.getJSONObject(i).getInt("age"));
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j.getM().add(c);
	    }
	}
	
	public void setMerveille(JSONObject Json) {
		Merveille m = null;
		try {
			m = new Merveille(Json.getString("nom"),Json.getString("ressource"),GestionPersistance.JSONToMapRessource(Json.getJSONObject("ressourceEtapeUne")),
					GestionPersistance.JSONToMapRessource(Json.getJSONObject("ressourceEtapeDeux")),GestionPersistance.JSONToMapRessource(Json.getJSONObject("ressourceEtapeTrois")),
					GestionPersistance.JSONToMapEffet(Json.getJSONObject("effetEtapeUne")),GestionPersistance.JSONToMapEffet(Json.getJSONObject("effetEtapeDeux")),
					GestionPersistance.JSONToMapEffet(Json.getJSONObject("effetEtapeTrois")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		j.setMerveille(m);	
	}
	
	public void jouerTour(Socket s) {
		Carte c = null;
		JSONObject carteJouéeJSON=null;
		JSONObject carteDéfausséeJSON=null;
		//là le bot choisit la carte à jouer selon les ressources nécessaires et les res dont il dispose

		/*
		il faut se servir de getQuantitéRessource(String nomRessource) de la classe joueur
		et de getCout() de la classe carte pour comparer les ressources dispo / needed

		ressources de joueur : HashMap<String,Integer>()

		cout de carte : Map<String, Integer>


		*/

		//on itère sur les cartes
		for(int i = 0; i < j.getM().getMain().size(); i++)
		{
			Map<String, Integer> cout = j.getM().getMain().get(i).getCout();
			Map<String, Integer> ressources = j.GetRessources();
			boolean isPlayable = true;
			
			//on itère sur les coûts des cartes et on compare avec les ressources du bot
			for (Map.Entry<String,Integer> entry : cout.entrySet()){
				String key = entry.getKey();
				Integer ressourceCarte = entry.getValue();
				Integer ressourceJoueur = ressources.get(key);
				//si le bot n'a pas assez de ressources, on met la variable isPlayable à false
				if (ressourceCarte > ressourceJoueur){
					isPlayable = false;
				}
			}
			// Si isPlayable est true à ce moment là, les ressources du joueur ont été comparées à toutes les ressources
			// nécessaires pour jouer la carte et le résultat est positif.
			if(isPlayable == true) {
				c = j.getM().get(i);
				if(c.getCout().get("pièces") != null) {
					j.setPièces(j.getPièces() - c.getCout().get("pièces"));
				}
				j.getM().remove(i); // pour remove de la main la carte (c) jouée
				break;
			}
		}
		

		// On vérifie que le bot est capable de jouer une carte, si ce n'est pas le cas, on défausse la première.
		if( c == null) {
			try {
				carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString( j.getM().get(0)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			s.emit("Carte Défaussée", carteDéfausséeJSON);
		}
		else
		{
			try {
				carteJouéeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(c));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.emit("Carte Jouée", carteJouéeJSON);
		}
	}

	public void addPièces(int valeur) {
		j.setPièces(j.getPièces() + valeur);
	}
	
	public void removePièces(int valeur) {
		if((j.getPièces() - valeur) > 0)
			j.setPièces(j.getPièces() - valeur);
		else
			j.setPièces(0);
	}
	
	public Joueur getJ() {
		return j;
	}

	public void setJ(Joueur j) {
		this.j = j;
	}
	
	



}
