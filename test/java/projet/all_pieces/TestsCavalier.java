package projet.all_pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projet.models.ChessBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsCavalier {

    private ChessBoard board;
    private Cavalier cavalierBlanc;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
        cavalierBlanc = new Cavalier(4, 4, 0); 
        board.addPiece(cavalierBlanc);
    }
    
    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Cavalier(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }

    @Test
    public void testDeplacementValide() {
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{6, 5}, board));
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{5, 6}, board));
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{2, 3}, board));
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{3, 2}, board));
    }

    @Test
    public void testDeplacementInvalide() {
        assertFalse(cavalierBlanc.isValidDeplacement(new int[]{4, 5}, board)); 
        assertFalse(cavalierBlanc.isValidDeplacement(new int[]{5, 5}, board));
        assertFalse(cavalierBlanc.isValidDeplacement(new int[]{6, 6}, board));
    }

    @Test
    public void testCapturePossible() {
        Tour ennemi1 = new Tour(6, 5, 1);
        Tour ennemi2 = new Tour(3, 2, 1);
        board.addPiece(ennemi1);
        board.addPiece(ennemi2);

        assertTrue(cavalierBlanc.isValidCatch(new int[]{6, 5}, board));
        assertTrue(cavalierBlanc.isValidCatch(new int[]{3, 2}, board));
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        Tour tourAlliee = new Tour(6, 5, 0);
        board.addPiece(tourAlliee);

        assertFalse(cavalierBlanc.isValidCatch(new int[]{6, 5}, board));
    }

    @Test
    public void testGetPossibleMovesSansObstacle() {
        List<int[]> moves = cavalierBlanc.getPossibleMoves(board);
        assertEquals(8, moves.size()); // Le cavalier a toujours 8 mouvements max
    }

    @Test
    public void testGetPossibleMovesBordDuPlateau() {
        Cavalier cavalier = new Cavalier(1, 1, 0);
        board.emptyChessBoard();
        board.addPiece(cavalier);
        List<int[]> moves = cavalier.getPossibleMoves(board);
        assertEquals(2, moves.size()); // Seulement 2 mouvements valides en coin
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(6, 5, 1));
        board.addPiece(new Tour(2, 3, 1));
        List<int[]> captures = cavalierBlanc.getPossibleCaptures(board);

        assertTrue(captures.stream().anyMatch(c -> c[0] == 6 && c[1] == 5));
        assertTrue(captures.stream().anyMatch(c -> c[0] == 2 && c[1] == 3));
        assertEquals(2, captures.size());
    }

    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = cavalierBlanc.getAffichage();
        assertEquals(0x2658, unicode); 
    }
    @Test
    public void testCavalierPeutSauterParDessus() {
        // On ajoute des pièces qui bloqueraient le chemin si le chemin si le cavalier ne pouvait pas sauter
        board.addPiece(new Tour(5, 4, 0)); 
        board.addPiece(new Tour(4, 5, 0));
        board.addPiece(new Tour(5, 5, 1));
        
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{6, 5}, board));
        assertTrue(cavalierBlanc.isValidDeplacement(new int[]{5, 6}, board));
        
        // Même avec plein d'obstacles en plus autour, il doit pouvoir prendre
        board.addPiece(new Tour(3, 3, 0));
        board.addPiece(new Tour(2, 5, 1));
        assertTrue(cavalierBlanc.isValidCatch(new int[]{2, 5}, board));
    }


    @Test
    public void testDeplacementEffectif() {
        cavalierBlanc.deplacement(new int[]{6, 5});
        assertEquals(6, cavalierBlanc.getX());
        assertEquals(5, cavalierBlanc.getY());
    }
}
