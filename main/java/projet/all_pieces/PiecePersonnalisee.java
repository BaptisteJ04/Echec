package projet.all_pieces;

import java.util.*;

import projet.models.ChessBoard;

/**
 * Classe représentant une pièce d'échec personnalisée avec des déplacements et des captures spécifiques.
 * Cette classe permet de définir des pièces avec des comportements non standards (par exemple,
 * des pièces qui peuvent sauter par-dessus d'autres ou se déplacer sur plusieurs cases).
 */
public class PiecePersonnalisee extends Piece {

    private List<int[]> deplacementsPossibles; // Liste des déplacements possibles sans capture
    private List<int[]> capturesPossibles;    // Liste des captures possibles
    private int[] affichage;                  // Code Unicode pour l'affichage de la pièce (en fonction du joueur)
    private boolean peutSauter;               // Indique si la pièce peut sauter par-dessus d'autres pièces
    private boolean deplacementLong;          // Indique si la pièce peut se déplacer sur plusieurs cases
    private String name;                      // Nom de la pièce

    /**
     * Constructeur de la pièce personnalisée.
     *
     * @param x                La position en x de la pièce
     * @param y                La position en y de la pièce
     * @param player           Le joueur auquel appartient la pièce (0 = blanc, 1 = noir)
     * @param pathImage        Le chemin de l'image associée à la pièce
     * @param moves            Liste des déplacements possibles pour la pièce
     * @param captures         Liste des captures possibles pour la pièce
     * @param affichage        Tableau contenant les codes Unicode pour chaque joueur
     * @param name             Le nom de la pièce
     * @param peutSauter       Indique si la pièce peut sauter par-dessus d'autres pièces
     * @param deplacementLong  Indique si la pièce peut se déplacer sur plusieurs cases
     */
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

    /**
     * Déplace la pièce vers la nouvelle position donnée.
     *
     * @param end Le tableau contenant les coordonnées finales [x, y] pour le déplacement
     */
    @Override
    public void deplacement(int[] end) {
        setX(end[0]);
        setY(end[1]);
    }

    /**
     * Vérifie si un déplacement est valide pour cette pièce.
     *
     * @param end    Les coordonnées de la case d'arrivée
     * @param board  L'échiquier sur lequel la pièce se déplace
     * @return True si le déplacement est valide, sinon False
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
     * Vérifie si une capture est valide pour cette pièce.
     *
     * @param end    Les coordonnées de la case cible pour la capture
     * @param board  L'échiquier sur lequel la capture peut se produire
     * @return True si la capture est valide, sinon False
     */
    @Override
    public boolean isValidCatch(int[] end, ChessBoard board) {
        for (int[] capture : getPossibleCaptures(board)) {
            if (capture[0] == end[0] && capture[1] == end[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère la liste des déplacements possibles pour cette pièce.
     *
     * @param board  L'échiquier sur lequel la pièce se déplace
     * @return La liste des déplacements possibles sous forme de coordonnées [x, y]
     */
    @Override
    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int x = getX();
        int y = getY();

        // Parcours des déplacements possibles
        for (int[] move : deplacementsPossibles) {
            if (move == null) break; // Fin de la liste des déplacements possibles
            int dx = move[0], dy = move[1];
            int nx = x + dx, ny = y + dy;

            // Vérification des déplacements longs
            while (deplacementLong && nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
                Piece obstacle = board.getPieceAt(nx, ny);

                // Si la case est libre ou si la pièce peut sauter, on ajoute le déplacement
                if (obstacle == null || peutSauter) {
                    moves.add(new int[] { nx, ny });
                }

                // Si un obstacle est rencontré et que la pièce ne peut pas sauter, on arrête
                if (obstacle != null && !peutSauter) {
                    break;
                }

                // Avancer à la case suivante dans la même direction
                nx += dx;
                ny += dy;
            }

            // Ajout du déplacement si la pièce ne fait pas de déplacement long
            if (!deplacementLong && nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8 && board.getPieceAt(nx, ny) == null) {
                moves.add(new int[] { nx, ny });
            }
        }

        return moves;
    }

    /**
     * Récupère la liste des captures possibles pour cette pièce.
     *
     * @param board  L'échiquier sur lequel la pièce peut capturer
     * @return La liste des captures possibles sous forme de coordonnées [x, y]
     */
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

    /**
     * Clone cette pièce personnalisée en créant une nouvelle instance avec les mêmes attributs.
     *
     * @return Une nouvelle instance de PiecePersonnalisee avec les mêmes valeurs
     */
    @Override
    public PiecePersonnalisee clone() {
        return new PiecePersonnalisee(
            getX(), getY(), getPlayer(), getPathImage(), getDeplacementsPossibles(),
            getCapturesPossibles(), getAffichage2(), getName(), isPeutSauter(), isDeplacementLong()
        );
    }

    // Getters et setters pour les propriétés de la pièce personnalisée
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

    public int[] getAffichage2() {
        return affichage;
    }

    public void setAffichage(int[] affichage) {
        this.affichage = affichage;
    }

    @Override
    public int getAffichage() {
    	return affichage[getPlayer()];
    }
}



