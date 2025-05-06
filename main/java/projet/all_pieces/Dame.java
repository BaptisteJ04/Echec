package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant une Dame (Queen) dans le jeu d'échecs.
 * La Dame combine les déplacements de la Tour et du Fou.
 */
public class Dame extends Piece {

	// Représentation Unicode selon le joueur (0 = blanc, 1 = noir)
	private final int[] affichage = { 0x2655, 0x265B };

	/**
	 * Constructeur de la Dame.
	 *
	 * @param x      Position initiale X
	 * @param y      Position initiale Y
	 * @param player Joueur (0 pour blanc, 1 pour noir)
	 */
	public Dame(int x, int y, int player) {
		super(x, y, player, player == 0 ? "projet/white_queen.png" : "projet/black_queen.png");
	}

	/**
	 * Met à jour les coordonnées de la Dame après un déplacement.
	 *
	 * @param end Coordonnées de destination
	 */
	@Override
	public void deplacement(int[] end) {
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si un déplacement sans capture est valide.
	 *
	 * @param end   Destination du déplacement
	 * @param board Échiquier
	 * @return true si le déplacement est autorisé
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
	 * @param end   Coordonnées de la pièce ciblée
	 * @param board Échiquier
	 * @return true si la capture est possible
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
	 * Renvoie les déplacements possibles (sans captures).
	 * La Dame peut se déplacer dans 8 directions (comme la Tour + Fou).
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées des cases accessibles
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		// 8 directions : 4 lignes (haut, bas, gauche, droite) + 4 diagonales
		int[][] directions = {
			{ 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },     // Lignes
			{ 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }    // Diagonales
		};

		// Pour chaque direction, on avance tant que la case est vide
		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				if (board.getPieceAt(nx, ny) == null) {
					moves.add(new int[] { nx, ny });
				} else {
					break; // Une pièce bloque le chemin
				}
				nx += dx;
				ny += dy;
			}
		}

		return moves;
	}

	/**
	 * Renvoie les captures possibles : cases occupées par des pièces adverses.
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées où une capture est possible
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		int[][] directions = {
			{ 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
			{ 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }
		};

		// On explore chaque direction jusqu'à rencontrer une pièce
		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				if (p != null) {
					if (p.getPlayer() != this.getPlayer()) {
						captures.add(new int[] { nx, ny });
					}
					break; // Que ce soit une capture ou pas, on s'arrête à la 1ère pièce
				}
				nx += dx;
				ny += dy;
			}
		}

		return captures;
	}

	/**
	 * Crée une copie de la Dame avec les mêmes attributs.
	 *
	 * @return Nouvelle instance identique de Dame
	 */
	public Dame clone() {
		return new Dame(getX(), getY(), getPlayer());
	}

	/**
	 * Retourne le symbole Unicode de la Dame selon le joueur.
	 *
	 * @return Code Unicode (entier) pour l'affichage
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
