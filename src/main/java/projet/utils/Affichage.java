package projet.utils;

import java.util.Scanner;

import projet.all_pieces.Piece;
import projet.all_pieces.Pion;
import projet.models.ChessBoard;

/*##################################### Classe Affichage #####################################
Classe responsable de l’affichage de l’échiquier et des interactions visuelles :
- Affiche le plateau de jeu dans la console
- Gère la conversion des pièces en caractères Unicode
- Gère la promotion d’un pion lorsque celui-ci atteint la dernière rangée
*/
public class Affichage {
	
	private ChessBoard chessBoard;  // Référence à l'échiquier pour récupérer les pièces

    /**
     * Constructeur de la classe Affichage
     * @param chessBoard l’échiquier à afficher
     */
    public Affichage(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }
    
    /**
     * Affiche l’échiquier dans la console en format texte :
     * - Parcourt chaque case du plateau
     * - Affiche les pièces avec leurs caractères Unicode
     * - Affiche les coordonnées (chiffres & lettres)
     */
    public void afficher() {
        for (int y = 8; y >= 1; y--) {
            System.out.print(y + "|");
            for (int x = 1; x <= 8; x++) {
                Piece piece = chessBoard.getPieceAt(x, y);
                if (piece != null) {
                    System.out.print(show(piece.getAffichage()) + "|");
                } else {
                    System.out.print(empty() + "|");
                }
            }
            System.out.println();
        }
        System.out.println("  A B C D E F G H");
    }

    /**
     * Retourne une chaîne vide représentant une case vide.
     * @return une chaîne contenant un espace
     */
    private String empty() {
        return " ";
    }

    /**
     * Convertit le code Unicode d’une pièce en caractère affichable.
     * @param piece le code Unicode de la pièce
     * @return le caractère correspondant
     */
    private String show(int piece) {
        return new String(Character.toChars(piece));
    }

    /**
     * Gère la promotion d’un pion arrivé au bout de l’échiquier.
     * - Propose à l’utilisateur un choix de pièce
     * - Remplace le pion par la nouvelle pièce sur l’échiquier
     * @param pos la position du pion à promouvoir
     * @param scanner l’instance Scanner pour lire la saisie utilisateur
     */
    public void handlePromotion(String pos, Scanner scanner) {
        System.out.println("Le pion atteint la dernière rangée, choisissez une pièce pour la transformation:");
        System.out.println("1: Dame\n2: Tour\n3: Fou\n4: Cavalier");

        int choice = scanner.nextInt();
                
        Piece piece = chessBoard.board.get(pos);
        Pion pion = (Pion) piece;
        chessBoard.changePiece(pos, pion.promote(choice)); // Promouvoir le pion
    }
}
