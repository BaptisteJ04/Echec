package tests.testsModels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import projet.models.ChessBoard;
import projet.all_pieces.*;

public class TestsBoard {

    private ChessBoard board;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard(); // Pour commencer avec un plateau vide
    }

    @Test
    public void testAddAndGetPiece() {
        Tour tour = new Tour(1, 1, 0);
        board.addPiece(tour);
        assertEquals(tour, board.getPieceAt(1, 1));
    }

    @Test
    public void testDeplacementValideSansCapture() {
        Pion pion = new Pion(4, 2, 0);
        board.addPiece(pion);
        int result = board.deplacer("D2", "D3", 0);
        assertEquals(0, result);
        assertNull(board.getPieceAt(4, 2)); //On verifie que le pion n'est plus là
        assertEquals(pion, board.getPieceAt(4, 3));	//On vérifie que le pion a bougé
    }

    @Test
    public void testDeplacementInvalide() {
        Pion pion = new Pion(4, 2, 0);
        board.addPiece(pion);
        int result = board.deplacer("D2", "D5", 0); // trop loin
        assertEquals(-1, result);
        assertEquals(pion, board.getPieceAt(4, 2)); //On vérifie qu'on a pas bougé le pion
    }

    @Test
    public void testCaptureValide() {
        Dame dame = new Dame(4, 4, 0);
        Tour ennemi = new Tour(5, 5, 1);
        board.addPiece(dame);
        board.addPiece(ennemi);
        int result = board.deplacer("D4", "E5", 0);
        assertEquals(0, result);
        assertEquals(dame, board.getPieceAt(5, 5)); //On vérifie que la dame a pris la Tour 
        assertNull(board.getPieceAt(4, 4));	//On vérifie que la dame a bougée (Le J et Marine sont d'accord)
    }

    @Test
    public void testDeplacementMetEnEchec() {
        Roi roi = new Roi(5, 1, 0);
        Tour ennemi = new Tour(5, 8, 1);
        board.addPiece(roi);
        board.addPiece(ennemi);
        Fou fou = new Fou(5, 2, 0);
        board.addPiece(fou);
        int result = board.deplacer("E2", "D3", 0); // libère la colonne pour la tour
        assertEquals(-1, result); // met le roi en échec
    }

    @Test
    public void testPromotionPossible() {
        Pion pion = new Pion(4, 7, 0);
        board.addPiece(pion);
        int result = board.deplacer("D7", "D8", 0);
        assertEquals(1, result); // Requête de promotion
        assertEquals(pion, board.getPieceAt(4, 8));
        assertTrue(board.isPawnPromotion("D8"));
    }

    @Test
    public void testChangePiece() {
        Pion pion = new Pion(4, 8, 0);
        board.addPiece(pion);
        Dame dame = new Dame(4, 8, 0);
        board.changePiece("D8", dame);
        assertFalse(board.getPieceAt(4, 8) instanceof Pion); //On regarde que la pièce en D8 n'est plus un pion
        assertEquals(dame, board.getPieceAt(4, 8)); //On regarde que la pièce en D8 est bien la dame qu'on vient de placer
    }

    @Test
    public void testLoadCustomPiece() {
        PiecePersonnalisee custom = new PiecePersonnalisee(
                4, 4, 0, "custom.png",
                Arrays.asList(new int[]{1, 0}, new int[]{-1, 0}),
                Arrays.asList(new int[]{1, 0}, new int[]{-1, 0}),
                new int[]{'C', 'c'}, "CustomPiece", false, true
        );
        board.loadCustomPieces(Arrays.asList(custom));
        assertEquals(custom, board.getPieceAt(4, 4));
    }

    @Test
    public void testIsKingInCheck() {
        Roi roi = new Roi(5, 1, 0);
        Tour ennemie = new Tour(5, 8, 1);
        board.addPiece(roi);
        board.addPiece(ennemie);
        assertTrue(board.isKingInCheck(0));
    }

    @Test
    public void testIsNotInCheck() {
        Roi roi = new Roi(5, 1, 0);
        Tour tour = new Tour(1, 1, 0);
        board.addPiece(roi);
        board.addPiece(tour);
        assertFalse(board.isKingInCheck(0));
    }

}
