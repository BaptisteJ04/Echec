package projet.utils;

import java.util.*;
import projet.models.ChessBoard;
import projet.models.JSONReader;
import projet.all_pieces.*;

/*##################################### Classe Chess #####################################
Classe principale pour gérer le déroulement d’une partie d’échecs (console) :
- Gère les différents modes de jeu : classique ou personnalisé, avec ou sans IA
- Permet l’ajout de pièces personnalisées depuis des fichiers JSON
- Contrôle le déroulement complet de la partie, tour par tour
*/
public class Chess {

	private final Scanner scanner = new Scanner(System.in);

	/**
	 * Constructeur Chess() Initialise le scanner et affiche un message de
	 * bienvenue.
	 */
	public Chess() {
		System.out.println("Jeu d'échecs initialisé.");
	}

	/**
	 * lancer(ModeJeu mode) Lance une partie selon le mode choisi : 
	 * - Crée un plateau personnalisé si besoin 
	 * - Lance la boucle de jeu avec ou sans IA
	 */
	public void lancer(ModeJeu mode) {
		ChessBoard board;

		if (mode == ModeJeu.PERSONNALISE_VS_JOUEUR || mode == ModeJeu.PERSONNALISE_VS_IA) {
			board = creerPlateauPersonnalise();
		} else {
			board = new ChessBoard(); // Plateau standard
		}

		boolean vsIA = (mode == ModeJeu.CLASSIQUE_VS_IA || mode == ModeJeu.PERSONNALISE_VS_IA);
		jouerPartie(board, vsIA);
	}

	/**
	 * creerPlateauPersonnalise() Permet à chaque joueur d'ajouter des pièces
	 * personnalisées via des fichiers JSON. 
	 * - Le joueur entre les coordonnées et le nom du fichier JSON
	 * - Validation de la zone de camp et de la présence d'un roi
	 * - Retourne un plateau personnalisé prêt à jouer
	 */
	private ChessBoard creerPlateauPersonnalise() {
		ChessBoard board = new ChessBoard();

		for (int player = 0; player <= 1; player++) {
			System.out.println("\n=== Joueur " + (player + 1) + " : Ajoutez vos pièces personnalisées ===");

			boolean ajouter = true;
			while (ajouter) {
				try {
					System.out.print("Position (ex: D2) : ");
					String pos = scanner.nextLine().trim().toUpperCase();

					int[] coords = Piece.parseCoordonnees(pos);
					if ((player == 0 && coords[1] > 4) || (player == 1 && coords[1] < 5)) {
						System.out.println("❌ Hors de votre camp !");
						continue;
					}

					if (board.getPieceAt(coords[0], coords[1]) instanceof Roi) {
						System.out.println("❌ Impossible d’écraser un roi !");
						continue;
					}

					System.out.print("Fichier JSON de la pièce (sans .json) : ");
					String file = scanner.nextLine().trim();
					String chemin = "./src/pieces/" + file + ".json";

					JSONReader.chargerPiecePersonnalisee(pos, player, chemin, board);
					System.out.println("✅ Pièce ajoutée à " + pos);

				} catch (Exception e) {
					System.out.println("❌ Erreur : " + e.getMessage());
				}

				System.out.print("Ajouter une autre pièce ? (o/n) : ");
				ajouter = scanner.nextLine().trim().equalsIgnoreCase("o");
			}
		}
		return board;
	}

	/*
	 * jouerPartie(ChessBoard board, boolean vsIA) Boucle principale du jeu : 
	 * - Affiche l’échiquier à chaque tour 
	 * - Gère les entrées utilisateur ou les coups de l’IA
	 * - Vérifie les coups valides, les promotions et les échecs
	 */
	private void jouerPartie(ChessBoard board, boolean vsIA) {
		Affichage affichage = new Affichage(board);
		int currentPlayer = 0;
		MinMax ia = vsIA ? new MinMax(1) : null;

		while (true) {
			affichage.afficher();

			// Tour de l’IA
			if (vsIA && currentPlayer == 1) {
				System.out.println("🤖 L’IA (noir) joue...");
				String[] coup = ia.meilleurCoup(board);
				if (coup == null) {
					System.out.println("L’IA ne peut pas jouer. Fin de la partie.");
					break;
				}

				board.deplacer(coup[0], coup[1], 1);
				System.out.println("L’IA a joué : " + coup[0] + " -> " + coup[1]);
			} else {
				// Tour d’un joueur
				System.out.println("\nJoueur " + (currentPlayer == 0 ? "Blanc" : "Noir"));
				System.out.print("Entrez un coup (ex: E2 E4) ou 'exit' pour quitter : ");
				String input = scanner.nextLine().trim();

				if (input.equalsIgnoreCase("exit"))
					break;

				String[] move = input.split(" ");
				if (move.length != 2) {
					System.out.println("Format invalide.");
					continue;
				}

				String from = move[0].toUpperCase();
				String to = move[1].toUpperCase();

				int result = board.deplacer(from, to, currentPlayer);
				if (result == -1) {
					System.out.println("❌ Coup invalide.");
					continue;
				}

				// Promotion
				if (result == 1) {
					affichage.handlePromotion(to, scanner);
				}

				// Échec
				int adversaire = (currentPlayer + 1) % 2;
				if (board.isKingInCheck(adversaire)) {
					System.out.println("⚠️  Échec au roi " + (adversaire == 0 ? "blanc" : "noir") + " !");
				}
			}

			currentPlayer = (currentPlayer + 1) % 2;
		}

		System.out.println("🎯 Fin de la partie !");
	}
}
