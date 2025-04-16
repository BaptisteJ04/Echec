package projet.all_pieces;

import java.util.ArrayList;
import java.util.List;

import projet.models.ChessBoard;

public class Pion extends Piece {

    private final int[] affichage = {0x2659,0x265F};

    public Pion(int x, int y, int player) {
        super(x, y, player, player == 0 ? "./src/Images/white_pawn.png" : "./src/Images/black_pawn.png");
    }

    @Override
    public void deplacement(int[] end) {
        this.setX(end[0]);
        this.setY(end[1]);
    }

    @Override
	public boolean isValidDeplacement(int[] end, ChessBoard board) {
        for (int[] move : getPossibleMoves(board)) {	//On regarde si le coup qu'on veut jouer est dans la liste des coups possible
            if (move[0] == end[0] && move[1] == end[1]) {
                return true;
            }
        }
        return false;
    }

    @Override
	public boolean isValidCatch(int[] end, ChessBoard board) {
        for (int[] capture : getPossibleCaptures(board)) {		//On regarde si le coup qu'on veut jouer est dans la liste des captures possible
            if (capture[0] == end[0] && capture[1] == end[1]) {
                return true;
            }
        }
        return false;
    }

    public List<int[]> getPossibleMoves(ChessBoard board) {
        List<int[]> moves = new ArrayList<>();

        int x = this.getX();
        int y = this.getY();
        int direction = (this.getPlayer() == 0) ? 1 : -1;  //Savoir si le pion avance ou recule

        if (board.getPieceAt(x, y + direction) == null) { //Si il n'y a personne sur la case devant
            moves.add(new int[]{x, y + direction});

            if ((this.getPlayer() == 0 && y == 2) || (this.getPlayer() == 1 && y == 7)) { //Si on est sur la case initiale
                if (board.getPieceAt(x, y + 2 * direction) == null) { //Si il n'y a personne sur la case 2 devant
                    moves.add(new int[]{x, y + 2 * direction});
                }
            }
        }

        return moves;
    }

    public List<int[]> getPossibleCaptures(ChessBoard board) {
        List<int[]> captures = new ArrayList<>();

        int x = this.getX();
        int y = this.getY();
        int direction = (this.getPlayer() == 0) ? 1 : -1;	//Savoir si le pion avance ou recule

        int[] diagLeft = {x - 1, y + direction};
        int[] diagRight = {x + 1, y + direction};

        for (int[] diag : new int[][]{diagLeft, diagRight}) { //On vérifie sur les diagonales
            Piece target = board.getPieceAt(diag[0], diag[1]);	
            if (target != null && target.getPlayer() != this.getPlayer()) {	//Si il y a quelqu'un sur la diagonale et que la pièce est ennemie on peut la prendre 
                captures.add(diag);
            }
        }

        return captures;
    }
    
    public boolean isPromotable() {
        if ((this.getPlayer() == 0 && this.getY() == 8) || (this.getPlayer() == 1 && this.getY() == 1)) {
            return true;
        }
        return false;
    }
    
    public Piece promote(int choice) {
        Piece newPiece;
        switch (choice) {
            case 1:
                newPiece = new Dame(this.getX(), this.getY(), this.getPlayer());
                break;
            case 2:
                newPiece = new Tour(this.getX(), this.getY(), this.getPlayer());
                break;
            case 3:
                newPiece = new Fou(this.getX(), this.getY(), this.getPlayer());
                break;
            case 4:
                newPiece = new Cavalier(this.getX(), this.getY(), this.getPlayer());
                break;
            default:
                newPiece = new Dame(this.getX(), this.getY(), this.getPlayer()); // Par défaut Dame
                break;
        }
        return newPiece;
    }

    public int getAffichage() {
        return affichage[this.getPlayer()];
    }
    
}