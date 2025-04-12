# ♟️ Jeu d'Échecs Personnalisé en Java

Bienvenue dans un projet de jeu d’échecs en Java avec une **interface graphique**, un **mode terminal** et la possibilité d'ajouter des **pièces personnalisées** via des fichiers JSON.  
Vous pouvez jouer avec les règles classiques ou inventer vos propres pièces !

## 🧑‍💻 Auteur

  - Baptiste Joubert

---

## 🧠 Fonctionnalités

- ✅ Jeu d’échecs en **mode console** ou **mode graphique (Swing GUI)**
- ♟️ Système de pièces **personnalisées** via des fichiers JSON
- 👑 Pièces de base : Roi, Reine, Cavalier, Tour, Fou, Pion
- 🎯 Prise en compte de la promotion, du tour par tour, des échecs
- 🚨 Bouton de **forfait**, suivi du **score** en GUI
- 🔁 Relancer une partie après un forfait en mode personnalisé GUI

---

## ▶️ Lancer le projet

### ✅ Prérequis
- Java 8 ou plus
- [org.json](https://mvnrepository.com/artifact/org.json/json) (pour lire les fichiers JSON)

### 🛠️ Compilation manuelle
```bash
javac -d bin src/projet/**/*.java
java -cp bin projet.Main
```
---

## 🎮 Choisissez votre mode :

    Mode Classique terminal (pièces de base)

    Mode Personnalisé terminal (ajoutez vos pièces)

    Mode Classique GUI

    Mode Personnalisé GUI

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
  "unicodeWhite": "♛",
  "unicodeBlack": "♕",
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

🧪 Testez-la en choisissant le mode GUI personnalisé, entrez la position (ex: D2), puis tapez le nom du fichier (ex: Dame).

---

## 📦 Librairies utilisées

✅ Seule dépendance externe requise, pour le parsing JSON, j'ai utilisé `org.json`

---

## 🌈 Idées de pièces personnalisées

    🐉 Un dragon qui vole sur les cases horizontales et verticales

    👣 Un fantôme qui peut sauter par-dessus toutes les pièces

Tu es libre de créer tes propres règles via les vecteurs de mouvements dans le JSON !
