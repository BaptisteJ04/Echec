package projet.all_pieces;

import java.util.*;

import projet.models.ChessBoard;

public class PiecePersonnalisee extends Piece {

	private List<int[]> deplacementsPossibles; // Déplacements sans capture
	private List<int[]> capturesPossibles; // Déplacements en cas de capture
	private int[] affichage;
	private boolean peutSauter; // Si la pièce peut sauter par-dessus d'autres pièces (ex : Assassin)
	private boolean deplacementLong; // Si la pièce peut se déplacer de plus d'une case (ex : Dame)

	private String name;

	public PiecePersonnalisee(int x, int y, int player, String pathImage, List<int[]> moves, List<int[]> captures,
			int[] affichage, String name, boolean peutSauter, boolean deplacementLong) {
		super(x, y, player, pathImage);
		this.deplacementsPossibles = moves;
		this.capturesPossibles = captures;
		this.affichage = affichage;
		this.name = name;
		this.peutSauter = peutSauter;
		this.deplacementLong = deplacementLong;
	}

	@Override
	public void deplacement(int[] end) {
		setX(end[0]);
		setY(end[1]);
	}

	@Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
		for (int[] move : getPossibleMoves(board)) {
			if (move[0] == end[0] && move[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
		for (int[] capture : getPossibleCaptures(board)) {
			if (capture[0] == end[0] && capture[1] == end[1]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<int[]> getPossibleMoves(ChessBoard board) {
		List<int[]> moves = new ArrayList<>();
		int x = getX();
		int y = getY();

		// Parcours des déplacements possibles
		for (int[] move : deplacementsPossibles) {
			if (move == null)
				break;
			int dx = move[0], dy = move[1];
			int nx = x + dx, ny = y + dy;

			// Si la pièce se déplace de manière longue, on vérifie plusieurs cases
			while (deplacementLong && nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
				Piece obstacle = board.getPieceAt(nx, ny);

				if (obstacle == null) { // Pas d'obstacles, on avance
					moves.add(new int[] { nx, ny });
				}

				if (!peutSauter && obstacle != null) { // Un obstacle mais on ne peut pas sauter, donc on est bloqué
					break;
				}

				// Avancer sur la case suivante
				nx += dx;
				ny += dy;
			}

			// Si la pièce ne se déplace pas de manière longue, on ajoute le déplacement une
			// seule fois
			if (!deplacementLong && nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8 && board.getPieceAt(nx, ny) == null) {
				moves.add(new int[] { nx, ny });
			}
		}
		return moves;
	}

	@Override
	public List<int[]> getPossibleCaptures(ChessBoard board) {
		List<int[]> captures = new ArrayList<>();
		int x = getX();
		int y = getY();
		int alreadyCatched = 0;

		// Parcours les directions de capture
		for (int[] capture : capturesPossibles) {
			alreadyCatched = 0;
			int nx = x + capture[0];
			int ny = y + capture[1];

			// Si la pièce peut se déplacer de plus d'une case, on va tester toutes les
			// cases
			if (deplacementLong) {
				while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
					Piece pieceSurChemin = board.getPieceAt(nx, ny);

					// Si la pièce peut sauter ou la case est vide
					if (peutSauter || pieceSurChemin == null) {
						// Si une pièce adverse est présente, et qu'on n'a pas de pièces entre nous, on
						// peut la capturer
						if (pieceSurChemin != null && pieceSurChemin.getPlayer() != getPlayer()
								&& alreadyCatched == 0) {
							alreadyCatched++; // Permet qu'on ne puisse pas capturer toutes les pièces d'une seule ligne
							captures.add(new int[] { nx, ny });
						}
					} else if (pieceSurChemin.getPlayer() != getPlayer()) { // Si on rencontre une pièce adverse mais
																			// qu'on ne peux pas sauter
						alreadyCatched++;
						captures.add(new int[] { nx, ny });
						break;
					} else {
						break;
					}

					// Avancer sur la case suivante
					nx += capture[0];
					ny += capture[1];
				}
			}
			// Si la pièce ne fait pas de déplacement long (plus d'une case)
			else {
				if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
					Piece cible = board.getPieceAt(nx, ny);
					if (cible != null && cible.getPlayer() != getPlayer()) {
						captures.add(new int[] { nx, ny });
					}
				}
			}
		}

		return captures;
	}

	@Override
	public String toString() {
		return String.format(
				"PiecePersonnalisee{name='%s', position=(%d,%d), player=%d, peutSauter=%b, deplacementLong=%b, unicode='%s'}",
				name, getX(), getY(), getPlayer(), peutSauter, deplacementLong,
				new String(Character.toChars(getAffichage())));
	}

	@Override
	public int getAffichage() {
		return affichage[getPlayer()];
	}

	public String getName() {
		return name;
	}

	public List<int[]> getDeplacementsPossibles() {
		return deplacementsPossibles;
	}

	public void setDeplacementsPossibles(List<int[]> deplacementsPossibles) {
		this.deplacementsPossibles = deplacementsPossibles;
	}

	public List<int[]> getCapturesPossibles() {
		return capturesPossibles;
	}

	public void setCapturesPossibles(List<int[]> capturesPossibles) {
		this.capturesPossibles = capturesPossibles;
	}

	public boolean isPeutSauter() {
		return peutSauter;
	}

	public void setPeutSauter(boolean peutSauter) {
		this.peutSauter = peutSauter;
	}

	public boolean isDeplacementLong() {
		return deplacementLong;
	}

	public void setDeplacementLong(boolean deplacementLong) {
		this.deplacementLong = deplacementLong;
	}

	public void setAffichage(int[] affichage) {
		this.affichage = affichage;
	}

	public int[] getAffichage2() {
		return affichage;
	}

	public PiecePersonnalisee clone() {
		return new PiecePersonnalisee(getX(), getY(), getPlayer(), getPathImage(), getDeplacementsPossibles(), // On ne
																												// copie
																												// pas
																												// les
																												// listes
																												// en
																												// profondeur
																												// car
																												// pas
																												// utile
																												// ici
				getCapturesPossibles(), getAffichage2(), getName(), isPeutSauter(), isDeplacementLong());
	}

	public void setName(String name) {
		this.name = name;
	}
}
