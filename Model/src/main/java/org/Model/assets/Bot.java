package org.Model.assets;

import org.Model.tools.GestionPersistance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

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
	
		c = j.getM().getMain().get(0);
		j.getM().remove(0);

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
