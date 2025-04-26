package projet.all_pieces;

import java.util.List;

import projet.models.ChessBoard;

public abstract class Piece {
    private int x;
    private int y;
    private int player;
    private String pathImage; 

    public String getPathImage() {
		return pathImage;
	}


	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}


	public Piece(int x, int y, int player, String pathImage) throws IllegalArgumentException{
        if (!isValidPosition(x, y)) {
            throw new IllegalArgumentException("Position invalide sur l'échiquier");
        }
        this.x = x;
        this.y = y;
        this.player = player;
        this.pathImage = pathImage;
    }
    

    public abstract void deplacement(int[] end);
    public abstract boolean isValidDeplacement(int[] end, ChessBoard Echec);
    public abstract boolean isValidCatch(int[] end, ChessBoard Echec);
    public abstract List<int[]> getPossibleMoves(ChessBoard board);
    public abstract List<int[]> getPossibleCaptures(ChessBoard board);
    public abstract int getAffichage();
    public abstract Piece clone();

    public static int[] parseCoordonnees(String coordonnees) throws IllegalArgumentException{ 
        if (coordonnees == null || coordonnees.length() != 2) {
            throw new IllegalArgumentException("Coordonnées invalides : " + coordonnees);
        }

        char x = coordonnees.toUpperCase().charAt(0);
        if (x < 'A' || x > 'H') {
            throw new IllegalArgumentException("Colonne invalide : " + x);
        }

        int y;
        try {
            y = Integer.parseInt(coordonnees.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Numéro de ligne invalide : " + coordonnees.substring(1));
        }

        if (y < 1 || y > 8) {
            throw new IllegalArgumentException("Ligne hors limites : " + y);
        }

        return new int[]{x - 'A' + 1, y};
    }
    
    
   
    private boolean isValidPosition(int x, int y) {
        return x >= 1 && x <= 8 && y >= 1 && y <= 8;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) throws IllegalArgumentException{
        if (!isValidPosition(x, this.y)) {
            throw new IllegalArgumentException("Nouvelle position invalide : " + x + ", " + this.y);
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) throws IllegalArgumentException{
        if (!isValidPosition(this.x, y)) {
            throw new IllegalArgumentException("Nouvelle position invalide : " + this.x + ", " + y);
        }
        this.y = y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        if (player != 0 && player != 1) {
            throw new IllegalArgumentException("Joueur invalide : " + player);
        }
        this.player = player;
    }
}