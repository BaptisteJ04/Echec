package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant un Cavalier (Knight) aux échecs.
 * Le Cavalier a un déplacement particulier en "L", capable de sauter par-dessus les pièces.
 */
public class Cavalier extends Piece {

	// Représentation Unicode du Cavalier selon le joueur : blanc ou noir
	private final int[] affichage = { 0x2658, 0x265E };

	/**
	 * Constructeur du Cavalier.
	 *
	 * @param x      Position initiale X
	 * @param y      Position initiale Y
	 * @param player Joueur (0 = blanc, 1 = noir)
	 */
	public Cavalier(int x, int y, int player) {
		super(x, y, player, player == 0 ? "projet/white_knight.png" : "projet/black_knight.png");
	}

	/**
	 * Met à jour les coordonnées du cavalier après un déplacement.
	 *
	 * @param end Coordonnées de destination
	 */
	@Override
	public void deplacement(int[] end) {
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si un déplacement (sans capture) est valide pour le cavalier.
	 *
	 * @param end   Coordonnées de destination
	 * @param board Échiquier
	 * @return true si le déplacement est valide
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
	 * Vérifie si une capture est valide pour le cavalier.
	 *
	 * @param end   Coordonnées de la pièce cible
	 * @param board Échiquier
	 * @return true si la capture est valide
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
	 * Renvoie la liste des déplacements possibles (sans captures).
	 * Le cavalier se déplace en "L" : 8 positions possibles autour de lui.
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées accessibles
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		// Les 8 mouvements en L possibles pour un cavalier
		int[][] directions = {
			{ 1, 2 }, { 2, 1 }, { -1, 2 }, { -2, 1 },
			{ 1, -2 }, { 2, -1 }, { -1, -2 }, { -2, -1 }
		};

		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			// Vérifie qu'on reste sur l'échiquier
			if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				if (board.getPieceAt(nx, ny) == null) {
					moves.add(new int[] { nx, ny });
				}
			}
		}

		return moves;
	}

	/**
	 * Renvoie la liste des captures possibles.
	 * Le cavalier peut capturer une pièce adverse sur une case atteignable en "L".
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées où une capture est possible
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		int[][] directions = {
			{ 1, 2 }, { 2, 1 }, { -1, 2 }, { -2, 1 },
			{ 1, -2 }, { 2, -1 }, { -1, -2 }, { -2, -1 }
		};

		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				if (p != null && p.getPlayer() != this.getPlayer()) {
					captures.add(new int[] { nx, ny });
				}
			}
		}

		return captures;
	}

	/**
	 * Crée une copie de ce cavalier.
	 *
	 * @return Nouvelle instance de Cavalier avec les mêmes propriétés
	 */
	public Cavalier clone() {
		return new Cavalier(getX(), getY(), getPlayer());
	}

	/**
	 * Retourne le caractère Unicode du cavalier pour affichage (console, GUI, etc).
	 *
	 * @return Entier Unicode représentant la pièce
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
