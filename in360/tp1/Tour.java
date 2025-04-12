package in360.tp1;

import java.util.ArrayList;
import java.util.List;

public class Tour extends Piece {

    private final int[] affichage = {0x265C,0x2656};

    public Tour(int x, int y, int player) {
        super(x, y, player, player == 0 ? "./src/Images/white_rook.png" : "./src/Images/black_rook.png");
    }

    @Override
    public void deplacement(int[] end) {
        this.setX(end[0]);
        this.setY(end[1]);
    }

    @Override
    protected boolean isValidDeplacement(int[] end, ChessBoard board) {
        for (int[] move : getPossibleMoves(board)) {
            if (move[0] == end[0] && move[1] == end[1]) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isValidCatch(int[] end, ChessBoard board) {
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

        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1} // horizontal et vertical
        };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;

            while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) { //On vérifie qu'on ne sort pas de l'échequier
                Piece p = board.getPieceAt(nx, ny);
                if (p == null) {
                    moves.add(new int[]{nx, ny});
                } else {
                    break;
                }
                nx += dx;
                ny += dy;
            }
        }

        return moves;
    }

    public List<int[]> getPossibleCaptures(ChessBoard board) {
        List<int[]> captures = new ArrayList<>();
        int x = this.getX();
        int y = this.getY();

        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;

            while (nx >= 1 && nx <= 8 && ny >= 1 && ny <= 8) {  //On vérifie qu'on ne sort pas de l'échequier
                Piece p = board.getPieceAt(nx, ny);
                if (p != null) {
                    if (p.getPlayer() != this.getPlayer()) {
                        captures.add(new int[]{nx, ny});
                    }
                    break;
                }
                nx += dx;
                ny += dy;
            }
        }

        return captures;
    }

    public int getAffichage() {
        return affichage[this.getPlayer()];
    }
}
