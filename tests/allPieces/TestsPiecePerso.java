package tests.allPieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import projet.all_pieces.*;
import projet.models.ChessBoard;

public class TestsPiecePerso {

    private ChessBoard board;
    private PiecePersonnalisee piece;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        board.emptyChessBoard();

        // Mouvement diagonaux de 1 case
        List<int[]> moves = Arrays.asList(new int[]{1, 1}, new int[]{-1, -1}, new int[]{1, -1}, new int[]{-1, 1});
        List<int[]> captures = Arrays.asList(new int[]{1, 1}, new int[]{-1, -1}, new int[]{1, -1}, new int[]{-1, 1});
        int[] affichage = {0x2605, 0x2606}; //affichage terminal pour l'exemple

        piece = new PiecePersonnalisee(4, 4, 0, "path/to/img.png", moves, captures, affichage, "Etoile", false, false); //Se deplace d'une case et ne peut pas sauter
        board.addPiece(piece);
    }

    @Test
    public void testDeplacementValide() {
        assertTrue(piece.isValidDeplacement(new int[]{5, 5}, board));
        assertTrue(piece.isValidDeplacement(new int[]{3, 3}, board));
        assertFalse(piece.isValidDeplacement(new int[]{6, 6}, board)); // Déplassement long
        assertFalse(piece.isValidDeplacement(new int[]{4, 5}, board)); // Déplassement non diagonal
    }

    @Test
    public void testCaptureValide() {
        Tour ennemi1 = new Tour(5, 5, 1);
        Tour ennemi2 = new Tour(3, 3, 1);
        board.addPiece(ennemi1);
        board.addPiece(ennemi2);

        assertTrue(piece.isValidCatch(new int[]{5, 5}, board));
        assertTrue(piece.isValidCatch(new int[]{3, 3}, board));
    }

    @Test
    public void testCaptureInvalideMemeCamp() {
        Tour allie = new Tour(5, 5, 0);
        board.addPiece(allie);
        assertFalse(piece.isValidCatch(new int[]{5, 5}, board));
    }

    @Test
    public void testDeplacementLongAvecObstacleSansSaut() {
    	List<int[]> moves = Arrays.asList(new int[]{0, 1});
    	List<int[]> captures = Arrays.asList(new int[]{0, 1});

        piece = new PiecePersonnalisee(4, 1, 0, "", moves, captures, new int[]{0x2605, 0x2606}, "Ligne", false, true);
        board.emptyChessBoard();
        board.addPiece(piece);

        board.addPiece(new Tour(4, 3, 0)); // Pièce alliée sur le chemin
        List<int[]> possible = piece.getPossibleMoves(board);
        assertTrue(possible.stream().noneMatch(m -> m[0] == 4 && m[1] == 4));
    }

    @Test
    public void testDeplacementLongAvecSaut() {
    	List<int[]> moves = Arrays.asList(new int[]{0, 1});
    	List<int[]> captures = Arrays.asList(new int[]{0, 1});
        piece = new PiecePersonnalisee(4, 1, 0, "", moves, captures, new int[]{0x2605, 0x2606}, "Saut", true, true);
        board.emptyChessBoard();
        board.addPiece(piece);

        board.addPiece(new Tour(4, 2, 1)); // Obstacle
        List<int[]> possible = piece.getPossibleMoves(board);
        assertTrue(possible.stream().anyMatch(m -> m[0] == 4 && m[1] == 3)); // A pu sauter
    }

    @Test
    public void testGetPossibleMovesEtCaptures() {
        board.addPiece(new Tour(5, 5, 1)); // Ennemie
        board.addPiece(new Tour(3, 3, 0)); // Alliée
        List<int[]> moves = piece.getPossibleMoves(board);
        List<int[]> captures = piece.getPossibleCaptures(board);
        assertTrue(moves.stream().anyMatch(m -> m[0] == 3 && m[1] == 5));
        assertFalse(moves.stream().anyMatch(m -> m[0] == 5 && m[1] == 5));
        assertFalse(moves.stream().anyMatch(m -> m[0] == 3 && m[1] == 3));

        assertTrue(captures.stream().anyMatch(c -> c[0] == 5 && c[1] == 5));
        assertFalse(captures.stream().anyMatch(c -> c[0] == 3 && c[1] == 3));
    }

    @Test
    public void testAffichageUnicode() {
        assertEquals(0x2605, piece.getAffichage()); // Étoile pleine
        assertEquals("★", piece.toString());
    }

    @Test
    public void testDeplacementEffectif() {
        piece.deplacement(new int[]{6, 6});
        assertEquals(6, piece.getX());
        assertEquals(6, piece.getY());
    }
}
