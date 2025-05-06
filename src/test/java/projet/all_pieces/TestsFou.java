package projet.all_pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import projet.models.ChessBoard;

public class TestsFou {

    private ChessBoard board;
    private Fou fouBlanc;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
        fouBlanc = new Fou(4, 4, 0); // Position centrale
        board.addPiece(fouBlanc);
    }
    
    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Fou(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }


    @Test
    public void testDeplacementValide() {
        assertTrue(fouBlanc.isValidDeplacement(new int[] {5, 5}, board));
        assertTrue(fouBlanc.isValidDeplacement(new int[] {1, 1}, board));
        assertTrue(fouBlanc.isValidDeplacement(new int[] {1, 7}, board));
        assertTrue(fouBlanc.isValidDeplacement(new int[] {6, 2}, board));
    }

    @Test
    public void testDeplacementInvalide() {
        assertFalse(fouBlanc.isValidDeplacement(new int[] {5, 4}, board));
        assertFalse(fouBlanc.isValidDeplacement(new int[] {4, 5}, board));
        assertFalse(fouBlanc.isValidDeplacement(new int[] {3, 4}, board));
        assertFalse(fouBlanc.isValidDeplacement(new int[] {4, 3}, board));
    }

    @Test
    public void testCapturePossible() {
        Tour ennemi1 = new Tour(5, 5, 1);
        Tour ennemi2 = new Tour(1, 1, 1);
        board.addPiece(ennemi1);
        board.addPiece(ennemi2);

        assertTrue(fouBlanc.isValidCatch(new int[]{5, 5}, board));
        assertTrue(fouBlanc.isValidCatch(new int[]{1, 1}, board));
        assertFalse(fouBlanc.isValidCatch(new int[]{6, 6}, board));
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        Tour tourBlanche = new Tour(6, 6, 0);
        board.addPiece(tourBlanche);

        assertFalse(fouBlanc.isValidCatch(new int[]{6, 6}, board));
    }

    @Test
    public void testGetPossibleMovesSansObstacle() {
        List<int[]> moves = fouBlanc.getPossibleMoves(board);
        assertEquals(13, moves.size()); //Il doit y avoir 13 coups possible
    }

    @Test
    public void testGetPossibleMovesAvecObstacle() {
        board.addPiece(new Tour(5, 5, 0)); 
        List<int[]> moves = fouBlanc.getPossibleMoves(board);

        // Il ne doit pas inclure la case C4 car il y a une tour dessus
        assertTrue(moves.stream().noneMatch(m -> m[0] == 5 && m[1] == 5));
        //Il ne doit pas pouvoir sauter la tour noire
        assertTrue(moves.stream().noneMatch(m -> m[0] == 6 && m[1] == 6));
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(7, 1, 1));
        board.addPiece(new Tour(3, 3, 1));
        List<int[]> captures = fouBlanc.getPossibleCaptures(board);

        assertTrue(captures.stream().anyMatch(c -> c[0] == 3 && c[1] == 3));
        assertEquals(2,captures.size());
    }

    
    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = fouBlanc.getAffichage();
        assertEquals(0x2657, unicode); // Roi blanc
    }

    @Test
    public void testDeplacementEffectif() {
        fouBlanc.deplacement(new int[]{5, 5});
        assertEquals(5, fouBlanc.getX());
        assertEquals(5, fouBlanc.getY());
    }
}
