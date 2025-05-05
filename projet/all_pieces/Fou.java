package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant un Fou (Bishop) dans le jeu d'échecs.
 * Le fou se déplace uniquement en diagonale.
 */
public class Fou extends Piece {

	// Codes Unicode pour l'affichage (blanc / noir)
	private final int[] affichage = { 0x2657, 0x265D };

	/**
	 * Constructeur du fou.
	 *
	 * @param x      Position X initiale
	 * @param y      Position Y initiale
	 * @param player Joueur (0 pour blanc, 1 pour noir)
	 */
	public Fou(int x, int y, int player) {
		super(x, y, player, player == 0 ? "./src/Images/white_bishop.png" : "./src/Images/black_bishop.png");
	}

	/**
	 * Applique le déplacement en mettant à jour les coordonnées de la pièce.
	 *
	 * @param end Coordonnées de destination [x, y]
	 */
	@Override
	public void deplacement(int[] end) {
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si un déplacement simple (non capture) est valide.
	 *
	 * @param end   Coordonnées de destination
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
	 * @param end   Coordonnées de la pièce cible
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
	 * Renvoie toutes les positions accessibles par un fou sans capturer de pièce.
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées des mouvements possibles
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		// Les 4 directions diagonales
		int[][] directions = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			// Continue dans une diagonale tant que c’est vide et dans les limites de l’échiquier
			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				if (board.getPieceAt(nx, ny) == null) {
					moves.add(new int[] { nx, ny });
				} else {
					break; // Arrêt si une pièce bloque le chemin
				}
				nx += dx;
				ny += dy;
			}
		}

		return moves;
	}

	/**
	 * Renvoie toutes les positions où le fou peut capturer une pièce ennemie.
	 *
	 * @param board Échiquier
	 * @return Liste des coordonnées des captures possibles
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = this.getX();
		int y = this.getY();

		int[][] directions = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

		for (int[] dir : directions) {
			int dx = dir[0], dy = dir[1];
			int nx = x + dx, ny = y + dy;

			// Explore chaque diagonale jusqu’à rencontrer une pièce
			while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece p = board.getPieceAt(nx, ny);
				if (p != null) {
					if (p.getPlayer() != this.getPlayer()) {
						captures.add(new int[] { nx, ny }); // Capture possible
					}
					break; // Que l'on capture ou non, on ne peut pas aller plus loin
				}
				nx += dx;
				ny += dy;
			}
		}

		return captures;
	}

	/**
	 * Clone cette pièce en conservant ses coordonnées et son joueur.
	 *
	 * @return Nouvelle instance de Fou
	 */
	public Fou clone() {
		return new Fou(getX(), getY(), getPlayer());
	}

	/**
	 * Retourne le code Unicode utilisé pour l'affichage (différent selon le joueur).
	 *
	 * @return Code Unicode du symbole du fou
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
