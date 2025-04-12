package projet.utils;

import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import projet.models.ChessBoard;
import projet.models.JSONReader;
import projet.all_pieces.*;

public class Chess {
	
	public Chess() {
		System.out.println("Le jeux est initialisé");
		return;
	}

    public void game(Scanner scanner) {
        ChessBoard chessBoard = new ChessBoard(); // Création de l'échiquier
        Affichage display = new Affichage(chessBoard); // Création de l'affichage
        int currentPlayer = 0; // 0 = Blanc, 1 = Noir

        while (true) {
            display.afficher(); // Affichage de l’échiquier

            System.out.println("\nC'est au tour du joueur " + (currentPlayer == 0 ? "Blanc" : "Noir"));
            System.out.print("Entrez un coup (ex: E2 E4) ou 'exit' pour quitter: ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Le jeu est terminé.");
                break;
            }

            String[] move = input.trim().split(" ");

            if (move.length != 2) {
                System.out.println("Entrée invalide. Veuillez entrer un coup sous la forme: 'E2 E4'.");
                continue;
            }

            String from = move[0].toUpperCase();
            String to = move[1].toUpperCase();

            int result = chessBoard.deplacer(from, to, currentPlayer);

            if (result == 0 || result == 1) {
                // Promotion si besoin
                if (result == 1) {
                    display.handlePromotion(to, scanner);
                }

                // Vérifie si l'adversaire est en échec
                int opponent = (currentPlayer == 0) ? 1 : 0;
                if (chessBoard.isKingInCheck(opponent)) {
                    System.out.println("⚠️  Échec au roi " + (opponent == 0 ? "blanc" : "noir") + " !");
                }

                // Changement de joueur
                currentPlayer = opponent;
            } else if (result == -1) {
                System.out.println("Coup invalide. Essayez encore.");
            }
        }

        scanner.close();
    }

    public static void game(Scanner scanner, ChessBoard chessBoard) {
        Affichage display = new Affichage(chessBoard); // Création de l'affichage
        int currentPlayer = 0; // 0 = Blanc, 1 = Noir

        while (true) {
            display.afficher(); // Affichage de l’échiquier

            System.out.println("\nC'est au tour du joueur " + (currentPlayer == 0 ? "Blanc" : "Noir"));
            System.out.print("Entrez un coup (ex: E2 E4) ou 'exit' pour quitter: ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Le jeu est terminé.");
                break;
            }

            String[] move = input.trim().split(" ");

            if (move.length != 2) {
                System.out.println("Entrée invalide. Veuillez entrer un coup sous la forme: 'E2 E4'.");
                continue;
            }

            String from = move[0].toUpperCase();
            String to = move[1].toUpperCase();

            int result = chessBoard.deplacer(from, to, currentPlayer);

            if (result == 0 || result == 1) {
                // Promotion si besoin
                if (result == 1) {
                    display.handlePromotion(to, scanner);
                }

                // Vérifie si l'adversaire est en échec
                int opponent = (currentPlayer == 0) ? 1 : 0;
                if (chessBoard.isKingInCheck(opponent)) {
                    System.out.println("⚠️  Échec au roi " + (opponent == 0 ? "blanc" : "noir") + " !");
                }

                // Changement de joueur
                currentPlayer = opponent;
            } else if (result == -1) {
                System.out.println("Coup invalide. Essayez encore.");
            }
        }

        scanner.close();
    }
    
    public void gameGUI() {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
    
    public void gamePersonnalise(Scanner scanner) {
    	ChessBoard board = new ChessBoard(); // Création de l'échiquier
        for (int player = 0; player <= 1; player++) {
            System.out.println("\n=== Joueur " + (player + 1) + " : Ajoutez vos pièces personnalisées ===");

            boolean ajouterAutre = true;

            while (ajouterAutre) {
                System.out.print("Entrez la position (ex: D2) : ");
                String position = scanner.nextLine().toUpperCase();

                try {
                    int[] coords = Piece.parseCoordonnees(position);
                    Piece pieceExistante = board.getPieceAt(coords[0], coords[1]);

                    
                    if (pieceExistante instanceof Roi) {	//On vérifie si on écrase pas un roi
                        System.out.println("❌ Vous ne pouvez pas écraser un ROI !");
                        continue;
                    }
                    if ((player == 0 && coords[1] > 4) || (player == 1 && coords[1] < 5)){ //On vérifie si on place dans so coté de l'échequier
                        System.out.println("❌ Vous ne pouvez pas placer en dehors de votre camps");
                        continue;
                    }

                    System.out.print("Entrez le nom du fichier JSON de la pièce (sans extension) : ");
                    String nomFichier = scanner.nextLine().trim();
                    String cheminFichier = "./src/pieces/" + nomFichier + ".json";

                    JSONReader.chargerPiecePersonnalisee(position, player, cheminFichier, board);
                    System.out.println("✅ Pièce ajoutée ou remplacée à " + position);

                } catch (Exception e) {
                    System.out.println("❌ Erreur lors de l'ajout de la pièce : " + e.getMessage());
                }

                System.out.print("Voulez-vous ajouter une autre pièce ? (o/n) : ");
                String reponse = scanner.nextLine().trim();
                ajouterAutre = reponse.equalsIgnoreCase("o");
            }
        }
        game(scanner, board);
    }

    public void gamePersonnaliseGUI() {
        SwingUtilities.invokeLater(() -> {
            ChessBoard board = new ChessBoard();
            board.initializeBoard();  // Initialise avec le plateau de base

            JFrame frame = new JFrame("Ajout des pièces personnalisées");
            frame.setSize(500, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            JLabel instructions = new JLabel("Joueur 1 : Ajoutez vos pièces (blancs)", SwingConstants.CENTER);
            instructions.setFont(new Font("Arial", Font.BOLD, 16));
            frame.add(instructions, BorderLayout.NORTH);

            JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField posField = new JTextField();
            JTextField fichierField = new JTextField();

            inputPanel.add(new JLabel("Position (ex: D2) :"));
            inputPanel.add(posField);
            inputPanel.add(new JLabel("Nom du fichier JSON :"));
            inputPanel.add(fichierField);

            JButton ajouterButton = new JButton("Ajouter la pièce");
            JButton suivantButton = new JButton("Joueur suivant / Lancer le jeu");

            inputPanel.add(ajouterButton);
            inputPanel.add(suivantButton);

            frame.add(inputPanel, BorderLayout.CENTER);

            final int[] joueurActuel = {0};  // 0 = blanc, 1 = noir

            ajouterButton.addActionListener(e -> {
                String pos = posField.getText().trim().toUpperCase();
                String fichier = fichierField.getText().trim();

                try {
                    int[] coords = Piece.parseCoordonnees(pos);
                    Piece pieceExistante = board.getPieceAt(coords[0], coords[1]);

                    if (pieceExistante instanceof Roi) {
                        JOptionPane.showMessageDialog(frame, "❌ Vous ne pouvez pas écraser un ROI !");
                        return;
                    }
                    if ((joueurActuel[0] == 0 && coords[1] > 4) || (joueurActuel[0] == 1 && coords[1] < 5)) {
                        JOptionPane.showMessageDialog(frame, "❌ Vous ne pouvez pas placer hors de votre camp !");
                        return;
                    }

                    String chemin = "./src/pieces/" + fichier + ".json";
                    JSONReader.chargerPiecePersonnalisee(pos, joueurActuel[0], chemin, board);
                    JOptionPane.showMessageDialog(frame, "✅ Pièce ajoutée avec succès à " + pos);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "❌ Erreur : " + ex.getMessage());
                }
            });

            suivantButton.addActionListener(e -> {
                if (joueurActuel[0] == 0) {
                    joueurActuel[0] = 1;
                    instructions.setText("Joueur 2 : Ajoutez vos pièces (noirs)");
                    posField.setText("");
                    fichierField.setText("");
                } else {
                    frame.dispose();  // Fermer la fenêtre d'ajout
                    new ChessGUI(board);  // Lancer le jeu avec les pièces personnalisées
                }
            });

            frame.setVisible(true);
        });
    }

}
