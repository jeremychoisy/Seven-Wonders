package bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.socket.client.Socket;
import org.Client.Client;
import org.Model.assets.Carte;
import org.Model.assets.Merveille;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;


import java.util.HashMap;
import java.util.Map;


@ExtendWith(MockitoExtension.class)

public class BotTest {

    Bot b;

    @Mock
    Socket socket;

    @Mock
    Client c;

    @BeforeEach
    @Test
    void constructeurEtCreationMain() {

        b = new Bot("bot_1", c);


        JSONArray j = new JSONArray();

        Map<String,Integer> ressourceEtapeUne = new HashMap<String,Integer>();
        Map<String,Integer> ressourceEtapeDeux = new HashMap<String,Integer>();
        Map<String,Integer> ressourceEtapeTrois = new HashMap<String,Integer>();

        Map<String,String> effetEtapeUne = new HashMap<String,String>();
        Map<String,String> effetEtapeDeux = new HashMap<String,String>();
        Map<String,String> effetEtapeTrois = new HashMap<String,String>();

        ressourceEtapeUne.put("pierre",2);
        ressourceEtapeDeux.put("bois",3);
        ressourceEtapeTrois.put("pierre",4);

        effetEtapeUne.put("nomEffet", "gain_pointsVictoire");
        effetEtapeUne.put("valeurEffet", "3");
        effetEtapeDeux.put("nomEffet", "gain_pointsVictoire");
        effetEtapeDeux.put("valeurEffet", "5");
        effetEtapeTrois.put("nomEffet", "gain_pointsVictoire");
        effetEtapeTrois.put("valeurEffet", "7");

        Merveille m = new Merveille("Gizah","pierre",ressourceEtapeUne,ressourceEtapeDeux,ressourceEtapeTrois, effetEtapeUne, effetEtapeDeux, effetEtapeTrois);


        Map<String,String> effet = new HashMap<String,String>();
        Map<String,Integer> ressources = new HashMap<String,Integer>();

        effet.put("nomEffet", "gain_boucliers");
        effet.put("valeurEffet", "2");

        ressources.put("minerai",1);
        ressources.put("bois",1);
        ressources.put("argile",1);

        Carte c1 = new Carte("Ecuries","Conflit Militaire",effet,ressources, 5,2);

        effet = new HashMap<String,String>();
        effet.put("nomEffet","gain_symboles");
        effet.put("symboleEffet","science");

        ressources = new HashMap<String,Integer>();
        ressources.put("tissu",1);

        Carte c2 = new Carte("Officine","Bâtiment Scientifique",effet,ressources,3,1);

        effet = new HashMap<String,String>();
        effet.put("nomEffet","gain_symboles");
        effet.put("symboleEffet","ingénieur");

        ressources = new HashMap<String,Integer>();
        ressources.put("verre",1);

        Carte c3 = new Carte("Atelier","Bâtiment Scientifique",effet,ressources,7,1);

        ObjectMapper o = new ObjectMapper();
        JSONObject mJSON = null;
        JSONObject cJSON1 = null;
        JSONObject cJSON2 = null;
        JSONObject cJSON3 = null;


        try {
            mJSON = new JSONObject(o.writeValueAsString(m));
            cJSON1 = new JSONObject(o.writeValueAsString(c1));
            cJSON2 = new JSONObject(o.writeValueAsString(c2));
            cJSON3 = new JSONObject(o.writeValueAsString(c3));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            j.put(0,cJSON2);
            j.put(1,cJSON3);

            for(int i=2;i<7;i++) {
                j.put(i,cJSON1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        b.setMerveille(mJSON);
        b.setMain(j);

        assertEquals(7,b.getJ().getM().size(),"Il y'a normalement maintenant 7 cartes dans la main du joueur.");
        assertEquals("Gizah",b.getJ().getMerveille().getNom(),"Le joueur dispose normalement maintenant de la merveille de Gizah.");


    }


    @Test
    void defausserDerniereCarteTest(){


        for(int i = 0; i < 7; i++){
            b.defausserDerniereCarte();
            assertEquals(6 - i,b.getJ().getM().size(),"Il y'a normalement maintenant" + (6 - i) +  "cartes dans la main du joueur.");

        }
    }

    @Test
    void jouerTourTest(){
        doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(1);
                assertEquals("Ecuries",j.getString("nom"),"Le nom de la carte jouée devrait être 'Ecuries'.");
                return null;
            }
        }).when(c).emit(eq("Carte Jouée"),any(JSONObject.class));

       doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(1);
                assertEquals("Officine",j.getString("nom"),"Le nom de la carte défaussé devrait être 'Officine'.");
                return null;
            }
        }).when(c).emit(eq("Carte Défaussée"),any(JSONObject.class));

        doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(1);
                assertEquals(1, b.getJ().getMerveille().getEtapeCourante(), "L'étape courante de la merveille devrait maintenant être 1.");
                assertEquals("Atelier",j.getString("nom"),"Le nom de la carte défaussé devrait être 'Atelier'.");
                return null;
            }
        }).when(c).emit(eq("Etape Merveille"),any(JSONObject.class));

        Map<String,Integer> ressources = new HashMap<String,Integer>();
        ressources.put("bois",2);

        b.jouerTour(ressources);


        ressources = new HashMap<String,Integer>();
        ressources.put("pierre",2);

        b.getJ().ajouterRessources(ressources);


        b.jouerTour(ressources);


        ressources = new HashMap<String,Integer>();
        ressources.put("minerai",1);
        ressources.put("bois",1);
        ressources.put("argile",1);

        b.getJ().ajouterRessources(ressources);

        b.jouerTour(ressources);
    }
}
