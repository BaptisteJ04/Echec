package projet.utils;

import projet.models.ChessBoard;
import projet.all_pieces.*;

import java.util.*;

public class MinMax {

    private static final int MAX_DEPTH = 3; 
    private int player;
 // Tables de position (pour les blancs, à inverser pour les noirs dans getPositionBonus)
    private static final double[][] PION_TABLE = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5,  5,  5, -5, -5,  5,  5,  5},
        { 1,  1,  2,  3,  3,  2,  1,  1},
        { 0,  0,  0,  2,  2,  0,  0,  0},
        { 0,  0,  0, -2, -2,  0,  0,  0},
        { 1, -1, -2,  0,  0, -2, -1,  1},
        { 1,  2,  2, -2, -2,  2,  2,  1},
        { 0,  0,  0,  0,  0,  0,  0,  0}
    };
    
    private static final double[][] TABLE_PERSO = {
    	    {-2.0, -1.0, -0.5, -0.5, -0.5, -0.5, -1.0, -2.0},
    	    {-1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  0.5, -1.0},
    	    {-0.5,  1.0,  1.5,  2.0,  2.0,  1.5,  1.0, -0.5},
    	    {-0.5,  1.0,  2.0,  2.5,  2.5,  2.0,  1.0, -0.5},
    	    {-0.5,  1.0,  2.0,  2.5,  2.5,  2.0,  1.0, -0.5},
    	    {-0.5,  1.0,  1.5,  2.0,  2.0,  1.5,  1.0, -0.5},
    	    {-1.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -1.0},
    	    {-2.0, -1.0, -0.5, -0.5, -0.5, -0.5, -1.0, -2.0}
    	};

    private static final double[][] CAVALIER_TABLE = {
        {-5, -4, -3, -3, -3, -3, -4, -5},
        {-4, -2,  0,  0,  0,  0, -2, -4},
        {-3,  0,  1, 1.5, 1.5,  1,  0, -3},
        {-3, 0.5,1.5,   2,   2,1.5,0.5, -3},
        {-3,  0,1.5,    2,   2,1.5,  0, -3},
        {-3,0.5,  1, 1.5, 1.5,  1,0.5, -3},
        {-4, -2,  0, 0.5, 0.5,  0, -2, -4},
        {-5, -4, -3, -3, -3, -3, -4, -5}
    };

    private static final double[][] FOU_TABLE = {
        {-2, -1, -1, -1, -1, -1, -1, -2},
        {-1,  0,  0,  0,  0,  0,  0, -1},
        {-1,  0, 0.5, 1, 1, 0.5,  0, -1},
        {-1, 0.5,0.5, 1, 1,0.5,0.5, -1},
        {-1,  0, 1,   1, 1,   1,  0, -1},
        {-1,  1, 1,   1, 1,   1,  1, -1},
        {-1, 0.5,  0,  0,  0,  0,0.5, -1},
        {-2, -1, -1, -1, -1, -1, -1, -2}
    };

    private static final double[][] TOUR_TABLE = {
        { 0, 0, 0, 0, 0, 0, 0, 0},
        { 0.5, 1, 1, 1, 1, 1, 1, 0.5},
        {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
        {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
        {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
        {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
        {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
        { 0, 0, 0, 0.5, 0.5, 0, 0, 0}
    };

    private static final double[][] DAME_TABLE = {
        {-2, -1, -1, -0.5, -0.5, -1, -1, -2},
        {-1,  0,  0,     0,     0,  0,  0, -1},
        {-1,  0, 0.5, 0.5, 0.5, 0.5,  0, -1},
        {-0.5,0, 0.5, 0.5, 0.5, 0.5,  0, -0.5},
        { 0,  0, 0.5, 0.5, 0.5, 0.5,  0, -0.5},
        {-1, 0.5, 0.5, 0.5, 0.5, 0.5,  0, -1},
        {-1,  0, 0.5,   0,   0, 0,   0, -1},
        {-2, -1, -1, -0.5, -0.5, -1, -1, -2}
    };

    private static final double[][] ROI_TABLE_OPENING = {
        {-5, -3, -3, -3, -3, -3, -3, -5},
        {-3, -1,  0,  0,  0,  0, -1, -3},
        {-3,  0, 1, 1.5, 1.5,  1,  0, -3},
        {-3, 0.5,1.5, 2,  2, 1.5, 0.5, -3},
        {-3, 0.5,1.5, 2,  2, 1.5, 0.5, -3},
        {-3,  0, 1, 1.5, 1.5,  1,  0, -3},
        {-3, -1, 0,  0,   0,  0, -1, -3},
        {-5, -3, -3, -3, -3, -3, -3, -5}
    };

    public MinMax(int player) {
		super();
		this.player = player;
	}


    //########################################################################################
    //#			 Cette méthode va retourner le meilleur coup (format String[] {from to}		 #
    //########################################################################################
    
	public String[] meilleurCoup(ChessBoard board) {
        int bestValue = Integer.MAX_VALUE;
        String[] meilleurCoup = null;

        for (Map.Entry<String, Piece> entry : board.board.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getPlayer() != player) continue; //Si on trouve une pièce de notre couleur

            String from = entry.getKey();
            //On regarde tout les deplacements possible captures comprises pour toute les pièces de notre couleur)
            List<int[]> moves = piece.getPossibleMoves(board);
            moves.addAll(piece.getPossibleCaptures(board));
            if(moves.isEmpty()) return null;  //Si on ne peux jouer aucun coup, on retourne null 
            for (int[] move : moves) {
                String to = convertToPosition(move[0], move[1]);
                                
                ChessBoard clone = cloneBoard(board);  //On fait un board clone pour ne pas toucher à l'echequier actuel 
                int status = clone.deplacer(from, to, player);
                if (status == -1) continue;  //On passe si le coup est n'est pas valide

                int eval = min(clone, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (eval < bestValue) {
                    bestValue = eval;
                    meilleurCoup = new String[]{from, to};
                    System.out.println("" + bestValue +", "+ from +", "+ to);
                }
            }
        }

        return meilleurCoup;
    }
	
	//############################################################################################
    //# Cette méthode va retourner un des meilleurs coup aléatoirement(format String[] {from to} #
    //############################################################################################
    
	
	public String[] meilleurCoupR(ChessBoard board) {
	    int bestValue = Integer.MAX_VALUE;
	    List<String[]> meilleursCoups = new ArrayList<>();

	    for (Map.Entry<String, Piece> entry : board.board.entrySet()) {
	        Piece piece = entry.getValue();
	        if (piece.getPlayer() != player) continue;

	        String from = entry.getKey();
	        List<int[]> moves = piece.getPossibleMoves(board);
	        moves.addAll(piece.getPossibleCaptures(board));

	        for (int[] move : moves) {
	            String to = convertToPosition(move[0], move[1]);

	            ChessBoard clone = cloneBoard(board);
	            int status = clone.deplacer(from, to, player);
	            if (status == -1) continue;

	            int eval = min(clone, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

	            if (eval < bestValue) {
	                bestValue = eval;
	                meilleursCoups.clear();
	                meilleursCoups.add(new String[]{from, to});
	            } else if (eval == bestValue) {
	                meilleursCoups.add(new String[]{from, to});
	            }
	        }
	    }

	    if (!meilleursCoups.isEmpty()) {
	        Random rand = new Random();
	        return meilleursCoups.get(rand.nextInt(meilleursCoups.size()));  //On retourne un des meilleurs coups dans la liste aléatoirement
	    }

	    return null;
	}



    //########################################################################################
    //#			 Cette méthode va jouer le meilleur coup pour nous							 #
    //########################################################################################
	
	private int min(ChessBoard board, int depth, int alpha, int beta) {
	    if (depth == MAX_DEPTH) return evaluation(board); //Si on arrive à la profondeur max, on s'arrete et on retourne la valeur de notre board
	    
	    int minEval = Integer.MAX_VALUE;	//On place la valeur de l'évaluation au maximum pour ne pas prendre une évaluation qui n'existe pas

	    for (Map.Entry<String, Piece> entry : board.board.entrySet()) {
	        Piece piece = entry.getValue();
	        if (piece.getPlayer() != player) continue;	//On joue nos pièces

	      //On regarde tout les deplacements possible captures comprises pour toute les pièces de notre couleur)
	        String from = entry.getKey();
	        List<int[]> moves = piece.getPossibleMoves(board);
	        moves.addAll(piece.getPossibleCaptures(board));

	        for (int[] move : moves) {
	            String to = convertToPosition(move[0], move[1]);
	            ChessBoard clone = cloneBoard(board);
	            int status = clone.deplacer(from, to, player);	 //On joue la pièce amie
	            if (status == -1) continue;

	            int eval = max(clone, depth + 1, alpha, beta);	//On descend d'une couche 
	            minEval = Math.min(minEval, eval);	 //On regarde quelle est la meilleure évaluation entre la meilleure acutelle et celle qu'on vient de trouver
	            beta = Math.min(beta, eval);

	            if (beta <= alpha) return minEval;  // élagage alpha-bêta
	        }
	    }

	    return minEval;
	}


    
    //########################################################################################
    //#			 Cette méthode va jouer le meilleur coup pour l'adversaire					 #
    //########################################################################################
	private int max(ChessBoard board, int depth, int alpha, int beta) {
	    if (depth == MAX_DEPTH) return evaluation(board);//Si on arrive à la profondeur max, on s'arrete et on retourne la valeur de notre board

	    int maxEval = Integer.MIN_VALUE;	//On place la valeur de l'évaluation au minimum pour ne pas prendre une évaluation qui n'existe pas

	    for (Map.Entry<String, Piece> entry : board.board.entrySet()) {
	        Piece piece = entry.getValue();
	        if (piece.getPlayer() != (player + 1) % 2) continue;	//On joue que la pièce ennemie
	        
	      //On regarde tout les deplacements possible (captures comprises) pour toute les pièces de la couleur adverse
	        String from = entry.getKey();
	        List<int[]> moves = piece.getPossibleMoves(board);
	        moves.addAll(piece.getPossibleCaptures(board));

	        for (int[] move : moves) {
	            String to = convertToPosition(move[0], move[1]);
	            ChessBoard clone = cloneBoard(board);
	            int status = clone.deplacer(from, to, (player + 1) % 2);	 
	            if (status == -1) continue;

	            int eval = min(clone, depth + 1, alpha, beta);	 //On descend d'une couche
	            maxEval = Math.max(maxEval, eval); //On regarde si le meilleur coup est le meilleur actuel ou celui qu'on vient de trouver 
	            alpha = Math.max(alpha, eval);

	            if (beta <= alpha) return maxEval;  // élagage alpha-bêta
	        }
	    }

	    return maxEval;
	}


    /*évaluation classique où l'on donne des valeurs aux pièces et on veut avoir le plus de valeur
     * 
     * private int evaluation(ChessBoard board) {
        int score = 0;

        for (Piece piece : board.board.values()) {
            int value = getValeurPiece(piece);
            score += (piece.getPlayer() == player ? -value : value); // Plus le score est bas, mieux c'est pour nous
        }

        return score;
    }*/
    
    public int evaluation(ChessBoard board) {
        int score = 0;

        // Vérifie si un roi est capturé (échec et mat)
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;

        for (Piece piece : board.board.values()) {
            if (piece instanceof Roi) {
                if (piece.getPlayer() == 0) whiteKingAlive = true;
                else blackKingAlive = true;
            }
        }

        if (!whiteKingAlive) score += player == 0 ? Integer.MIN_VALUE + 2000 : Integer.MAX_VALUE - 2000; //On met un gros score si on met en mat ou qu'on en prend un 
        if (!blackKingAlive) score += player == 1 ? Integer.MIN_VALUE + 2000 : Integer.MAX_VALUE - 2000;

        for (Map.Entry<String, Piece> entry : board.board.entrySet()) {
            Piece piece = entry.getValue();
            int baseValue = getValeurPiece(piece);
            int[] pos = {piece.getX(), piece.getY()};
            int positionBonus = getPositionBonus(piece, pos[0], pos[1]);

            int totalValue = baseValue + positionBonus;
            score += (piece.getPlayer() == player ? -totalValue : totalValue);
        }

        return score;
    }

    //On rajoute du poid à la position que la pièce occupe (milieu c'est mieux)
    private int getPositionBonus(Piece p, int x, int y) {
        double[][] table = null;

        // Adapter l'orientation de l'échiquier pour les noirs
        int row = p.getPlayer() == 0 ? 8 - y : y - 1;
        int col = p.getPlayer() == 0 ? x - 1 : 8 - x;

        if (p instanceof Pion) table = PION_TABLE;
        else if (p instanceof Cavalier) table = CAVALIER_TABLE;
        else if (p instanceof Fou) table = FOU_TABLE;
        else if (p instanceof Tour) table = TOUR_TABLE;
        else if (p instanceof Dame) table = DAME_TABLE;
        else if (p instanceof Roi) {
            table = ROI_TABLE_OPENING;
        }
        else if(p instanceof PiecePersonnalisee) {
        	if(((PiecePersonnalisee) p).isDeplacementLong()) table = TOUR_TABLE;
        }
        else table = TABLE_PERSO;

        if (table != null && row >= 0 && row < 8 && col >= 0 && col < 8) {
            return (int) table[row][col];
        }

        return 0;
    }


    //On donne une valeur à chaque pièce
    private int getValeurPiece(Piece p) {
        if (p instanceof Roi) return 0;  // Le roi n’a de valeur que s’il est maté
        if (p instanceof Pion) return 10;
        if (p instanceof Cavalier) return 30;
        if (p instanceof Fou) return 30;
        if (p instanceof Tour) return 50;
        if (p instanceof Dame) return 90;
        if (p instanceof PiecePersonnalisee) {
            String name = ((PiecePersonnalisee) p).getName();
            switch (name) {
                case "Pion": return 10;
                case "Fou": return 30;
                case "Cavalier": return 30;
                case "Tour": return 50;
                case "Dame": return 90;
            }
        }
        return 20; // Valeur par défaut pour les pièces inconnues
    }


    //Cette methode crée un nouvel echequier pour jouer les coup du minmax
    private ChessBoard cloneBoard(ChessBoard original) {
        ChessBoard clone = new ChessBoard();
        clone.emptyChessBoard(); //On crée un board vide

        for (Piece piece : original.board.values()) {
        	Piece clone_piece = piece.clone();
            clone.addPiece(clone_piece);	//On remplit le nouveau board avec l'état actuel du plateau 
        }

        return clone;
    }

    private String convertToPosition(int x, int y) {
        return (char) ('A' + x - 1) + Integer.toString(y);
    }
}
