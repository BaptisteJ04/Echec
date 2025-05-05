package projet.utils;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

/*##################################### Classe Accueil #####################################
Cette classe gère l'écran d'accueil permettant à l'utilisateur de choisir le mode de jeu.
Elle propose deux interfaces : 
 - Une interface en mode terminal
 - Une interface en mode graphique (GUI)
Chaque interface permet de sélectionner :
  • Le nombre de joueurs (1 joueur vs IA ou 2 joueurs)
  • Le type de jeu (Classique ou Personnalisé)
*/
public class Accueil {

    /**
     * afficherMenu(Scanner)
     * Affiche un menu en mode terminal pour choisir le mode de jeu.
     * Demande successivement :
     *  - L'interface souhaitée (Terminal ou GUI)
     *  - Le nombre de joueurs (1 vs IA ou 2 joueurs)
     *  - Le type de jeu (Classique ou Personnalisé)
     * En fonction des choix, lance la partie via la méthode lancer() de la classe Chess.
     * @param scanner instance Scanner pour lire les entrées utilisateur.
     */
    public static void afficherMenu(Scanner scanner) {
        Chess chess = new Chess();

        System.out.println("== Interface ==");
        System.out.println("1. Terminal");
        System.out.println("2. GUI");
        System.out.print("Votre choix : ");
        int interfaceChoisie = scanner.nextInt();

        // Si l'utilisateur choisit l'interface GUI, on redirige vers le menu GUI
        if (interfaceChoisie == 2) {
            afficherMenuGUI(null);
            return;
        }

        System.out.println("== Mode de jeu ==");
        System.out.println("1. 1 Joueur (vs IA)");
        System.out.println("2. 2 Joueurs");
        System.out.print("Votre choix : ");
        int modeJoueur = scanner.nextInt();

        System.out.println("== Type de jeu ==");
        System.out.println("1. Classique");
        System.out.println("2. Personnalisé");
        System.out.print("Votre choix : ");
        int typeJeu = scanner.nextInt();

        scanner.nextLine(); // Consommer la nouvelle ligne

        // Détermine le mode de jeu en fonction des choix
        switch (typeJeu) {
            case 1:
                chess.lancer(modeJoueur == 1 ? ModeJeu.CLASSIQUE_VS_IA : ModeJeu.CLASSIQUE_VS_JOUEUR);
                break;
            case 2:
                chess.lancer(modeJoueur == 1 ? ModeJeu.PERSONNALISE_VS_IA : ModeJeu.PERSONNALISE_VS_JOUEUR);
                break;
            default:
                System.out.println("Type de jeu invalide.");
        }
    }

    /**
     * afficherMenuGUI(Scanner)
     * Affiche une fenêtre GUI pour choisir le mode de jeu.
     * L'interface utilise des boutons radio pour sélectionner :
     *  - Le nombre de joueurs (1 joueur vs IA ou 2 joueurs)
     *  - Le type de jeu (Classique ou Personnalisé)
     * Lorsqu'on clique sur "Lancer le jeu", la fenêtre se ferme et une nouvelle instance
     * de ChessGUI est lancée avec le mode de jeu choisi.
     * @param scanner (peut être null dans ce contexte, car on utilise des composants Swing)
     */
    public static void afficherMenuGUI(Scanner scanner) {
        // Création de la fenêtre d'accueil
        JFrame frame = new JFrame("Accueil - Jeu d'Échecs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Titre de la fenêtre
        JLabel title = new JLabel("Bienvenue dans le jeu d'échecs !", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        // Panel central avec une grille pour afficher les options
        JPanel panel = new JPanel(new GridLayout(10, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Choix du nombre de joueurs via des boutons radio
        JRadioButton vsIA = new JRadioButton("1 Joueur (vs IA)");
        JRadioButton vsJoueur = new JRadioButton("2 Joueurs");
        ButtonGroup modeGroupe = new ButtonGroup();
        modeGroupe.add(vsIA);
        modeGroupe.add(vsJoueur);
        vsIA.setSelected(true);

        // Choix du mode de jeu (Classique ou Personnalisé)
        JRadioButton classique = new JRadioButton("Classique");
        JRadioButton personnalise = new JRadioButton("Personnalisé");
        ButtonGroup typeGroupe = new ButtonGroup();
        typeGroupe.add(classique);
        typeGroupe.add(personnalise);
        classique.setSelected(true);

        // Bouton de lancement qui récupère les choix et lance ChessGUI avec le mode approprié
        JButton lancer = new JButton("Lancer le jeu");
        lancer.addActionListener(e -> {
            frame.dispose();

            boolean isIA = vsIA.isSelected();
            boolean isClassique = classique.isSelected();

            ModeJeu modeJeu;
            if (isClassique && isIA)
                modeJeu = ModeJeu.CLASSIQUE_VS_IA;
            else if (isClassique)
                modeJeu = ModeJeu.CLASSIQUE_VS_JOUEUR;
            else if (isIA)
                modeJeu = ModeJeu.PERSONNALISE_VS_IA;
            else
                modeJeu = ModeJeu.PERSONNALISE_VS_JOUEUR;

            new ChessGUI(modeJeu);
        });

        // Ajout des composants au panel
        panel.add(new JLabel("Choisissez le nombre de joueurs :"));
        panel.add(vsIA);
        panel.add(vsJoueur);
        panel.add(new JLabel("Choisissez le mode de jeu :"));
        panel.add(classique);
        panel.add(personnalise);
        panel.add(lancer);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
