package projet.utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import projet.all_pieces.*;
import projet.models.*;

/**
 * Classe principale pour gérer l'interface graphique du jeu d'échecs.
 * Elle affiche l'échiquier, gère les interactions utilisateur,
 * ainsi que le mode de jeu choisi (classique/personnalisé, IA/joueur).
 */
public class ChessGUI extends JFrame {
    private static final long serialVersionUID = 1L;

   
    private ChessBoard board;
    private JButton[][] buttons;
    private JLabel scoreLabel;
    private int currentPlayer = 0;
    private boolean vsAI;
    private String selected = null;
    private List<int[]> possibleMoves, possibleCaptures;
    private int scoreBlanc = 0, scoreNoir = 0;
    private MinMax ia;

    /**
     * Constructeur principal de l'interface graphique.
     * Initialise le plateau selon le mode de jeu sélectionné.
     */
    public ChessGUI(ModeJeu mode) {
        switch (mode) {
            case CLASSIQUE_VS_JOUEUR:
            case CLASSIQUE_VS_IA:
                board = new ChessBoard();
                board.initializeBoard();
                break;
            case PERSONNALISE_VS_JOUEUR:
            case PERSONNALISE_VS_IA:
                board = creerPlateauPersonnalise();
                break;
            default:
                board = new ChessBoard();
                board.initializeBoard();
        }

        vsAI = (mode == ModeJeu.CLASSIQUE_VS_IA || mode == ModeJeu.PERSONNALISE_VS_IA);
        ia = vsAI ? new MinMax(1) : null;

        initGUI();        // Construction de l’interface graphique
        updateBoard();    // Mise à jour initiale de l’échiquier
        setVisible(true);
    }

    /**
     * Permet aux joueurs de créer un plateau personnalisé via des boîtes de dialogue.
     */
    private ChessBoard creerPlateauPersonnalise() {
        ChessBoard board = new ChessBoard();
        for (int player = 0; player <= 1; player++) {
            boolean continuer = true;
            while (continuer) {
                String pos = JOptionPane.showInputDialog(this, "Joueur " + (player + 1) + " - Position (ex: D2) :");
                if (pos == null) break;
                pos = pos.toUpperCase();

                try {
                    int[] coords = Piece.parseCoordonnees(pos);
                    if ((player == 0 && coords[1] > 4) || (player == 1 && coords[1] < 5)) {
                        JOptionPane.showMessageDialog(this, "❌ Hors de votre camp !");
                        continue;
                    }

                    if (board.getPieceAt(coords[0], coords[1]) instanceof Roi) {
                        JOptionPane.showMessageDialog(this, "❌ Impossible d’écraser un roi !");
                        continue;
                    }

                    String file = JOptionPane.showInputDialog(this, "Nom du fichier JSON (sans .json) :");
                    if (file == null) break;
                    String chemin = "./src/pieces/" + file + ".json";

                    JSONReader.chargerPiecePersonnalisee(pos, player, chemin, board);
                    JOptionPane.showMessageDialog(this, "✅ Pièce ajoutée à " + pos);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "❌ Erreur : " + e.getMessage());
                }

                int choice = JOptionPane.showConfirmDialog(this, "Ajouter une autre pièce ?", "Continuer ?", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) continuer = false;
            }
        }
        return board;
    }

    /**
     * Initialise l’interface graphique (fenêtre, boutons, panneaux...).
     */
    private void initGUI() {
        setTitle("Jeu d'Échecs");
        setSize(1000, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Affichage du score en haut
        scoreLabel = new JLabel("Score - Blanc: 0 | Noir: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel, BorderLayout.NORTH);

        // Création de la grille d’échecs (8x8)
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        buttons = new JButton[8][8];
        for (int y = 8; y >= 1; y--) {
            for (int x = 1; x <= 8; x++) {
                JButton button = new JButton();
                int finalX = x, finalY = y;
                button.addActionListener(e -> handleClick(finalX, finalY));
                buttons[8 - y][x - 1] = button;
                boardPanel.add(button);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Boutons de contrôle en bas
        JPanel controlPanel = new JPanel();
        JButton forfeitButton = new JButton("FF15");
        JButton quitButton = new JButton("Quitter");

        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Quitter la partie ?", "Quitter", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        forfeitButton.addActionListener(e -> handleForfeit());

        controlPanel.add(quitButton);
        controlPanel.add(forfeitButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Gère le clic d’un utilisateur sur une case de l’échiquier.
     */
    private void handleClick(int x, int y) {
        String pos = convertToPosition(x, y);

        if (selected == null) {
            // Sélection d’une pièce
            Piece piece = board.getPieceAt(x, y);
            if (piece != null && piece.getPlayer() == currentPlayer) {
                selected = pos;
                possibleMoves = piece.getPossibleMoves(board);
                possibleCaptures = piece.getPossibleCaptures(board);
                highlightPossibleMoves(possibleMoves);
                highlightPossibleCaptures(possibleCaptures);
            }
        } else {
            // Tentative de déplacement
            int result = board.deplacer(selected, pos, currentPlayer);
            if (result == 0 || result == 1) {
                if (result == 1) {
                    // Promotion si pion atteint la dernière ligne
                    Piece piece = board.board.get(pos);
                    if (piece instanceof Pion) {
                        String[] options = {"Dame", "Tour", "Fou", "Cavalier"};
                        int choice = JOptionPane.showOptionDialog(this, "Promotion :", "Choisissez une pièce",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (choice >= 0) {
                            Piece nouvelle = ((Pion) piece).promote(choice + 1);
                            board.changePiece(pos, nouvelle);
                        }
                    }
                }

                if (board.isKingInCheck(1 - currentPlayer)) {
                    JOptionPane.showMessageDialog(this, "⚠️ Échec au roi " + (1 - currentPlayer == 0 ? "blanc" : "noir") + " !");
                }

                currentPlayer = 1 - currentPlayer;
                updateBoard();

                // L’IA joue automatiquement après le joueur
                if (vsAI && currentPlayer == 1) {
                    SwingUtilities.invokeLater(() -> jouerCoupIA());
                }
            } else {
                // Mauvais clic, nouvelle sélection si c’est une pièce du joueur
                Piece clickedPiece = board.getPieceAt(x, y);
                if (clickedPiece != null && clickedPiece.getPlayer() == currentPlayer) {
                    selected = pos;
                    possibleMoves = clickedPiece.getPossibleMoves(board);
                    possibleCaptures = clickedPiece.getPossibleCaptures(board);
                    highlightPossibleMoves(possibleMoves);
                    highlightPossibleCaptures(possibleCaptures);
                } else {
                    JOptionPane.showMessageDialog(this, "Coup invalide.");
                    selected = null;
                }
            }
            selected = null;
        }
    }

    /**
     * Fait jouer un coup à l’IA.
     */
    private void jouerCoupIA() {
        if (ia == null) return;

        String[] coup = ia.meilleurCoup(board);
        if (coup == null) {
            JOptionPane.showMessageDialog(this, "L'IA ne peut pas jouer.");
            handleForfeit();
            return;
        }

        board.deplacer(coup[0], coup[1], 1);

        if (board.isKingInCheck(0)) {
            JOptionPane.showMessageDialog(this, "⚠️ Échec au roi blanc !");
        }

        currentPlayer = 0;
        updateBoard();
    }

    /**
     * Rafraîchit l’affichage de l’échiquier selon l’état actuel du plateau.
     */
    private void updateBoard() {
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                JButton button = buttons[8 - y][x - 1];
                Piece piece = board.getPieceAt(x, y);

                if (piece != null) {
                    ImageIcon icon = new ImageIcon(piece.getPathImage());
                    button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
                } else {
                    button.setIcon(null);
                }

                button.setBackground((x + y) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99));
            }
        }
    }

    /**
     * Gère un abandon de la part d’un joueur.
     */
    private void handleForfeit() {
        if (currentPlayer == 0) scoreNoir++;
        else scoreBlanc++;

        updateScoreLabel();
        JOptionPane.showMessageDialog(this, "Le joueur " + (currentPlayer == 0 ? "noir" : "blanc") + " gagne par forfait !");
        board = new ChessBoard();
        board.initializeBoard();
        currentPlayer = 0;
        updateBoard();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score - Blanc: " + scoreBlanc + " | Noir: " + scoreNoir);
    }

    private String convertToPosition(int x, int y) {
        return (char) ('A' + x - 1) + Integer.toString(y);
    }

    private String convertToPosition(int[] coords) {
        return convertToPosition(coords[0], coords[1]);
    }

    /**
     * Surligne les cases accessibles pour un déplacement (en vert).
     */
    private void highlightPossibleMoves(List<int[]> possibleMoves) {
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                JButton button = buttons[8 - y][x - 1];
                String pos = convertToPosition(x, y);
                boolean isPossibleMove = false;

                for (int[] move : possibleMoves) {
                    String possiblePos = convertToPosition(move);
                    if (pos.equals(possiblePos)) {
                        isPossibleMove = true;
                        break;
                    }
                }

                if (isPossibleMove) {
                    button.setBackground(new Color(105, 212, 70)); // Vert
                } else {
                    button.setBackground((x + y) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99));
                }
            }
        }
    }

    /**
     * Surligne les cases où une capture est possible (en rouge).
     */
    private void highlightPossibleCaptures(List<int[]> possibleCaptures) {
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                JButton button = buttons[8 - y][x - 1];
                String pos = convertToPosition(x, y);
                boolean isCaptureMove = false;

                for (int[] capture : possibleCaptures) {
                    String capturePos = convertToPosition(capture);
                    if (pos.equals(capturePos)) {
                        isCaptureMove = true;
                        break;
                    }
                }

                if (isCaptureMove) {
                    button.setBackground(new Color(203, 56, 12)); // Rouge
                }
            }
        }
    }
}
