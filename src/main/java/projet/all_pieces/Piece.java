package projet.all_pieces;

import java.util.List;
import projet.models.ChessBoard;

/**
 * Classe abstraite représentant une pièce d'échecs.
 * Chaque type de pièce (Tour, Cavalier, etc.) devra hériter de cette classe
 * et implémenter ses méthodes abstraites selon ses propres règles de mouvement.
 */
public abstract class Piece {
	private int x; // Coordonnée x (colonne) de la pièce sur l'échiquier (1 à 8)
	private int y; // Coordonnée y (ligne) de la pièce sur l'échiquier (1 à 8)
	private int player; // Numéro du joueur (0 ou 1)
	private String pathImage; // Chemin vers l'image représentant la pièce

	// Getters / Setters de l'image
	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	/**
	 * Constructeur de pièce avec vérification de position valide.
	 *
	 * @param x         Coordonnée x de la pièce (1 à 8)
	 * @param y         Coordonnée y de la pièce (1 à 8)
	 * @param player    Joueur propriétaire de la pièce (0 ou 1)
	 * @param pathImage Chemin vers l'image de la pièce
	 */
	public Piece(int x, int y, int player, String pathImage) throws IllegalArgumentException {
		if (!isValidPosition(x, y)) {
			throw new IllegalArgumentException("Position invalide sur l'échiquier");
		}
		this.x = x;
		this.y = y;
		this.player = player;
		this.pathImage = pathImage;
	}

	/**
	 * Déplace la pièce aux coordonnées données (sans vérifier la validité).
	 * 
	 * @param end Coordonnées cibles [x, y]
	 */
	public abstract void deplacement(int[] end);

	/**
	 * Vérifie si un déplacement est valide selon les règles de la pièce.
	 *
	 * @param end   Coordonnées cibles [x, y]
	 * @param Echec L'échiquier actuel
	 * @return true si le déplacement est autorisé
	 */
	public abstract boolean isValidDeplacement(int[] end, ChessBoard Echec);

	/**
	 * Vérifie si une capture est valide selon les règles de la pièce.
	 *
	 * @param end   Coordonnées de la cible à capturer
	 * @param Echec L'échiquier actuel
	 * @return true si la capture est autorisée
	 */
	public abstract boolean isValidCatch(int[] end, ChessBoard Echec);

	/**
	 * Retourne la liste des déplacements possibles pour la pièce.
	 *
	 * @param board L'échiquier sur lequel chercher
	 * @return Liste de coordonnées [x, y] valides
	 */
	public abstract List<int[]> getPossibleMoves(ChessBoard board);

	/**
	 * Retourne la liste des captures possibles pour la pièce.
	 *
	 * @param board L'échiquier sur lequel chercher
	 * @return Liste de coordonnées [x, y] des pièces adverses pouvant être capturées
	 */
	public abstract List<int[]> getPossibleCaptures(ChessBoard board);

	/**
	 * Utilisé pour afficher la pièce dans une interface ou sur la console.
	 *
	 * @return Code entier représentant la pièce (peut être lié à un symbole ou couleur)
	 */
	public abstract int getAffichage();

	/**
	 * Crée une copie de la pièce avec les mêmes attributs.
	 *
	 * @return Une nouvelle instance de la même pièce
	 */
	public abstract Piece clone();

	/**
	 * Convertit une position au format échiquier (ex: "A2") en coordonnées [x, y].
	 *
	 * @param coordonnees Chaîne de caractères représentant une case (ex: "D4")
	 * @return Tableau contenant les coordonnées [x, y]
	 * @throws IllegalArgumentException si le format est incorrect
	 */
	public static int[] parseCoordonnees(String coordonnees) throws IllegalArgumentException {
		if (coordonnees == null || coordonnees.length() != 2) {
			throw new IllegalArgumentException("Coordonnées invalides : " + coordonnees);
		}

		char x = coordonnees.toUpperCase().charAt(0);
		if (x < 'A' || x > 'H') {
			throw new IllegalArgumentException("Colonne invalide : " + x);
		}

		int y;
		try {
			y = Integer.parseInt(coordonnees.substring(1));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Numéro de ligne invalide : " + coordonnees.substring(1));
		}

		if (y < 1 || y > 8) {
			throw new IllegalArgumentException("Ligne hors limites : " + y);
		}

		return new int[] { x - 'A' + 1, y };
	}

	// Vérifie si les coordonnées sont bien dans les limites de l'échiquier
	private boolean isValidPosition(int x, int y) {
		return x >= 1 && x <= 8 && y >= 1 && y <= 8;
	}

	// Getters / Setters de position
	public int getX() {
		return x;
	}

	public void setX(int x) throws IllegalArgumentException {
		if (!isValidPosition(x, this.y)) {
			throw new IllegalArgumentException("Nouvelle position invalide : " + x + ", " + this.y);
		}
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) throws IllegalArgumentException {
		if (!isValidPosition(this.x, y)) {
			throw new IllegalArgumentException("Nouvelle position invalide : " + this.x + ", " + y);
		}
		this.y = y;
	}

	// Getter / Setter du joueur
	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		if (player != 0 && player != 1) {
			throw new IllegalArgumentException("Joueur invalide : " + player);
		}
		this.player = player;
	}
}
