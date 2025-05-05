package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant le Roi dans le jeu d'échecs.
 * Le Roi peut se déplacer d'une case dans toutes les directions.
 */
public class Roi extends Piece {

	// Codes Unicode pour afficher le roi (blanc ou noir)
	private final int[] affichage = { 0x2654, 0x265A };

	/**
	 * Constructeur du roi.
	 *
	 * @param x      Position X initiale du roi
	 * @param y      Position Y initiale du roi
	 * @param player Joueur auquel appartient le roi (0 = blanc, 1 = noir)
	 */
	public Roi(int x, int y, int player) {
		// Appelle le constructeur de Piece avec l'image correspondante à la couleur du roi
		super(x, y, player, player == 0 ? "./src/Images/white_king.png" : "./src/Images/balck_king.png");
	}

	/**
	 * Déplace le roi à la position indiquée.
	 *
	 * @param end Coordonnées [x, y] de la nouvelle position
	 */
	@Override
	public void deplacement(int[] end) {
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si le déplacement demandé est valide pour le roi.
	 *
	 * @param end   Coordonnées de destination
	 * @param board Échiquier
	 * @return True si le déplacement est autorisé
	 */
	@Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
		for (int[] move : getPossibleMoves(board)) {
			if (move[0] == end[0] && move[1] == end[1])
				return true;
		}
		return false;
	}

	/**
	 * Vérifie si la capture demandée est valide pour le roi.
	 *
	 * @param end   Coordonnées de la cible
	 * @param board Échiquier
	 * @return True si la capture est autorisée
	 */
	@Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
		for (int[] capture : getPossibleCaptures(board)) {
			if (capture[0] == end[0] && capture[1] == end[1])
				return true;
		}
		return false;
	}

	/**
	 * Retourne toutes les cases vers lesquelles le roi peut se déplacer sans capturer.
	 *
	 * @param board Échiquier
	 * @return Liste des positions accessibles sans capture
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		// Le roi peut aller dans 8 directions autour de lui
		int[][] directions = {
			{ 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, // Droite, gauche, haut, bas
			{ 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } // Diagonales
		};

		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			// Vérifie que les coordonnées restent dans les limites de l'échiquier
			if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				// Si la case est vide, le roi peut s'y déplacer
				if (board.getPieceAt(nx, ny) == null) {
					moves.add(new int[] { nx, ny });
				}
			}
		}

		return moves;
	}

	/**
	 * Retourne toutes les cases vers lesquelles le roi peut capturer une pièce ennemie.
	 *
	 * @param board Échiquier
	 * @return Liste des positions accessibles avec capture
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		int[][] directions = {
			{ 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
			{ 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }
		};

		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				// Si une pièce ennemie est présente, elle peut être capturée
				if (p != null && p.getPlayer() != this.getPlayer()) {
					captures.add(new int[] { nx, ny });
				}
			}
		}

		return captures;
	}

	/**
	 * Clone le roi à sa position actuelle.
	 *
	 * @return Nouvelle instance du roi avec les mêmes coordonnées et joueur
	 */
	public Roi clone() {
		return new Roi(getX(), getY(), getPlayer());
	}

	/**
	 * Retourne le code Unicode pour afficher le roi en fonction de sa couleur.
	 *
	 * @return Code Unicode pour l'affichage du roi
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
