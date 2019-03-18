package bot;

import org.Client.Client;
import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Merveille;
import org.Model.tools.GestionPersistance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

import java.util.Map;

public class Bot {
	private Client c;
	private Joueur j;
	public Bot(String name, Client c) {
		this.j = new Joueur(name);
		this.c = c;
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

	public void defausserDerniereCarte(Socket s) {
		JSONObject carteDéfausséeJSON = null;
		try {
			carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString( j.getM().get(0)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		j.getM().remove(0);
		c.emit(s,"Carte Défaussée", carteDéfausséeJSON);
	}

	public void jouerTour(Socket s) {
		Carte carte = null;
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
				carte = j.getM().get(i);
				if(carte.getCout().get("pièces") != null) {
					j.setPièces(j.getPièces() - carte.getCout().get("pièces"));
				}
				j.getM().remove(i); // pour remove de la main la carte (c) jouée
				break;
			}

		}


		// On vérifie que le bot est capable de jouer une carte, si ce n'est pas le cas, on défausse la première.
		if( carte == null) {

			Map<String, Integer> cout = j.getM().getMain().get(i).getCout();

			boolean isCreable = true;

			try {
				carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString( j.getM().get(0)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j.getM().remove(0);
			c.emit(s,"Carte Défaussée", carteDéfausséeJSON);
		}
		else
		{
			try {
				carteJouéeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(carte));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.emit(s,"Carte Jouée", carteJouéeJSON);
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
