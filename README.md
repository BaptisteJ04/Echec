# ♟️ Custom Chess Game in Java

![Java](https://img.shields.io/badge/Java-8%2B-orange) ![Maven](https://img.shields.io/badge/build-Maven-blue) ![MIT License](https://img.shields.io/badge/license-MIT-green)

A Java chess game with a **graphical interface**, a **terminal mode**, and support for **custom pieces** defined in JSON files.
Play with the classic rules or invent your own pieces!

Built as part of my Java coursework.

## 🧑‍💻 Author

  - Baptiste Joubert

---

## 🧠 Features

  - ✅ Chess in console mode or graphical mode (Swing GUI)

  - ♟️ Custom piece system driven by JSON files

  - 👑 Standard pieces: King, Queen, Knight, Rook, Bishop, Pawn

  - 🎯 Support for promotion, turn-based play and check

  - 🚨 Resign button and score tracking in the GUI

  - 🔁 Restart a game after a resignation in custom GUI mode

  - 🧠 MinMax-based AI that can play automatically

  - 🧪 Thorough JUnit unit tests to keep the project stable

---

## ▶️ Running the project with Maven

```bash
git clone <repo-url>
cd <repo-name>
```

### ✅ Prerequisites
- Java 8 or later
- [Maven](https://maven.apache.org/) installed (make sure the `mvn` command is available in your terminal)

---

### 📦 Building & testing with Maven

The project uses **Maven** to manage dependencies (`org.json` and JUnit) and to simplify compiling, running and packaging.

#### 🔨 Compile the project:

```bash
mvn compile
```

#### 🧪 Run the unit tests:

```bash
mvn test
```

#### 📦 Build the executable .jar:

```bash
mvn package
```

The .jar file is generated in the `target/` folder.
You can then run it with:

#### ▶️ Run the executable archive

```bash
java -jar target/echecs-1.0.jar
```
---

## 🎮 Choose your mode

  - Classic terminal mode (standard pieces)

  - Custom terminal mode (add your own pieces)

  - Classic GUI mode

  - Custom GUI mode

All modes can be played with 1 or 2 players.

---

## 🔧 Adding a custom piece

Custom pieces are `.json` files placed in the folder:

```bash
./src/main/resources/projet/pieces
```

### ✏️ JSON example (sentinelle.json)

```json
{
    "name": "Sentinelle",
    "unicodeWhite": "🛡️",
    "unicodeBlack": "🛡️",
    "imageWhite": "projet/white_guard.png",
    "imageBlack": "projet/black_guard.png",
    "movesWhite": [
        [1, 0], [-1, 0], [0, 1], [0, -1]
    ],
    "movesBlack": [
        [1, 0], [-1, 0], [0, 1], [0, -1]
    ],
    "capturesWhite": [],
    "capturesBlack": [],
    "deplacementLong": true,
    "peutSauter": false
}
```

## 🧪 Unit tests

The project is covered by JUnit unit tests:

  - Move validation

  - Detection of legal moves when in check

  - Board evaluation checks

There are no tests for the AI yet.


## 🤖 Artificial Intelligence (AI)

A MinMax-based AI is built in:

  - Picks the best possible move according to a custom board evaluation

  - Takes captures, checks, checkmate and threats into account

Play against the computer if you want a challenge!


---

## 📦 Libraries used

✅ The only external dependency is `org.json`, used for JSON parsing.

---

## 🎮 Game preview

Here is the graphical mode (GUI) with the pieces displayed on the board:

![Chess game preview](./echec_perso.png)


## 🌈 Custom piece ideas

    🐉 A dragon that flies along horizontal and vertical squares

    👣 A ghost that can jump over all pieces

You are free to create your own rules through the movement vectors in the JSON!

---

## 📄 License

Distributed under the [MIT](./LICENSE) license © Baptiste Joubert.
