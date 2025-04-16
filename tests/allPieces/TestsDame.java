package tests.allPieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projet.all_pieces.Dame;
import projet.all_pieces.Tour;
import projet.models.ChessBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsDame {

    private ChessBoard board;
    private Dame dameBlanche;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
        dameBlanche = new Dame(4, 4, 0); // Dame blanche positionnée au centre
        board.addPiece(dameBlanche);
    }

    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Dame(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }

    @Test
    public void testDeplacementValide() {
        assertTrue(dameBlanche.isValidDeplacement(new int[] {4, 7}, board)); // vertical
        assertTrue(dameBlanche.isValidDeplacement(new int[] {1, 4}, board)); // horizontal
        assertTrue(dameBlanche.isValidDeplacement(new int[] {7, 7}, board)); // diagonale
        assertTrue(dameBlanche.isValidDeplacement(new int[] {1, 7}, board)); // autre diagonale
    }

    @Test
    public void testDeplacementInvalide() {
        assertFalse(dameBlanche.isValidDeplacement(new int[] {5, 6}, board)); // pas en ligne/dia
        assertFalse(dameBlanche.isValidDeplacement(new int[] {2, 5}, board));
    }

    @Test
    public void testCapturePossible() {
        Tour ennemi1 = new Tour(4, 7, 1);
        Tour ennemi2 = new Tour(1, 1, 1);
        board.addPiece(ennemi1);
        board.addPiece(ennemi2);

        assertTrue(dameBlanche.isValidCatch(new int[]{4, 7}, board));
        assertTrue(dameBlanche.isValidCatch(new int[]{1, 1}, board));
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        Tour allié = new Tour(5, 5, 0);
        board.addPiece(allié);
        assertFalse(dameBlanche.isValidCatch(new int[]{5, 5}, board));
    }

    @Test
    public void testGetPossibleMovesSansObstacle() {
        List<int[]> moves = dameBlanche.getPossibleMoves(board);
        assertEquals(27, moves.size());
    }

    @Test
    public void testGetPossibleMovesAvecObstacle() {
        board.addPiece(new Tour(6, 6, 0)); // bloqueur
        List<int[]> moves = dameBlanche.getPossibleMoves(board);

        assertTrue(moves.stream().noneMatch(m -> m[0] == 6 && m[1] == 6)); //Est bloqué
        assertTrue(moves.stream().noneMatch(m -> m[0] == 7 && m[1] == 7)); //Ne saute pas l'obstacle
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(2, 2, 1));
        board.addPiece(new Tour(4, 6, 1));
        List<int[]> captures = dameBlanche.getPossibleCaptures(board);

        assertTrue(captures.stream().anyMatch(c -> c[0] == 2 && c[1] == 2));
        assertTrue(captures.stream().anyMatch(c -> c[0] == 4 && c[1] == 6));
        assertEquals(2, captures.size());
    }

    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = dameBlanche.getAffichage();
        assertEquals(0x2655, unicode); // ♕ pour dame blanche
    }

    @Test
    public void testDeplacementEffectif() {
        dameBlanche.deplacement(new int[]{8, 8});
        assertEquals(8, dameBlanche.getX());
        assertEquals(8, dameBlanche.getY());
    }
}

