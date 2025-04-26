package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

public class Fou extends Piece {
    
    private final int[] affichage = {0x2657,0x265D}; 

    public Fou(int x, int y, int player) {
        super(x, y, player, player == 0 ? "./src/Images/white_bishop.png" : "./src/Images/black_bishop.png");
    }

    @Override
    public void deplacement(int[] end) {
        this.setX(end[0]);
        this.setY(end[1]);
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
        for (int[] cap : getPossibleCaptures(board)) {
            if (cap[0] == end[0] && cap[1] == end[1]) {
                return true;
            }
        }
        return false;
    }

    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int x = this.getX();
        int y = this.getY();

        // 4 directions diagonales : haut/droite, haut/gauche, bas/droite, bas/gauche
        int[][] directions = { {1, 1}, {-1, 1}, {1, -1}, {-1, -1} };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;

            while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) { //On se déplace en restant dans l'échequier
                if (board.getPieceAt(nx, ny) == null) { //Si il n'y a personne, j'avance
                    moves.add(new int[]{nx, ny});
                } else {
                    break; // sinon obstacle rencontré
                }
                nx += dx; //J'avance dans la diagonale
                ny += dy;
            }
        }

        return moves;
    }

    public List<int[]> getPossibleCaptures(ChessBoard board) {
        List<int[]> captures = new ArrayList<>();
        int x = this.getX();
        int y = this.getY();

        int[][] directions = { {1, 1}, {-1, 1}, {1, -1}, {-1, -1} };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;

            while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {
                Piece p = board.getPieceAt(nx, ny);
                if (p != null) {
                    if (p.getPlayer() != this.getPlayer()) {
                        captures.add(new int[]{nx, ny});
                    }
                    break; // Stoppe après avoir trouvé une pièce
                }
                nx += dx;
                ny += dy;
            }
        }

        return captures;
    }
    
    public Fou clone() {
    	return new Fou(getX(),getY(), getPlayer());
    }

    public int getAffichage() {
        return affichage[this.getPlayer()];
    }
}
