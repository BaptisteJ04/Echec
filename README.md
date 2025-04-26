# ♟️ Jeu d'Échecs Personnalisé en Java

Bienvenue dans un projet de jeu d’échecs en Java avec une **interface graphique**, un **mode terminal** et la possibilité d'ajouter des **pièces personnalisées** via des fichiers JSON.  
Vous pouvez jouer avec les règles classiques ou inventer vos propres pièces !

## 🧑‍💻 Auteur

  - Baptiste Joubert

---

## 🧠 Fonctionnalités

  - ✅ Jeu d’échecs en mode console ou mode graphique (Swing GUI)

  -  ♟️ Système de pièces personnalisées via des fichiers JSON

  - 👑 Pièces de base : Roi, Reine, Cavalier, Tour, Fou, Pion

  -  🎯 Prise en compte de la promotion, du tour par tour, des échecs

  - 🚨 Bouton de forfait, suivi du score en GUI
  
  -  🔁 Relancer une partie après un forfait en mode personnalisé GUI

  - 🧠 Intelligence Artificielle basée sur MinMax pour jouer automatiquement

  - 🧪 Tests unitaires complets avec JUnit pour assurer la stabilité du projet

---

## ▶️ Lancer le projet

### ✅ Prérequis
- Java 8 ou plus
- [org.json](https://mvnrepository.com/artifact/org.json/json) (pour lire les fichiers JSON)

### 📦 Compilation (en ligne de commande)

```bash
  javac -d bin -cp lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar src/projet/**/*.java src/tests/**/*.java
```

### 🚀 Exécuter le projet principal (Main.java)

```bash
  java -cp bin projet.main.Main
```

### 🧪 Exécuter les tests JUnit

```bash
  java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore tests.allPieces.YourTestClass
```

Ou pour tout lancer (en créant un AllTests.java qui agrège tous les tests) :

```bash
java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore tests.AllTests
```

### 📦 Pour packager en .jar

  Depuis le répertoire principal :
```bash
  jar cfe monEchecs.jar projet.main.Main -C bin .
```

  Pour lancer ensuite :
```bash
  java -jar monEchecs.jar
```

---

## 🎮 Choisissez votre mode :

  -  Mode Classique terminal (pièces de base)

  -  Mode Personnalisé terminal (ajoutez vos pièces)

  -  Mode Classique GUI

  -  Mode Personnalisé GUI

Tout les modes peuvent être joués en 1 ou 2 joueurs

---

## 🔧 Ajouter une pièce personnalisée

Les pièces personnalisées sont des fichiers .json placés dans le dossier :

```bash
./src/pieces/
```

### ✏️ Exemple de JSON (Dame.json)

```json
{
  "name": "Dame",
  "unicodeWhite": "♕",
  "unicodeBlack": "♛",
  "imageWhite": "./src/Images/white_queen.png",
  "imageBlack": "./src/Images/black_queen.png",
  "movesWhite": [
    [1, 0], [-1, 0], [0, 1], [0, -1],
    [1, 1], [-1, -1], [1, -1], [-1, 1]
  ],
  "movesBlack": [
    [1, 0], [-1, 0], [0, 1], [0, -1],
    [1, 1], [-1, -1], [1, -1], [-1, 1]
  ],
  "capturesWhite": [
    [1, 0], [-1, 0], [0, 1], [0, -1],
    [1, 1], [-1, -1], [1, -1], [-1, 1]
  ],
  "capturesBlack": [
    [1, 0], [-1, 0], [0, 1], [0, -1],
    [1, 1], [-1, -1], [1, -1], [-1, 1]
  ],
  "deplacementLong": true,
  "peutSauter": false
}
```

## 🧪 Tests unitaires

Le projet est couvert par des tests unitaires JUnit :

  -  Validation des déplacements

  - Détection des coups légaux en cas d’échec

  - Vérification de l’évaluation des plateaux de jeu

  - Préférence stratégique (par exemple : capturer une Dame plutôt qu'un Pion, éviter les échanges désavantageux, privilégier un mat)

### Pour exécuter les tests :

```bash
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar -d bin src/projet/**/*.java src/tests/**/*.java
java -cp .:bin:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore projet.tests.AllTests
```

## 🤖 Intelligence Artificielle (IA)

Une IA basée sur l'algorithme MinMax est intégrée :

  - Choix du meilleur coup possible selon une évaluation heuristique du plateau

  - Prise en compte des captures, échecs, mat et menaces

Jouez contre l’ordinateur ou laissez l'IA s'affronter elle-même !


---

## 📦 Librairies utilisées

✅ Seule dépendance externe requise, pour le parsing JSON, j'ai utilisé `org.json`

---

## 🎮 Aperçu du jeu

Voici un aperçu du mode graphique (GUI) avec les pièces affichées sur l’échiquier :

![Aperçu du jeu d'échecs](./echec_perso.png)


## 🌈 Idées de pièces personnalisées

    🐉 Un dragon qui vole sur les cases horizontales et verticales

    👣 Un fantôme qui peut sauter par-dessus toutes les pièces

Tu es libre de créer tes propres règles via les vecteurs de mouvements dans le JSON !
