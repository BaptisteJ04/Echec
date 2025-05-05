package projet.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import projet.all_pieces.Piece;
import projet.all_pieces.PiecePersonnalisee;

public class JSONReader {

	public static void chargerPiecePersonnalisee(String position, int player, String cheminFichier, ChessBoard board) {
		try {
			// Lire le JSON
			String contenu = new String(Files.readAllBytes(Paths.get(cheminFichier)));

			// Créer l'objet JSON de la classe JSONOject
			JSONObject obj = new JSONObject(contenu);

			String name = obj.getString("name");
			String unicodeW = obj.getString("unicodeWhite");
			String unicodeB = obj.getString("unicodeBlack");

			// Récupérer les chemins des images en fonction du joueur
			String imagePath = player == 0 ? obj.getString("imageWhite") : obj.getString("imageBlack");
			List<int[]> moves;
			List<int[]> captures;
			// Parsing des déplacements et captures
			if (player == 0) {
				moves = jsonArrayToList(obj.getJSONArray("movesWhite"));
				captures = jsonArrayToList(obj.getJSONArray("capturesWhite"));
			} else {
				moves = jsonArrayToList(obj.getJSONArray("movesBlack"));
				captures = jsonArrayToList(obj.getJSONArray("capturesBlack"));
			}

			// Récupérer les champs de déplacement
			boolean deplacementLong = obj.getBoolean("deplacementLong");
			boolean peutSauter = obj.getBoolean("peutSauter");

			// Convertir position donnée en parametre en int[]
			int[] coords = Piece.parseCoordonnees(position);

			// Créer la pièce personnalisée
			Piece piece = new PiecePersonnalisee(coords[0], coords[1], player, imagePath, moves, captures,
					new int[] { unicodeW.codePointAt(0), unicodeB.codePointAt(0) }, name, peutSauter, deplacementLong);
			board.addPiece(piece);

			System.out.println("Pièce personnalisée ajoutée : " + name);

		} catch (IOException e) {
			System.err.println("Erreur de lecture du fichier : " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur lors du parsing JSON ou création de la pièce : " + e.getMessage());
		}
	}

	// Converti un JSONArray en coordonnées 
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
