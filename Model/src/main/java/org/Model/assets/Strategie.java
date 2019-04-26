package org.Model.assets;


import java.util.HashMap;
import java.util.Map;

public class Strategie {
    private String premierType;
    private String deuxiemeType;
    private String troisiemeType;

    public Strategie(String premierType, String deuxiemeType, String troisiemeType){
        this.premierType = premierType;
        this.deuxiemeType = deuxiemeType;
        this.troisiemeType = troisiemeType;
    }

    public String getPremierType() {
        return premierType;
    }

    public void setPremierType(String premierType) {
        this.premierType = premierType;
    }

    public String getDeuxiemeType() {
        return deuxiemeType;
    }

    public void setDeuxiemeType(String deuxiemeType) {
        this.deuxiemeType = deuxiemeType;
    }

    public String getTroisiemeType() {
        return troisiemeType;
    }

    public void setTroisiemeType(String troisiemeType) {
        this.troisiemeType = troisiemeType;
    }



    public Action stratBot(Map<Carte,Integer> playableList, Joueur j) {
        Map<String, Integer> ressources = j.getRessources();
        Carte carte;
        int coutAction = 0;

        if (playableList.isEmpty()) {
            Map<String, Integer> coutMerveille = j.getMerveille().getressourceEtapeCourante();
            boolean isCreatable = true;

            for (Map.Entry<String, Integer> entry : coutMerveille.entrySet()) {
                String key = entry.getKey();
                Integer ressourceMerveille = entry.getValue();
                Integer ressourceJoueur = ressources.get(key);
                if (ressourceJoueur < ressourceMerveille) {
                    isCreatable = false;
                }
            }

            if (isCreatable) {
                return new Action(j.getM().get(0), "Etape Merveille",coutAction);
            }
            else {
                return new Action(j.getM().get(0), "Carte Défaussée",0);
            }
        }

        else {
            for (Map.Entry<Carte,Integer> entry : playableList.entrySet()) {
                carte = entry.getKey();
                coutAction = entry.getValue();
                if (carte.getType().equals(premierType)) {
                    if(carte.getCout().get("pièces") != null){
                        if (carte.getCout().get("pièces") != coutAction){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                    else{
                        if (coutAction != 0){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                }
            }
            for (Map.Entry<Carte,Integer> entry : playableList.entrySet()) {
                carte = entry.getKey();
                coutAction = entry.getValue();
                if (carte.getType().equals(deuxiemeType)) {
                    if(carte.getCout().get("pièces") != null){
                        if (carte.getCout().get("pièces") != coutAction){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                    else{
                        if (coutAction != 0){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                }
            }
            for (Map.Entry<Carte,Integer> entry : playableList.entrySet()) {
                carte = entry.getKey();
                coutAction = entry.getValue();
                if (carte.getType().equals(troisiemeType)) {
                    if(carte.getCout().get("pièces") != null){
                        if (carte.getCout().get("pièces") != coutAction){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                    else{
                        if (coutAction != 0){
                            return new Action(carte, "Carte Jouée avec commerce",coutAction);
                        }
                        else{
                            return new Action(carte, "Carte Jouée ",coutAction);
                        }
                    }
                }
            }
        }
        return new Action(j.getM().get(0), "Défaussée Carte",coutAction);
    }


    public Map<Carte,Integer> listCartePlayable(Map<String,Integer> ressourcesVoisinsList, Joueur j) {
        Carte carte = null;
        Map<Carte, Integer> isPlayableList = new HashMap<>();
        Map<String, Integer> ressources = j.getRessources();
        boolean isTradeUsed = false;


        //on itère sur les cartes
        for (int i = 0; i < j.getM().getMain().size(); i++) {
            Map<String, Integer> cout = j.getM().getMain().get(i).getCout();
            isTradeUsed = false;
            boolean isPlayable = true;
            boolean isTradeable;

            int pièces = j.getPièces();
            //on itère sur les coûts des cartes et on compare avec les ressources du bot
            for (Map.Entry<String, Integer> entry : cout.entrySet()) {
                String key = entry.getKey();
                Integer ressourceCarte = entry.getValue();
                Integer ressourceJoueur = ressources.get(key);
                //si le bot n'a pas assez de ressources, on regarde les ressources voisine
                if (ressourceCarte > ressourceJoueur) {
                    isTradeable = false;
                    // Si les voisins disposent de la ressource
                    if (ressourcesVoisinsList.get(key) != null) {
                        // Si les voisins peuvent combler le besoin et que le bot est en mesure de payer
                        if ((ressourceCarte <= ressourceJoueur + ressourcesVoisinsList.get(key)) && ((ressourceCarte - ressourceJoueur) * 2 <= pièces)) {
                            // On diminue le stock de pièce crée spécialement pour l'itération sur les ressources.
                            pièces -= (ressourceCarte - ressourceJoueur) * 2;
                            // On déclare que la ressource sera compensée grâce au commerce
                            isTradeable = true;
                            // On déclarer que le commerce a été utilisé, dans le cas où la carte est finalement jouable.
                            isTradeUsed = true;
                        }
                    }
                    // Si la ressource n'est pas compensée, la carte est injouable
                    if (!isTradeable) {
                        isPlayable = false;
                    }
                }
            }
            // Si isPlayable est true à ce moment là, les ressources du joueur ont été comparées à toutes les ressources
            // nécessaires pour jouer la carte et le résultat est positif.
            if (isPlayable) {
                carte = j.getM().get(i);
                // Si le commerce a été utilisé, le bot vérifie qu'il est en mesure de payer le prix du commerce + le prix de la carte
                // si elle a un coût en pièces.
                if (isTradeUsed) {
                    if (carte.getCout().get("pièces") != null) {
                        if (j.getPièces() >= (j.getPièces() - pièces) + carte.getCout().get("pièces")) {
                            isPlayableList.put(carte, (j.getPièces() - pièces) + carte.getCout().get("pièces"));
                        }
                    } else {
                        if (j.getPièces() >= j.getPièces() - pièces) {
                            isPlayableList.put(carte, j.getPièces() - pièces);
                        }
                    }
                } else {
                    if (carte.getCout().get("pièces") != null) {
                        isPlayableList.put(carte, carte.getCout().get("pièces"));
                    } else {
                        isPlayableList.put(carte, 0);
                    }
                }
            }
        }
        return isPlayableList;
    }
}
