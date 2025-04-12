package projet.main;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import projet.models.ChessBoard;
import projet.utils.Affichage;
import projet.utils.ChessGUI;

public class Chess {
	
	public Chess() {
		System.out.println("Le jeux est initialisé");
		return;
	}

    public void game() {
        ChessBoard chessBoard = new ChessBoard(); // Création de l'échiquier
        Affichage display = new Affichage(chessBoard); // Création de l'affichage
        Scanner scanner = new Scanner(System.in);
        int currentPlayer = 0; // 0 = Blanc, 1 = Noir

        while (true) {
            display.afficher(); // Affichage de l’échiquier

            System.out.println("\nC'est au tour du joueur " + (currentPlayer == 0 ? "Blanc" : "Noir"));
            System.out.print("Entrez un coup (ex: E2 E4) ou 'exit' pour quitter: ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Le jeu est terminé.");
                break;
            }

            String[] move = input.trim().split(" ");

            if (move.length != 2) {
                System.out.println("Entrée invalide. Veuillez entrer un coup sous la forme: 'E2 E4'.");
                continue;
            }

            String from = move[0].toUpperCase();
            String to = move[1].toUpperCase();

            int result = chessBoard.deplacer(from, to, currentPlayer);

            if (result == 0 || result == 1) {
                // Promotion si besoin
                if (result == 1) {
                    display.handlePromotion(to, scanner);
                }

                // Vérifie si l'adversaire est en échec
                int opponent = (currentPlayer == 0) ? 1 : 0;
                if (chessBoard.isKingInCheck(opponent)) {
                    System.out.println("⚠️  Échec au roi " + (opponent == 0 ? "blanc" : "noir") + " !");
                }

                // Changement de joueur
                currentPlayer = opponent;
            } else if (result == -1) {
                System.out.println("Coup invalide. Essayez encore.");
            }
        }

        scanner.close();
    }
    public void gameGUI() {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
}
