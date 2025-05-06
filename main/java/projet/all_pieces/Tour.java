package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant une Tour (Rook) dans le jeu d'échecs.
 * La tour se déplace uniquement en ligne droite (horizontalement ou verticalement).
 */
public class Tour extends Piece {

	// Codes Unicode pour l'affichage de la tour (blanche ou noire)
	private final int[] affichage = { 0x2656, 0x265C };

	/**
	 * Constructeur de la tour.
	 *
	 * @param x      Position X initiale
	 * @param y      Position Y initiale
	 * @param player Joueur auquel appartient la pièce (0 = blanc, 1 = noir)
	 */
	public Tour(int x, int y, int player) {
		super(x, y, player, player == 0 ? "projet/white_rook.png" : "projet/black_rook.png");
	}

	/**
	 * Déplace la tour aux coordonnées spécifiées.
	 *
	 * @param end Coordonnées de destination [x, y]
	 */
	@Override
	public void deplacement(int[] end) {
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si un déplacement simple (sans capture) est valide.
	 *
	 * @param end   Coordonnées de destination
	 * @param board Échiquier
	 * @return True si le mouvement est autorisé
	 */
	@Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
		for (int[] move : getPossibleMoves(board)) {
			if (move[0] == end[0] && move[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si une capture est valide.
	 *
	 * @param end   Coordonnées de la pièce à capturer
	 * @param board Échiquier
	 * @return True si la capture est possible
	 */
	@Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
		for (int[] cap : getPossibleCaptures(board)) {
			if (cap[0] == end[0] && cap[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Renvoie toutes les positions possibles vers lesquelles la tour peut se déplacer (cases vides uniquement).
	 *
	 * @param board Échiquier
	 * @return Liste des mouvements valides (sans capture)
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		// Directions horizontales et verticales
		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			// La tour continue en ligne droite tant qu'il n'y a pas d'obstacle
			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				if (p == null) {
					moves.add(new int[] { nx, ny });
				} else {
					break; // Arrêt si une pièce est sur la trajectoire
				}
				nx += dx;
				ny += dy;
			}
		}

		return moves;
	}

	/**
	 * Renvoie toutes les positions où la tour peut capturer une pièce ennemie.
	 *
	 * @param board Échiquier
	 * @return Liste des captures possibles
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			// La tour vérifie chaque case sur sa trajectoire
			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				if (p != null) {
					if (p.getPlayer() != this.getPlayer()) {
						captures.add(new int[] { nx, ny }); // Capture possible
					}
					break; // Arrêt dès qu'on rencontre une pièce
				}
				nx += dx;
				ny += dy;
			}
		}

		return captures;
	}

	/**
	 * Crée une copie de la tour actuelle.
	 *
	 * @return Nouvelle instance de Tour avec mêmes coordonnées et joueur
	 */
	public Tour clone() {
		return new Tour(getX(), getY(), getPlayer());
	}

	/**
	 * Retourne le code Unicode pour afficher la tour (selon la couleur).
	 *
	 * @return Code Unicode pour l'affichage
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
