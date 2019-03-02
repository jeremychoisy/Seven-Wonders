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
			HashMap<String, Integer> ressources = j.GetRessources();

			//on itère sur les coûts des cartes et on compare avec les ressources du bot https://stackoverflow.com/a/30906661
			for (Map.Entry<String,Integer> entry1 : cout.entrySet()){
				String key = entry1.getKey();
				Integer value1 = entry1.getValue();
				Integer value2 = ressources.get(key);
				//si le bot a assez de ressources il joue cette carte
				if (value2>=value1){
					c = j.getM().getMain().get(i);
					j.getM().remove(i); // pour remove de la main la carte (c) jouée
					break;
				}
			}
		}





		try {
			carteJouéeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(c));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.emit("Carte Jouée", carteJouéeJSON);
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
