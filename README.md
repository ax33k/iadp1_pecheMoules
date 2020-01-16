# IA Pêche Moules (profondeur)
Intelligence Artificielle réalisé dans le cadre d'un TP du module d'IA.
Serveur de jeu (fournit par les enseignants): http://iic0e.univ-littoral.fr/moodle/mod/resource/view.php?id=12366
## Compiler
Pour compiler le client, 

    javac *.java
Classes modifiées:

 - Client.java
 - Case.java
 
 Classes requises pour la bonne exécution de l'IA:
 
 - Client.java
 - Labyrinthe.java
 - Client.java
 - Joueur.java

  ## Lancer le client
  Pour lancer le client, *le serveur doit évidemment être lancée*
 
    java Client <ip du serveur> <port du serveur> IA_Lebas
   Exemple:

    java Client 127.0.0.1 1337 IA_Lebas
## Algorithme utilisé
Algorithme de parcours en profondeur (**récursif**).
