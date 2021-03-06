# Seven Wonders


![alt text](./Documentation/logo-7-wonders.png)
Projet de développement en Java dans le cadre de l'enseignement dispensé en L3 MIAGE par Mr Renevier.

Il s'agit de créer une version électronique du jeu de plateau 7 Wonders avec 2 joueurs robots intelligents et 1 "random".

## Avant de commencer

Avant de lancer le jeu. Vous devez installer votre environnement de travail.

Téléchargez Apache Maven depuis le site officiel (les checksums sont aussi fournis sur la page de téléchargement) : [Maven](https://maven.apache.org/download.cgi)

Ensuite vous pouvez suivre ce magnifique [tutoriel](https://openclassrooms.com/fr/courses/4503526-organisez-et-packagez-une-application-java-avec-apache-maven/4608805-configurez-votre-environnement-de-developpement) pour installer Maven. 



### Installation

Depuis votre terminal, de commande déplacez vous dans le répertoire où se trouve le jeu et suivez cette procédure.


```
mvn install
```




## Lancement des tests

Toujours depuis le terminal et en étant dans le répertoire du Launcher.

```
mvn exec:java@partie
```
Pour l'exécution d'une partie avec logs.
```
mvn exec:java@stat100
```
Pour l'exécution de 100 parties sans logs avec statistiques à la fin.
```
mvn exec:java@stat200
```
Pour l'exécution de 200 parties sans logs avec statistiques à la fin.

```
mvn exec:java@stat500
```
Pour l'exécution de 500 parties sans logs avec statistiques à la fin.



## Built With

* [Maven](https://maven.apache.org/) - Dépendance Fonctionnelle


## Auteurs

* **Jérémy Choisy** - *Chef de Projet* - [LEPLUFOR](https://github.com/jeremychoisy)

Voici la liste des contributeurs [contributeurs](https://github.com/jeremychoisy/Seven-Wonders/graphs/contributors) qui ont participé au projet.


## Connaissances

* Une connaissance parfaite des Règles du jeu 7Wonders
* Des heures de pure plaisir à jouer au jeu de plateau


