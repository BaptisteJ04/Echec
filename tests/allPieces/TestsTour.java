package tests.allPieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import projet.models.ChessBoard;
import projet.all_pieces.Tour;

public class TestsTour {

    private ChessBoard board;
    private Tour tourBlanche;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
        tourBlanche = new Tour(4, 4, 0); // Position centrale
        board.addPiece(tourBlanche);
    }

    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Tour(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }

    @Test
    public void testDeplacementValide() {
        assertTrue(tourBlanche.isValidDeplacement(new int[] {4, 1}, board)); // en bas
        assertTrue(tourBlanche.isValidDeplacement(new int[] {4, 8}, board)); // en haut
        assertTrue(tourBlanche.isValidDeplacement(new int[] {1, 4}, board)); // à gauche
        assertTrue(tourBlanche.isValidDeplacement(new int[] {8, 4}, board)); // à droite
    }

    @Test
    public void testDeplacementInvalide() {
        assertFalse(tourBlanche.isValidDeplacement(new int[] {5, 5}, board)); // diagonale
        assertFalse(tourBlanche.isValidDeplacement(new int[] {3, 3}, board)); // diagonale
        assertFalse(tourBlanche.isValidDeplacement(new int[] {2, 6}, board)); // L
    }

    @Test
    public void testCapturePossible() {
        Tour ennemi1 = new Tour(4, 1, 1);
        Tour ennemi2 = new Tour(6, 4, 1);
        board.addPiece(ennemi1);
        board.addPiece(ennemi2);

        assertTrue(tourBlanche.isValidCatch(new int[]{4, 1}, board));
        assertTrue(tourBlanche.isValidCatch(new int[]{6, 4}, board));
        assertFalse(tourBlanche.isValidCatch(new int[]{4, 2}, board));
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        Tour tourAmie = new Tour(4, 2, 0);
        board.addPiece(tourAmie);

        assertFalse(tourBlanche.isValidCatch(new int[]{4, 2}, board));
    }

    @Test
    public void testGetPossibleMovesSansObstacle() {
        List<int[]> moves = tourBlanche.getPossibleMoves(board);
        assertEquals(14, moves.size()); // 7 cases dans chaque direction (4 directions)
    }

    @Test
    public void testGetPossibleMovesAvecObstacle() {
        board.addPiece(new Tour(4, 6, 0)); // même camp devant
        board.addPiece(new Tour(4, 2, 1)); // ennemi derrière

        List<int[]> moves = tourBlanche.getPossibleMoves(board);

        assertTrue(moves.stream().noneMatch(m -> m[0] == 4 && m[1] == 6)); // bloqué par allié
        assertTrue(moves.stream().anyMatch(m -> m[0] == 4 && m[1] == 3));  // peut s'arrêter avant la capture
        assertTrue(moves.stream().noneMatch(m -> m[0] == 4 && m[1] == 1)); // ne peut pas sauter
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(4, 7, 1)); // en haut
        board.addPiece(new Tour(1, 4, 1)); // à gauche

        List<int[]> captures = tourBlanche.getPossibleCaptures(board);

        assertEquals(2, captures.size());
        assertTrue(captures.stream().anyMatch(c -> c[0] == 4 && c[1] == 7));
        assertTrue(captures.stream().anyMatch(c -> c[0] == 1 && c[1] == 4));
    }

    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = tourBlanche.getAffichage();
        assertEquals(0x2656, unicode); // Tour blanche
    }

    @Test
    public void testDeplacementEffectif() {
        tourBlanche.deplacement(new int[]{4, 1});
        assertEquals(4, tourBlanche.getX());
        assertEquals(1, tourBlanche.getY());
    }
}
