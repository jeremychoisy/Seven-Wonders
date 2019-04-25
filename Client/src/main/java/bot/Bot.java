package bot;

import org.Client.Client;
import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Merveille;
import org.Model.tools.GestionPersistance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import org.Model.assets.*;
public class Bot {
	private Client c;
	private Joueur j;

	public Bot(String name, Client c) {
		this.j = new Joueur(name);
		this.c = c;
	}

	public void setMain(JSONArray Json) {
		j.setM(new Main()); 
		for(int i=0;i<Json.length();i++) {
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

	@SuppressWarnings("Duplicates")
	public void defausserDerniereCarte() {
		JSONObject carteDéfausséeJSON = null;
		try {
			carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString( j.getM().get(0)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		j.getM().remove(0);
		c.emit("Carte Défaussée", carteDéfausséeJSON);
	}

	@SuppressWarnings("Duplicates")
	public void jouerTour(Map<String,Integer> ressourcesVoisinsList) {
        JSONObject carteJouéeJSON = null;
        JSONObject carteDéfausséeJSON = null;
        Strategie strategieMilitaire = new Strategie("Conflit Militaire","Batiment Commercial","Bâtiment Scientifique");
        Action action = strategieMilitaire.stratBot(strategieMilitaire.listCartePlayable(ressourcesVoisinsList,j),j);
        int indexCarte = j.getM().getMain().indexOf(action.getCarte());


        if (action.getType().equals("Etape Merveille") || action.getType().equals("Carte Défaussée")){
            try {
                carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(action.getCarte()));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            if (action.getType().equals("Etape Merveille")){
                j.getM().remove(indexCarte);
                j.getMerveille().etapeSuivante(j);
                c.emit("Etape Merveille", carteDéfausséeJSON);
            }
            else {
                j.getM().remove(indexCarte);
                j.substractPièces(action.getCout());
                c.emit("Carte Défaussée", carteDéfausséeJSON);
            }
        }
        else{
            try {
                carteJouéeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(action.getCarte()));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(action.getType().equals("Carte Jouée avec commerce")){
                j.substractPièces(action.getCout());
                j.getM().remove(indexCarte);
                c.emit("Carte Jouée avec commerce", carteJouéeJSON);
            }
            else{
                j.getM().remove(indexCarte);
                c.emit("Carte Jouée", carteJouéeJSON);
            }
        }

	}



	public Joueur getJ() {
		return j;
	}

	public void setJ(Joueur j) {
		this.j = j;
	}





}
