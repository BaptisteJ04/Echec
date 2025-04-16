package tests.allPieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import projet.all_pieces.Roi;
import projet.models.ChessBoard;
import projet.all_pieces.Tour; // Pour les tests de captures

public class TestsRoi {

    private ChessBoard board;
    private Roi roiBlanc;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
        roiBlanc = new Roi(4, 4, 0); // Position centrale
        board.addPiece(roiBlanc);
    }
    
    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Roi(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }


    @Test
    public void testDeplacementValide() {
        int[] nouvellePosition = {5, 5};
        assertTrue(roiBlanc.isValidDeplacement(nouvellePosition, board));
    }

    @Test
    public void testDeplacementInvalide() {
        int[] tropLoin = {6, 6};
        assertFalse(roiBlanc.isValidDeplacement(tropLoin, board));
    }

    @Test
    public void testCapturePossible() {
        Tour tourNoire = new Tour(5, 5, 1);
        board.addPiece(tourNoire);

        assertTrue(roiBlanc.isValidCatch(new int[]{5, 5}, board));
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        Tour tourBlanche = new Tour(5, 5, 0);
        board.addPiece(tourBlanche);

        assertFalse(roiBlanc.isValidCatch(new int[]{5, 5}, board));
    }

    @Test
    public void testGetPossibleMovesSansObstacle() {
        List<int[]> moves = roiBlanc.getPossibleMoves(board);
        assertEquals(8, moves.size()); //Il doit y avoir 8 coups possible
    }

    @Test
    public void testGetPossibleMovesAvecObstacle() {
        board.addPiece(new Tour(3, 4, 0)); 
        List<int[]> moves = roiBlanc.getPossibleMoves(board);

        // Il ne doit pas inclure la case C4 car il y a une tour dessus
        assertTrue(moves.stream().noneMatch(m -> m[0] == 3 && m[1] == 4));
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(3, 4, 1));
        board.addPiece(new Tour(3, 3, 1));
        List<int[]> captures = roiBlanc.getPossibleCaptures(board);

        assertTrue(captures.stream().anyMatch(c -> c[0] == 3 && c[1] == 4));
        assertEquals(2,captures.size());
    }

    
    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = roiBlanc.getAffichage();
        assertEquals(0x2654, unicode); // Roi blanc
    }

    @Test
    public void testDeplacementEffectif() {
        roiBlanc.deplacement(new int[]{5, 5});
        assertEquals(5, roiBlanc.getX());
        assertEquals(5, roiBlanc.getY());
    }
}
