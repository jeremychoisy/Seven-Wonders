package bot;

import org.Client.Client;
import org.Model.assets.Carte;
import org.Model.assets.Joueur;
import org.Model.assets.Merveille;
import org.Model.tools.GestionPersistance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	public void changeMain(JSONArray Json) {
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
		Carte carte = null;
		JSONObject carteJouéeJSON=null;
		JSONObject carteDéfausséeJSON=null;
		Map<String, Integer> ressources = j.getRessources();
        boolean isTradeUsed = false;
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
			isTradeUsed = false;
			boolean isPlayable = true;
			boolean isTradeable;

			int pièces = j.getPièces();
			//on itère sur les coûts des cartes et on compare avec les ressources du bot
			for (Map.Entry<String,Integer> entry : cout.entrySet()){
				String key = entry.getKey();
				Integer ressourceCarte = entry.getValue();
				Integer ressourceJoueur = ressources.get(key);
				//si le bot n'a pas assez de ressources, on regarde les ressources voisine
				if (ressourceCarte > ressourceJoueur){
				    isTradeable = false;
				    // Si les voisins disposent de la ressource
				    if(ressourcesVoisinsList.get(key) != null){
				        // Si les voisins peuvent combler le besoin et que le bot est en mesure de payer
				        if((ressourceCarte <= ressourceJoueur + ressourcesVoisinsList.get(key)) && ((ressourceCarte - ressourceJoueur) * 2 <= pièces)){
				            // On diminue le stock de pièce crée spécialement pour l'itération sur les ressources.
                            pièces -= (ressourceCarte - ressourceJoueur) * 2;
                            // On déclare que la ressource sera compensée grâce au commerce
                            isTradeable = true;
                            // On déclarer que le commerce a été utilisé, dans le cas où la carte est finalement jouable.
                            isTradeUsed = true;
                        }
                    }
				    // Si la ressource n'est pas compensée, la carte est injouable
				    if(!isTradeable){
				        isPlayable = false;
                    }
				}
			}
			// Si isPlayable est true à ce moment là, les ressources du joueur ont été comparées à toutes les ressources
			// nécessaires pour jouer la carte et le résultat est positif.
			if(isPlayable) {
                carte = j.getM().get(i);
                // Si le commerce a été utilisé, le bot vérifie qu'il est en mesure de payer le prix du commerce + le prix de la carte
                // si elle a un coût en pièces.
			    if(isTradeUsed){
                    if(carte.getCout().get("pièces") != null){
                        if(j.getPièces() >= (j.getPièces() - pièces) + carte.getCout().get("pièces")) {
                            carte = j.getM().get(i);
                            j.substractPièces((j.getPièces() - pièces) + carte.getCout().get("pièces"));
                            j.getM().remove(i); // pour remove de la main la carte (c) jouée
                            break;
                        }
                    } else {
                        if(j.getPièces() >= j.getPièces() - pièces) {
                            carte = j.getM().get(i);
                            j.substractPièces(j.getPièces() - pièces);
                            j.getM().remove(i); // pour remove de la main la carte (c) jouée
                            break;
                        }
                    }
                } else {
                    if(carte.getCout().get("pièces") != null) {
                        j.substractPièces(carte.getCout().get("pièces"));
                    }
                    j.getM().remove(i); // pour remove de la main la carte (c) jouée
                    break;
                }
			    // Si aucun break n'a été appelé, les conditions ne sont pas remplies, on remet carte à null et on continue à boucler sur la main.
			    carte = null;

			}
		}


		// On vérifie que le bot est capable de jouer une carte, si ce n'est pas le cas, on essaie de débloquer une étape de merveille sinon on défausse la première.
		if( carte == null) {

			Map<String, Integer> coutMerveille = j.getMerveille().getressourceEtapeCourante();
			boolean isCreatable = true;

			for (Map.Entry<String,Integer> entry : coutMerveille.entrySet()){
				String key = entry.getKey();
				Integer ressourceMerveille = entry.getValue();
				Integer ressourceJoueur = ressources.get(key);
				if (ressourceJoueur < ressourceMerveille){
					isCreatable = false;
				}
			}

			try {
				carteDéfausséeJSON = new JSONObject(GestionPersistance.ObjectToJSONString( j.getM().get(0)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			j.getM().remove(0);

			if(isCreatable){
				j.getMerveille().etapeSuivante(j);
				c.emit("Etape Merveille", carteDéfausséeJSON);
			}
			else {
				c.emit("Carte Défaussée", carteDéfausséeJSON);
			}
		}
		else
		{
			try {
				carteJouéeJSON = new JSONObject(GestionPersistance.ObjectToJSONString(carte));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isTradeUsed){
                c.emit("Carte Jouée avec commerce", carteJouéeJSON);
            } else {
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
