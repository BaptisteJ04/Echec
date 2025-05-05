package projet.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import projet.all_pieces.Piece;
import projet.all_pieces.PiecePersonnalisee;

/**
 * Classe utilitaire permettant de lire une pièce personnalisée depuis un fichier JSON
 * et de l'ajouter à un échiquier.
 */
public class JSONReader {

	/**
	 * Charge une pièce personnalisée depuis un fichier JSON et l'ajoute à l'échiquier.
	 *
	 * @param position       La position de la pièce en notation échec (ex: "D4")
	 * @param player         Le joueur auquel appartient la pièce (0 = blanc, 1 = noir)
	 * @param cheminFichier  Le chemin vers le fichier JSON décrivant la pièce
	 * @param board          L'échiquier sur lequel ajouter la pièce
	 */
	public static void chargerPiecePersonnalisee(String position, int player, String cheminFichier, ChessBoard board) {
		try {
			// Lecture du contenu du fichier JSON en tant que chaîne de caractères
			String contenu = new String(Files.readAllBytes(Paths.get(cheminFichier)));

			// Création d'un objet JSON à partir du contenu
			JSONObject obj = new JSONObject(contenu);

			// Extraction des informations de base
			String name = obj.getString("name");
			String unicodeW = obj.getString("unicodeWhite");
			String unicodeB = obj.getString("unicodeBlack");

			// Récupération du chemin de l’image correspondant au joueur
			String imagePath = player == 0 ? obj.getString("imageWhite") : obj.getString("imageBlack");

			// Chargement des déplacements et captures en fonction du joueur
			List<int[]> moves;
			List<int[]> captures;
			if (player == 0) {
				moves = jsonArrayToList(obj.getJSONArray("movesWhite"));
				captures = jsonArrayToList(obj.getJSONArray("capturesWhite"));
			} else {
				moves = jsonArrayToList(obj.getJSONArray("movesBlack"));
				captures = jsonArrayToList(obj.getJSONArray("capturesBlack"));
			}

			// Lecture des capacités spéciales
			boolean deplacementLong = obj.getBoolean("deplacementLong"); // Peut se déplacer sur plusieurs cases
			boolean peutSauter = obj.getBoolean("peutSauter"); // Peut sauter au-dessus d'autres pièces

			// Conversion de la position (ex: "E4") en coordonnées internes [x, y]
			int[] coords = Piece.parseCoordonnees(position);

			// Création de la nouvelle pièce personnalisée
			Piece piece = new PiecePersonnalisee(
				coords[0], coords[1], player, imagePath, moves, captures,
				new int[] { unicodeW.codePointAt(0), unicodeB.codePointAt(0) },
				name, peutSauter, deplacementLong
			);

			// Ajout de la pièce sur le plateau
			board.addPiece(piece);

			System.out.println("Pièce personnalisée ajoutée : " + name);

		} catch (IOException e) {
			System.err.println("Erreur de lecture du fichier : " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur lors du parsing JSON ou création de la pièce : " + e.getMessage());
		}
	}

	/**
	 * Convertit un tableau JSON contenant des couples [x, y] en liste de tableaux int[].
	 *
	 * @param array JSONArray contenant des coordonnées
	 * @return Liste de couples (x, y) sous forme de tableaux int[]
	 */
	private static List<int[]> jsonArrayToList(JSONArray array) {
		List<int[]> liste = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			JSONArray pair = array.getJSONArray(i);
			int x = pair.getInt(0);
			int y = pair.getInt(1);
			liste.add(new int[] { x, y });
		}
		return liste;
	}
}
