package projet.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projet.all_pieces.*;

/**
 * Représente un échiquier et gère la logique de placement, déplacement et capture des pièces.
 * L'échiquier est implémenté comme une map, avec des positions en notation d'échecs (ex: "E2") comme clés.
 */
public class ChessBoard {
	public Map<String, Piece> board; // Stocke les pièces avec la notation échec comme clé ("A1", "D5", etc.)

	/**
	 * Constructeur : crée un nouvel échiquier vide et l'initialise avec les pièces classiques.
	 */
	public ChessBoard() {
		board = new HashMap<>();
		initializeBoard();
	}

	/**
	 * Vide entièrement l'échiquier.
	 */
	public void emptyChessBoard() {
		board.clear();
	}

	/**
	 * Convertit des coordonnées entières (x, y) en notation d’échecs (ex: 1,1 -> "A1").
	 *
	 * @param x Colonne (1 à 8)
	 * @param y Ligne (1 à 8)
	 * @return La position sous forme de chaîne en notation échec
	 */
	protected String convertToPosition(int x, int y) {
		return (char) ('A' + x - 1) + Integer.toString(y);
	}

	/**
	 * Initialise l'échiquier avec les pièces dans leur position de départ classique.
	 */
	public void initializeBoard() {
		// Pions
		for (int i = 1; i <= 8; i++) {
			board.put(convertToPosition(i, 2), new Pion(i, 2, 0)); // Blancs
			board.put(convertToPosition(i, 7), new Pion(i, 7, 1)); // Noirs
		}

		// Tours
		board.put("A1", new Tour(1, 1, 0)); board.put("H1", new Tour(8, 1, 0)); // Blancs
		board.put("A8", new Tour(1, 8, 1)); board.put("H8", new Tour(8, 8, 1)); // Noirs

		// Cavaliers
		board.put("B1", new Cavalier(2, 1, 0)); board.put("G1", new Cavalier(7, 1, 0)); // Blancs
		board.put("B8", new Cavalier(2, 8, 1)); board.put("G8", new Cavalier(7, 8, 1)); // Noirs

		// Fous
		board.put("C1", new Fou(3, 1, 0)); board.put("F1", new Fou(6, 1, 0)); // Blancs
		board.put("C8", new Fou(3, 8, 1)); board.put("F8", new Fou(6, 8, 1)); // Noirs

		// Dames
		board.put("D1", new Dame(4, 1, 0)); // Blanche
		board.put("D8", new Dame(4, 8, 1)); // Noire

		// Rois
		board.put("E1", new Roi(5, 1, 0)); // Blanc
		board.put("E8", new Roi(5, 8, 1)); // Noir
	}

	/**
	 * Récupère la pièce à la position donnée.
	 *
	 * @param x Colonne (1 à 8)
	 * @param y Ligne (1 à 8)
	 * @return La pièce présente ou null si la case est vide
	 */
	public Piece getPieceAt(int x, int y) {
		return board.get(convertToPosition(x, y));
	}

	/**
	 * Déplace une pièce d'une position vers une autre, si le déplacement est valide.
	 * Gère les captures, vérifie les échecs et les promotions de pions.
	 *
	 * @param from   Position de départ (notation échec)
	 * @param to     Position d’arrivée (notation échec)
	 * @param player Joueur qui effectue le coup (0 ou 1)
	 * @return -1 si coup invalide, 0 si déplacement simple, 1 si promotion requise
	 */
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

		int[] end = Piece.parseCoordonnees(to);

		boolean capturePossible = piece.isValidCatch(end, this);
		boolean deplacementPossible = piece.isValidDeplacement(end, this);

		if (!capturePossible && !deplacementPossible) {
			System.out.println("Déplacement invalide.");
			return -1;
		}

		// Sauvegarde des états
		Piece capturedPiece = board.get(to);
		int[] originalPosition = new int[] { piece.getX(), piece.getY() };

		// Application temporaire du mouvement
		board.remove(from);
		if (capturePossible) board.remove(to);
		piece.deplacement(end);
		board.put(to, piece);

		// Annule si le roi est mis en échec après le coup
		if (isKingInCheck(player)) {
			board.remove(to);
			piece.setX(originalPosition[0]);
			piece.setY(originalPosition[1]);
			board.put(from, piece);
			if (capturePossible && capturedPiece != null) {
				board.put(to, capturedPiece);
			}
			return -1;
		}

		// Vérifie si un pion doit être promu
		if (isPawnPromotion(to)) {
			return 1;
		}

		return 0;
	}

	/**
	 * Vérifie si un pion à une position donnée est éligible à la promotion.
	 *
	 * @param pos Position en notation échec
	 * @return true si le pion doit être promu
	 */
	public boolean isPawnPromotion(String pos) {
		Piece piece = board.get(pos);
		if (piece instanceof Pion) {
			return ((Pion) piece).isPromotable();
		}
		return false;
	}

	/**
	 * Ajoute une pièce au plateau, en se basant sur sa position interne (x, y).
	 *
	 * @param piece La pièce à ajouter
	 */
	public void addPiece(Piece piece) {
		String key = convertToPosition(piece.getX(), piece.getY());
		board.put(key, piece);
	}

	/**
	 * Remplace la pièce à une position donnée par une nouvelle pièce.
	 *
	 * @param pos   Position cible (notation échec)
	 * @param piece Nouvelle pièce
	 */
	public void changePiece(String pos, Piece piece) {
		board.remove(pos);
		board.put(pos, piece);
	}

	/**
	 * Vérifie si le roi d’un joueur est actuellement en échec.
	 *
	 * @param player Joueur (0 ou 1)
	 * @return true si le roi est attaqué
	 */
	public boolean isKingInCheck(int player) {
		int[] kingPosition = findKingCoordinates(player);
		if (kingPosition == null) return false;

		for (Piece piece : board.values()) {
			if (piece.getPlayer() != player && piece.isValidCatch(kingPosition, this)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trouve la position du roi d’un joueur sur l’échiquier.
	 *
	 * @param player Joueur (0 ou 1)
	 * @return Coordonnées [x, y] du roi, ou null s’il n’est pas trouvé
	 */
	private int[] findKingCoordinates(int player) {
		for (Map.Entry<String, Piece> entry : board.entrySet()) {
			Piece piece = entry.getValue();
			if (piece instanceof Roi && piece.getPlayer() == player) {
				return new int[] { piece.getX(), piece.getY() };
			}
		}
		return null;
	}

	/**
	 * Charge une liste de pièces personnalisées sur l’échiquier.
	 *
	 * @param customPieces Liste de pièces créées dynamiquement par l’utilisateur
	 */
	public void loadCustomPieces(List<PiecePersonnalisee> customPieces) {
		for (PiecePersonnalisee customPiece : customPieces) {
			String position = convertToPosition(customPiece.getX(), customPiece.getY());
			board.put(position, customPiece);
		}
	}
}
