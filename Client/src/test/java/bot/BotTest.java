package bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.socket.client.Socket;
import org.Client.Client;
import org.Model.assets.Carte;
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
    void constructeurEtCreationMain() {

        b = new Bot("bot_1", c);


        JSONArray j = new JSONArray();

        Map<String,String> effet = new HashMap<String,String>();
        Map<String,Integer> ressources = new HashMap<String,Integer>();
        effet = new HashMap<String,String>();
        effet.put("nomEffet", "gain_boucliers");
        effet.put("valeurEffet", "2");

        ressources = new HashMap<String,Integer>();
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
        JSONObject cJSON1 = null;
        JSONObject cJSON2 = null;
        JSONObject cJSON3 = null;


        try {
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

        b.setMain(j);
    }


    @Test
    void defausserDerniereCarteEtSetMainTest(){
        assertEquals(7,b.getJ().getM().size(),"Il y'a normalement maintenant 7 cartes dans la main du joueur.");


        for(int i = 0; i < 7; i++){
            b.defausserDerniereCarte(socket);
            assertEquals(6 - i,b.getJ().getM().size(),"Il y'a normalement maintenant" + (6 - i) +  "cartes dans la main du joueur.");

        }
    }

    @Test
    void jouerTourTest(){
        doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(2);
                assertEquals("Ecuries",j.getString("nom"),"Le nom de la carte jouée devrait être 'Ecuries'.");
                return null;
            }
        }).when(c).emit(eq(socket),eq("Carte Jouée"),any(JSONObject.class));

       doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(2);
                assertEquals("Officine",j.getString("nom"),"Le nom de la carte défaussé devrait être 'Officine'.");
                return null;
            }
        }).when(c).emit(eq(socket),eq("Carte Défaussée"),any(JSONObject.class));

        doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JSONObject j = invocation.getArgument(2);
                assertEquals("Officine",j.getString("nom"),"Le nom de la carte défaussé devrait être 'Officine'.");
                return null;
            }
        }).when(c).emit(eq(socket),eq("Carte Défaussée"),any(JSONObject.class));


        b.jouerTour(socket);

        Map<String,Integer> ressources = new HashMap<String,Integer>();


        ressources = new HashMap<String,Integer>();
        ressources.put("minerai",1);
        ressources.put("bois",1);
        ressources.put("argile",1);

        b.getJ().ajouterRessources(ressources);

        b.jouerTour(socket);
    }
}
