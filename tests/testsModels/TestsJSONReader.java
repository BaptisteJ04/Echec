package tests.testsModels;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import projet.models.*;
import projet.all_pieces.*;

import java.io.IOException;

public class TestsJSONReader {

    private ChessBoard board;
    private final String testFilePath = "./src/projet/tests/testsModels/test_custom_piece.json";
    private final String testFileNonConformePath = "./src/projet/tests/testsModels/jsonNonConforme.json";

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();
    }

    @Test
    public void testChargerPiecePersonnaliseeValide() throws IOException {

        JSONReader.chargerPiecePersonnalisee("D4", 0, testFilePath, board);

        Piece piece = board.getPieceAt(4, 4); // D4 = (4,4)
        System.out.println(piece.toString());
        
        assertNotNull(piece);
        assertTrue(piece instanceof PiecePersonnalisee);
        assertEquals("Test", ((PiecePersonnalisee) piece).getName());
    }

    @Test
    public void testFichierInexistant() {
        assertDoesNotThrow(() -> {
            JSONReader.chargerPiecePersonnalisee("E5", 0, "fichier_inexistant.json", board);
        }, "La méthode ne devrait pas crasher si le fichier est introuvable.");
    }

    @Test
    public void testJSONNonConforme() throws IOException {
        assertDoesNotThrow(() -> {
            JSONReader.chargerPiecePersonnalisee("C3", 0, testFileNonConformePath, board);
        }, "La méthode ne devrait pas crasher même avec du JSON invalide.");
    }

    @Test
    public void testCoordonneesCorrectes() throws IOException {

        JSONReader.chargerPiecePersonnalisee("B2", 1, testFilePath, board);

        Piece piece = board.getPieceAt(2, 2); // B2 = (2,2)
        assertNotNull(piece);
        assertEquals(2, piece.getX());
        assertEquals(2, piece.getY());
        assertEquals(1, piece.getPlayer());
    }
}
