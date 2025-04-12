package in360.tp1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChessBoard {
    public Map<String, Piece> board; // Utilisation d'un map pour stocker les pièces en fonction de leur position (position en clé)

    public ChessBoard() {
        board = new HashMap<>();
        initializeBoard();
        }
    
    protected String convertToPosition(int x, int y) {
        return (char) ('A' + x - 1) + Integer.toString(y);
    }
    
    private void initializeBoard() {
        // Pions
        for (int i = 1; i <= 8; i++) {
            board.put(convertToPosition(i, 2), new Pion(i, 2, 0)); //Blanc
            board.put(convertToPosition(i, 7), new Pion(i, 7, 1)); //Noir
        }

        //Tours
        board.put("A1", new Tour(1, 1, 0));//Blanc
        board.put("H1", new Tour(8, 1, 0));
        board.put("A8", new Tour(1, 8, 1));//Noir
        board.put("H8", new Tour(8, 8, 1));

        //Cavaliers
        board.put("B1", new Cavalier(2, 1, 0));//Blanc
        board.put("G1", new Cavalier(7, 1, 0));
        board.put("B8", new Cavalier(2, 8, 1));//Noir
        board.put("G8", new Cavalier(7, 8, 1));

        //Fous
        board.put("C1", new Fou(3, 1, 0));//Blanc
        board.put("F1", new Fou(6, 1, 0));
        board.put("C8", new Fou(3, 8, 1));//Noir
        board.put("F8", new Fou(6, 8, 1));

        //Dame
        board.put("D1", new Dame(4, 1, 0));//Blanc
        board.put("D8", new Dame(4, 8, 1));//Noir

        //Rois
        board.put("E1", new Roi(5, 1, 0));//Blanc
        board.put("E8", new Roi(5, 8, 1));//Noir
    }
    
    public Piece getPieceAt(int x, int y) {
        return board.get(convertToPosition(x, y));
    }
    
    public int deplacer(String from, String to, int player) {
        Piece piece = board.get(from);
        if (piece == null) {
            System.out.println("Aucune pièce sur la case de départ.");
            return -1;
        }

        if (piece.getPlayer() != player) {
            System.out.println("Cette pièce ne vous appartient pas.");
            return -1;
        }

        int[] end = piece.parseCoordonnees(to);

        boolean capturePossible = piece.isValidCatch(end, this);
        boolean deplacementPossible = piece.isValidDeplacement(end, this);

        if (!capturePossible && !deplacementPossible) {
            System.out.println("Déplacement invalide.");
            return -1;
        }

        // Sauvegarde des états
        Piece capturedPiece = board.get(to);
        int[] originalPosition = new int[]{piece.getX(), piece.getY()};

        // On applique temporairement le coup
        board.remove(from);
        if (capturePossible) {
            board.remove(to);
        }
        piece.deplacement(end);
        board.put(to, piece);

        // Vérification : est-ce que ça met le roi du joueur en échec ?
        if (isKingInCheck(player)) {
            // On annule le coup
            board.remove(to);
            piece.setX(originalPosition[0]);
            piece.setY(originalPosition[1]);
            board.put(from, piece);
            if (capturePossible && capturedPiece != null) {
                board.put(to, capturedPiece);
            }

            System.out.println("Ce coup vous met en échec. Déplacement annulé.");
            return -1;
        }

        // Promotion du pion ?
        if (isPawnPromotion(to)) {
            return 1; // Besoin de promotion
        }

        return 0; // Déplacement normal
    }

    
    public boolean isPawnPromotion(String pos) {
        Piece piece = board.get(pos);
        if (piece instanceof Pion) {
            Pion pion = (Pion) piece;
            return pion.isPromotable(); // Vérifie si le pion doit être promu
        }
        return false;
    }
    
    public void addPiece(Piece piece) {
        String key = convertToPosition(piece.getX(), piece.getY());
        board.put(key, piece);
    }

    
    public void changePiece(String pos, Piece piece) {
    	 board.remove(pos);
         board.put(pos, piece);
    	return;
    }
    
    public boolean isKingInCheck(int player) {
        // Trouve la position du roi du joueur
        String kingPosition = findKingPosition(player);
                
        for (Piece piece : board.values()) {	//On vérifie si une des pièce ennemie du plateau peux prendre le roi
            if (piece.getPlayer() != player && piece.isValidCatch(piece.parseCoordonnees(kingPosition), this)) {
                    return true;
            }
        }
        
        return false;
    }


    // Trouve la position du roi du joueur
    private String findKingPosition(int player) {
        for (Map.Entry<String, Piece> entry : board.entrySet()) {  //On cherche dans toute les pièce encore sur l'échequier
            Piece piece = entry.getValue();
            if (piece instanceof Roi && piece.getPlayer() == player) {
                return entry.getKey(); // Retourne la position du roi
            }
        }
        return null; // Il ne devrait normalement pas, ne pas y avoir de roi sur le plateau
    }
    
    public void loadCustomPieces(List<PiecePersonnalisee> customPieces) {
        for (PiecePersonnalisee customPiece : customPieces) {
            String position = convertToPosition(customPiece.getX(), customPiece.getY());
            board.put(position, customPiece);  // Ajoute la pièce personnalisée au plateau
        }
    }
    
}
