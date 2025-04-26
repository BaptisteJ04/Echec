package tests.testsModels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projet.models.ChessBoard;
import projet.all_pieces.*;
import projet.utils.MinMax;

import static org.junit.jupiter.api.Assertions.*;

public class TestsMinMax {

    private ChessBoard board;
    private MinMax ai;

    @BeforeEach
    public void setup() {
        board = new ChessBoard();
        board.emptyChessBoard(); // Nettoyage de l'échiquier

        // Ajouter quelques pièces pour joueur 0 (blanc) et joueur 1 (noir)
        board.addPiece(new Pion(1, 2, 0)); // Blanc
        board.addPiece(new Pion(1, 7, 1)); // Noir
        board.addPiece(new Roi(5, 1, 0));  // Roi Blanc
        board.addPiece(new Roi(5, 8, 1));  // Roi Noir

        ai = new MinMax(0); // On teste pour le joueur blanc
    }

    @Test
    public void testMeilleurCoupNotNull() {
        String[] coup = ai.meilleurCoup(board);
        assertNotNull(coup, "Le meilleur coup ne doit pas être null.");
        assertEquals(2, coup.length, "Le coup doit contenir 2 positions.");
    }

    @Test
    public void testMeilleurCoupRRandom() {
        String[] coup = ai.meilleurCoupR(board);
        assertNotNull(coup, "Le meilleur coup aléatoire ne doit pas être null.");
        assertEquals(2, coup.length, "Le coup aléatoire doit contenir 2 positions.");
    }
    
    @Test
    public void testEviterEchecDangereuxEtPréférerCapture() {
        board.emptyChessBoard();

        board.addPiece(new Roi(1, 1, 1));         // Roi noir en A1
        board.addPiece(new Tour(2, 6, 1));        // Tour noire en B6
        board.addPiece(new Tour(1, 7, 1));        // Tour noire en A7
        board.addPiece(new Dame(3, 5, 0));        // Dame blanche en C5 (peut capturer Tour)
        board.addPiece(new Cavalier(3, 1, 0));    // Cavalier blanc en C1

        // Si la dame noire fait échec en B1, elle est capturée par la tour ensuite.
        // L’IA doit donc préférer capturer la tour blanche en B6 plutôt que de faire échec.

        String[] meilleurCoup = ai.meilleurCoup(board);

        assertEquals("C5", meilleurCoup[0]);  // On joue la dame
        assertEquals("B6", meilleurCoup[1]);  // Capture de la tour et non échec inutile
    }

    @Test
    public void testPreferenceCaptureDameVsPion() {
        board.emptyChessBoard(); // Nettoyage de l'échiquier
        board.addPiece(new Dame(4, 5, 1)); // Dame noire en D5
        board.addPiece(new Pion(5, 4, 1)); // Pion noire en E4
        board.addPiece(new Cavalier(6, 6, 0)); // Cavalier blanc en F6
        String[] meilleurCoup = ai.meilleurCoup(board);
        // Vérifie que le coup est bien G6 -> E4 (capture de la dame)
        assertEquals("F6", meilleurCoup[0], "Le départ doit être F6 (coordonnées de G6)");
        assertEquals("D5", meilleurCoup[1], "L'IA doit capturer la dame plutôt que le pion");
    }

    
    @Test
    public void testAucunePieceDisponible() {
        ChessBoard empty = new ChessBoard();
        empty.emptyChessBoard();
        empty.addPiece(new Roi(1, 1, 0));
        empty.addPiece(new Roi(8, 8, 1));
        empty.addPiece(new Tour(2, 3, 1));
        empty.addPiece(new Tour(3, 2, 1));
        MinMax emptyAI = new MinMax(0);
        String[] coup = emptyAI.meilleurCoup(empty);
        assertNull(coup, "Il ne devrait pas y avoir de coup possible si aucune pièce ne peut jouer.");
    }

    @Test
    public void testEvaluationEchecEtMat() {
        // Supposons qu'on enlève le roi noir pour simuler un mat
        board.board.values().removeIf(piece -> piece instanceof Roi && piece.getPlayer() == 1);
        int eval = ai.evaluation(board);
        assertTrue(Integer.MAX_VALUE - 2000 < eval, "Le score doit refléter l’échec et mat de l’adversaire.");
    }

    @Test
    public void testEvaluationPerteRoi() {
        board.board.values().removeIf(piece -> piece instanceof Roi && piece.getPlayer() == 0);
        int eval = ai.evaluation(board);
        assertTrue(Integer.MIN_VALUE + 2000 > eval, "Le score doit refléter la perte du roi.");
    }
}
