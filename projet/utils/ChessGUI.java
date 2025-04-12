package projet.utils;

import javax.swing.*;

import projet.all_pieces.Piece;
import projet.all_pieces.Pion;
import projet.models.ChessBoard;

import java.awt.*;
import java.util.List;

public class ChessGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private int scoreBlanc = 0;
    private int scoreNoir = 0;
    private JLabel scoreLabel;
	private ChessBoard board;
    private JButton[][] buttons;
    private int currentPlayer = 0;
    private String selected = null;
    private List<int[]> possibleMoves = null;  // Liste pour stocker les déplacements possibles sous forme de coordonnées
    private List<int[]> possibleCaptures = null;  // Liste pour stocker les captures possibles

    // Constructeur de la classe ChessGUI
    public ChessGUI() {
        board = new ChessBoard();
        buttons = new JButton[8][8];

        // Initialiser la fenêtre
        setTitle("Jeu d'Échecs");
        setSize(1200,1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Score panel en haut
        scoreLabel = new JLabel("Score - Blanc: 0 | Noir: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(scoreLabel, BorderLayout.NORTH);


        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Création des boutons de l'échiquier
        for (int y = 8; y >= 1; y--) {
            for (int x = 1; x <= 8; x++) {
                JButton button = new JButton();
                int finalX = x;
                int finalY = y;

                // Ajout un écouteur de clic
                button.addActionListener(e -> handleClick(finalX, finalY));
                buttons[8 - y][x - 1] = button;
                boardPanel.add(button);
            }
        }

        // Créer un panel pour le bouton de forfait
        JPanel controlPanel = new JPanel();
        JButton forfeitButton = new JButton("FF15");
        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter ?", "Quitter", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        controlPanel.add(quitButton);

        
        // Action à effectuer quand le joueur déclare forfait
        forfeitButton.addActionListener(e -> handleForfeit());
        controlPanel.add(forfeitButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);  // Ajout panneau avec bouton ff
        updateBoard();  // Mettre à jour l'affichage des pièces sur l'échiquier
        setVisible(true);  // Afficher la fenêtre
    }
    
    public ChessGUI(ChessBoard board) {
        this.board = board;
        buttons = new JButton[8][8];

        // Initialiser la fenêtre
        setTitle("Jeu d'Échecs");
        setSize(1200,1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Score panel en haut
        scoreLabel = new JLabel("Score - Blanc: 0 | Noir: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(scoreLabel, BorderLayout.NORTH);


        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Création des boutons de l'échiquier
        for (int y = 8; y >= 1; y--) {
            for (int x = 1; x <= 8; x++) {
                JButton button = new JButton();
                int finalX = x;
                int finalY = y;

                // Ajout un écouteur de clic
                button.addActionListener(e -> handleClick(finalX, finalY));
                buttons[8 - y][x - 1] = button;
                boardPanel.add(button);
            }
        }

        // Créer un panel pour le bouton de forfait
        JPanel controlPanel = new JPanel();
        JButton forfeitButton = new JButton("FF15");
        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter ?", "Quitter", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        controlPanel.add(quitButton);

        
        // Action à effectuer quand le joueur déclare forfait
        forfeitButton.addActionListener(e -> handleForfeit());
        controlPanel.add(forfeitButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);  // Ajout panneau avec bouton ff
        updateBoard();  // Mettre à jour l'affichage des pièces sur l'échiquier
        setVisible(true);  // Afficher la fenêtre
    }

    // Gestion du clic sur une case de l'échiquier
    private void handleClick(int x, int y) {
        String pos = convertToPosition(x, y);

        if (selected == null) {
            // Sélectionne la pièce
            Piece piece = board.getPieceAt(x, y);
            if (piece != null && piece.getPlayer() == currentPlayer) {
                selected = pos;

                // Affiche les déplacements de la pièce sélectionnée
                possibleMoves = getPossibleMovesForPiece(piece);
                possibleCaptures = getPossibleCapturesForPiece(piece); 
                highlightPossibleMoves(possibleMoves);			// Surbrillance des moves possibles
                highlightPossibleCaptures(possibleCaptures);  // Surbrillance des captures possibles
            }
        } else {
            // Déplacement
            int result = board.deplacer(selected, pos, currentPlayer);

            if (result == 0 || result == 1) {
                if (result == 1) {
                    // Promotion d'un pion
                    String[] options = {"Dame", "Tour", "Fou", "Cavalier"};
                    int choice = JOptionPane.showOptionDialog(
                        this,
                        "Promotion du pion. Choisissez une pièce :",
                        "Promotion",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                    );

                    if (choice >= 0 && choice <= 3) {
                        Piece piece = board.board.get(pos);
                        Pion pion = (Pion) piece;
                        Piece nouvelle = pion.promote(choice + 1); // Promotion selon le choix
                        board.changePiece(pos, nouvelle);
                    }
                }

                // Vérifier si le roi adverse est en échec
                if (board.isKingInCheck(1 - currentPlayer)) {
                    JOptionPane.showMessageDialog(this, "Échec au roi " + (1 - currentPlayer == 0 ? "Blanc" : "Noir") + " !");
                }

                // Changer de joueur après un coup valide
                currentPlayer = 1 - currentPlayer;
            } else {
                JOptionPane.showMessageDialog(this, "Coup invalide.");
            }

            selected = null;
            possibleMoves = null;  // Réinitialiser les déplacements possibles
            possibleCaptures = null;  // Réinitialiser les captures possibles
            updateBoard();  // Mettre à jour l'affichage
        }
    }

    private String convertToPosition(int x, int y) {
        return (char) ('A' + x - 1) + Integer.toString(y);
    }

    private String convertToPosition(int[] coordinates) {
        return (char) ('A' + coordinates[0] - 1) + Integer.toString(coordinates[1]);
    }

    private List<int[]> getPossibleMovesForPiece(Piece piece) {
        return piece.getPossibleMoves(board);  
    }

    private List<int[]> getPossibleCapturesForPiece(Piece piece) {
        return piece.getPossibleCaptures(board);  
    }

    // Afficher les cases où la pièce peut se déplacer
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
                    button.setBackground(new Color(105, 212, 70)); //Mise en vert de la case 
                } else {
                    button.setBackground((x + y) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99));  //Sinon couleur de base de l'échequier
                }
            }
        }
    }

    // Afficher les cases où une pièce peut être capturée
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
                    button.setBackground(new Color(203, 56, 12));		//Mise en rouge de la case 
                }
            }
        }
    }

    // Affichage des pièces sur l'échiquier
    private void updateBoard() {
        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                JButton button = buttons[8 - y][x - 1];
                Piece piece = board.getPieceAt(x, y);  //On prend la pièce à la position acutelle
                
                if (piece != null) {	//Si il y a une pièce sur la case
                    String imagePath = piece.getPathImage();
                    ImageIcon icon = new ImageIcon(imagePath); 
                    Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));	//Ajoute une icon au bouton de la case, icon = image de la pièce
                    button.setText("");
                } else {
                    button.setIcon(null);
                    button.setText("");
                }

                button.setBackground((x + y) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99)); //On colorise l'échequier
            }
        }
    }
    
    private void updateScoreLabel() {
        scoreLabel.setText("Score - Blanc: " + scoreBlanc + " | Noir: " + scoreNoir);
    }


    private void handleForfeit() {
        if (currentPlayer == 0) {
            scoreNoir++;
        } else {
            scoreBlanc++;
        }

        updateScoreLabel();

        String winner = currentPlayer == 0 ? "Noir" : "Blanc";
        JOptionPane.showMessageDialog(this, "Le joueur " + winner + " a gagné par forfait !");
        
        // Reset du plateau après forfait 
        board = new ChessBoard();
        board.initializeBoard();
        selected = null;
        currentPlayer = 0;
        updateBoard();
    }

}
