package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

public class Roi extends Piece {

    private final int[] affichage = {0x2654,0x265A};

    public Roi(int x, int y, int player) {
        super(x, y, player, player == 0 ? "./src/Images/white_king.png" : "./src/Images/balck_king.png");
    }

    @Override
    public void deplacement(int[] end) {
        this.setX(end[0]);
        this.setY(end[1]);
    }

    @Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
        for (int[] move : getPossibleMoves(board)) {
            if (move[0] == end[0] && move[1] == end[1]) return true;
        }
        return false;
    }

    @Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
        for (int[] capture : getPossibleCaptures(board)) {
            if (capture[0] == end[0] && capture[1] == end[1]) return true;
        }
        return false;
    }

    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();
        int x = this.getX();
        int y = this.getY();
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, 
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
            };


        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {	//On vérifie qu'on reste dans l'échequier 
                if (board.getPieceAt(nx, ny) == null) {
                    moves.add(new int[]{nx, ny});
                }
            }
        }

        return moves;
    }

    public List<int[]> getPossibleCaptures(ChessBoard board) {
        List<int[]> captures = new ArrayList<>();
        int x = this.getX();
        int y = this.getY();
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, 
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
            };


        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) { //On vérifie qu'on reste dans l'échequier 
                Piece p = board.getPieceAt(nx, ny);
                if (p != null && p.getPlayer() != this.getPlayer()) {
                    captures.add(new int[]{nx, ny});
                }
            }
        }

        return captures;
    }
    
    public Roi clone() {
    	return new Roi(getX(),getY(), getPlayer());
    }

    public int getAffichage() {
        return affichage[this.getPlayer()];
    }
}
