package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

/**
 * Classe représentant un pion d'échecs. Le pion est une pièce qui a des comportements spécifiques,
 * tels que se déplacer d'une case à la fois, capturer en diagonale, et la promotion lorsqu'il atteint
 * la dernière ligne de l'échiquier.
 */
public class Pion extends Piece {

	// Codes Unicode pour l'affichage du pion en fonction de la couleur (blanc ou noir)
	private final int[] affichage = { 0x2659, 0x265F };

	/**
	 * Constructeur pour créer un pion à une position donnée et pour un joueur spécifique.
	 *
	 * @param x       La position en x du pion
	 * @param y       La position en y du pion
	 * @param player  Le joueur auquel appartient le pion (0 = blanc, 1 = noir)
	 */
	public Pion(int x, int y, int player) {
		// Appel du constructeur de la classe parente Piece avec l'image appropriée en fonction du joueur
		super(x, y, player, player == 0 ? "projet/white_pawn.png" : "projet/black_pawn.png");
	}

	/**
	 * Déplace le pion vers une nouvelle position spécifiée par les coordonnées de fin.
	 *
	 * @param end Les coordonnées de la case d'arrivée sous forme de tableau [x, y]
	 */
	@Override
	public void deplacement(int[] end) {
		// Mise à jour de la position du pion
		this.setX(end[0]);
		this.setY(end[1]);
	}

	/**
	 * Vérifie si le déplacement vers la position spécifiée est valide.
	 *
	 * @param end    Les coordonnées de la case d'arrivée sous forme de tableau [x, y]
	 * @param board  L'échiquier sur lequel le pion se déplace
	 * @return True si le déplacement est valide, sinon False
	 */
	@Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
		// On parcourt la liste des déplacements possibles et on vérifie si le coup correspond
		for (int[] move : getPossibleMoves(board)) {
			if (move[0] == end[0] && move[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Vérifie si le déplacement vers la position spécifiée est une capture valide.
	 *
	 * @param end    Les coordonnées de la case d'arrivée sous forme de tableau [x, y]
	 * @param board  L'échiquier sur lequel le pion se déplace
	 * @return True si la capture est valide, sinon False
	 */
	@Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
		// On parcourt la liste des captures possibles et on vérifie si le coup correspond
		for (int[] capture : getPossibleCaptures(board)) {
			if (capture[0] == end[0] && capture[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Récupère la liste des déplacements possibles pour ce pion.
	 *
	 * @param board  L'échiquier sur lequel le pion se déplace
	 * @return La liste des déplacements possibles sous forme de coordonnées [x, y]
	 */
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();

		int x = this.getX();
		int y = this.getY();
		int direction = (this.getPlayer() == 0) ? 1 : -1; // Direction de déplacement : +1 pour les blancs, -1 pour les noirs

		// Vérification si la case directement devant le pion est libre
		if (board.getPieceAt(x, y + direction) == null) {
			moves.add(new int[] { x, y + direction });

			// Si le pion est sur sa case de départ (ligne 2 pour les blancs, ligne 7 pour les noirs)
			if ((this.getPlayer() == 0 && y == 2) || (this.getPlayer() == 1 && y == 7)) {
				// Vérification si la case deux cases devant est également libre
				if (board.getPieceAt(x, y + 2 * direction) == null) {
					moves.add(new int[] { x, y + 2 * direction });
				}
			}
		}

		return moves;
	}

	/**
	 * Récupère la liste des captures possibles pour ce pion.
	 *
	 * @param board  L'échiquier sur lequel le pion se déplace
	 * @return La liste des captures possibles sous forme de coordonnées [x, y]
	 */
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();

		int x = this.getX();
		int y = this.getY();
		int direction = (this.getPlayer() == 0) ? 1 : -1; // Direction de capture : +1 pour les blancs, -1 pour les noirs

		// Calcul des cases en diagonale où le pion peut capturer
		int[] diagLeft = { x - 1, y + direction };
		int[] diagRight = { x + 1, y + direction };

		// Vérification des diagonales gauche et droite
		for (int[] diag : new int[][] { diagLeft, diagRight }) {
			Piece target = board.getPieceAt(diag[0], diag[1]);
			if (target != null && target.getPlayer() != this.getPlayer()) {
				// Si une pièce ennemie est présente sur la diagonale, on peut la capturer
				captures.add(diag);
			}
		}

		return captures;
	}

	/**
	 * Vérifie si le pion peut être promu en une autre pièce (Dame, Tour, Fou ou Cavalier).
	 *
	 * @return True si le pion peut être promu (il atteint la dernière ligne de l'échiquier),
	 *         sinon False
	 */
	public boolean isPromotable() {
		// Le pion peut être promu si il arrive sur la dernière ligne de l'échiquier (ligne 8 pour les blancs,
		// ligne 1 pour les noirs)
		if ((this.getPlayer() == 0 && this.getY() == 8) || (this.getPlayer() == 1 && this.getY() == 1)) {
			return true;
		}
		return false;
	}

	/**
	 * Effectue la promotion du pion en une autre pièce (Dame, Tour, Fou ou Cavalier).
	 *
	 * @param choice Le choix de la pièce (1 = Dame, 2 = Tour, 3 = Fou, 4 = Cavalier)
	 * @return La nouvelle pièce correspondant à la promotion choisie
	 */
	public Piece promote(int choice) {
		Piece newPiece;
		switch (choice) {
		case 1:
			// Promotion en Dame
			newPiece = new Dame(this.getX(), this.getY(), this.getPlayer());
			break;
		case 2:
			// Promotion en Tour
			newPiece = new Tour(this.getX(), this.getY(), this.getPlayer());
			break;
		case 3:
			// Promotion en Fou
			newPiece = new Fou(this.getX(), this.getY(), this.getPlayer());
			break;
		case 4:
			// Promotion en Cavalier
			newPiece = new Cavalier(this.getX(), this.getY(), this.getPlayer());
			break;
		default:
			// Par défaut, promotion en Dame
			newPiece = new Dame(this.getX(), this.getY(), this.getPlayer());
			break;
		}
		return newPiece;
	}

	/**
	 * Clone ce pion pour créer une nouvelle instance avec les mêmes attributs.
	 *
	 * @return Une nouvelle instance de Pion avec les mêmes coordonnées et le même joueur
	 */
	public Pion clone() {
		return new Pion(getX(), getY(), getPlayer());
	}

	/**
	 * Récupère le code Unicode pour afficher le pion en fonction de sa couleur.
	 *
	 * @return Le code Unicode correspondant à l'affichage du pion (blanc ou noir)
	 */
	public int getAffichage() {
		return affichage[this.getPlayer()];
	}
}
