package in360.tp1;

import java.util.Scanner;

public class Affichage {
	
	private ChessBoard chessBoard;  // Référence à l'échiquier pour récupérer les pièces

    public Affichage(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

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

    private String empty() {
        return " ";
    }

    private String show(int piece) {
        return new String(Character.toChars(piece));
    }
    
    public void handlePromotion(String pos, Scanner scanner) {
        System.out.println("Le pion atteint la dernière rangée, choisissez une pièce pour la transformation:");
        System.out.println("1: Dame\n2: Tour\n3: Fou\n4: Cavalier");

        int choice = scanner.nextInt();
                
        Piece piece = chessBoard.board.get(pos);
        Pion pion = (Pion) piece;
        chessBoard.changePiece(pos, pion.promote(choice)); // Promouvoir le pion
        
    }
}
