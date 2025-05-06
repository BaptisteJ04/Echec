package projet.all_pieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import projet.models.ChessBoard;

public class TestsPion {

    private ChessBoard board;
    private Pion pionBlanc;
    private Pion pionNoir;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();

        pionBlanc = new Pion(4, 2, 0); // Pion blanc initiale
        pionNoir = new Pion(5, 7, 1);  // Pion noir initiale
        board.addPiece(pionBlanc);
        board.addPiece(pionNoir);
    }
    
    @Test
    public void testInitHorsPlateau() {
    	assertThrows(IllegalArgumentException.class, () -> {
    	        new Pion(9, 5, 0); // X = 9 est hors échiquier
    	        });
    }

    @Test
    public void testDeplacementEffectif() {
        pionBlanc.deplacement(new int[]{4, 3});
        assertEquals(4, pionBlanc.getX());
        assertEquals(3, pionBlanc.getY());
    }

    @Test
    public void testDeplacementValideBlanc() {
        assertTrue(pionBlanc.isValidDeplacement(new int[]{4, 3}, board)); // avance 1
        assertTrue(pionBlanc.isValidDeplacement(new int[]{4, 4}, board)); // avance 2
        pionBlanc.deplacement(new int[]{4, 3});
        assertTrue(pionBlanc.isValidDeplacement(new int[]{4, 4}, board)); // avance 1
        assertFalse(pionBlanc.isValidDeplacement(new int[]{4, 5}, board)); // avance 2 mais pas en initiale
    }

    @Test
    public void testDeplacementValideNoir() {
        assertTrue(pionNoir.isValidDeplacement(new int[]{5, 6}, board)); // avance 1
        assertTrue(pionNoir.isValidDeplacement(new int[]{5, 5}, board)); // avance 2
    }

    @Test
    public void testDeplacementInvalide() {
        assertFalse(pionBlanc.isValidDeplacement(new int[]{4, 5}, board)); // trop loin
        assertFalse(pionNoir.isValidDeplacement(new int[]{5, 4}, board)); // trop loin
        assertFalse(pionBlanc.isValidDeplacement(new int[]{5, 3}, board)); // diagonale sans capture
    }

    @Test
    public void testCapturePossible() {
        // Placer une tour ennemie pour tester la capture diagonale
        board.addPiece(new Tour(5, 3, 1)); // diagonale avant droite pour le blanc
        board.addPiece(new Tour(4, 6, 0)); // diagonale avant gauche pour le noir
        board.addPiece(new Tour(5, 6, 0)); // devant le noir

        assertTrue(pionBlanc.isValidCatch(new int[]{5, 3}, board));
        assertTrue(pionNoir.isValidCatch(new int[]{4, 6}, board));
        assertFalse(pionNoir.isValidCatch(new int[]{5, 6}, board)); //ne peux pas prendre à l'horizontale
    }

    @Test
    public void testCaptureImpossibleMemeCamp() {
        board.addPiece(new Tour(5, 3, 0)); // ne peut pas capturer un allié
        assertFalse(pionBlanc.isValidCatch(new int[]{5, 3}, board));
    }

    @Test
    public void testGetPossibleMoves() {
        List<int[]> movesBlanc = pionBlanc.getPossibleMoves(board);
        assertTrue(movesBlanc.stream().anyMatch(m -> m[0] == 4 && m[1] == 3));
        assertTrue(movesBlanc.stream().anyMatch(m -> m[0] == 4 && m[1] == 4));
    }

    @Test
    public void testGetPossibleCaptures() {
        board.addPiece(new Tour(3, 3, 1));
        board.addPiece(new Tour(5, 3, 1));
        List<int[]> captures = pionBlanc.getPossibleCaptures(board);

        assertEquals(2, captures.size());
        assertTrue(captures.stream().anyMatch(c -> c[0] == 3 && c[1] == 3));
        assertTrue(captures.stream().anyMatch(c -> c[0] == 5 && c[1] == 3));
    }

    @Test
    public void testDeplacementBloque() {
        board.addPiece(new Tour(4, 3, 1)); // une pièce bloque le pion blanc
        assertFalse(pionBlanc.isValidDeplacement(new int[]{4, 3}, board));
    }

    @Test
    public void testAffichageUnicodeCorrect() {
        int unicode = pionBlanc.getAffichage();
        assertEquals(0x2659, unicode); // Pion blanc
    }
    
    @Test
    public void testPromotionPionBlanc() {
        Pion pion = new Pion(4, 8, 0); // Le pion blanc est à la dernière ligne
        assertTrue(pion.isPromotable());
        Piece promotionDame = pion.promote(1);
        Piece promotionTour = pion.promote(2);
        Piece promotionFou = pion.promote(3);
        Piece promotionCavalier = pion.promote(4);
        Piece promotionDefaut = pion.promote(99);

        assertTrue(promotionDame instanceof Dame);
        assertTrue((pion.getX() == promotionDame.getX()) &&(pion.getY() == promotionDame.getY())); //Vérifie qu'on place bien la dame au bon endroit
        assertTrue(promotionTour instanceof Tour);
        assertTrue(promotionFou instanceof Fou);
        assertTrue(promotionCavalier instanceof Cavalier);
        assertTrue(promotionDefaut instanceof Dame); // choix par défaut
    }

    @Test
    public void testPromotionPionNoir() {
        Pion pion = new Pion(5, 1, 1); // Le pion noir est à la dernière ligne
        assertTrue(pion.isPromotable());

        Piece promotion = pion.promote(2); // Promotion en Tour
        assertTrue(promotion instanceof Tour);
    }

}
