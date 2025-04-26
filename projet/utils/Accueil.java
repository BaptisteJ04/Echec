package projet.utils;

import java.util.Scanner;
import javax.swing.*;

import projet.utils.ChessGUI.ModeJeu;

import java.awt.*;

public class Accueil {

    public static void afficherMenu(Scanner scanner) {
        Chess chess = new Chess();

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

        System.out.println("== Interface ==");
        System.out.println("1. Terminal");
        System.out.println("2. GUI");
        System.out.print("Votre choix : ");
        int interfaceChoisie = scanner.nextInt();

        scanner.nextLine(); // Consommer la nouvelle ligne

        if (interfaceChoisie == 2) {
            afficherMenuGUI(null); // rediriger vers la GUI
            return;
        }

        // Choix via switch
        switch (typeJeu) {
            case 1:
                chess.lancer(modeJoueur == 1 ? Chess.ModeJeu.CLASSIQUE_VS_IA : Chess.ModeJeu.CLASSIQUE_VS_JOUEUR);
                break;
            case 2:
                chess.lancer(modeJoueur == 1 ? Chess.ModeJeu.PERSONNALISE_VS_IA : Chess.ModeJeu.PERSONNALISE_VS_JOUEUR);
                break;
            default:
                System.out.println("Type de jeu invalide.");
        }
    }

    public static void afficherMenuGUI(Scanner scanner) {
        JFrame frame = new JFrame("Accueil - Jeu d'Échecs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Bienvenue dans le jeu d'échecs !", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(10, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Choix du nombre de joueurs
        JRadioButton vsIA = new JRadioButton("1 Joueur (vs IA)");
        JRadioButton vsJoueur = new JRadioButton("2 Joueurs");
        ButtonGroup modeGroupe = new ButtonGroup();
        modeGroupe.add(vsIA);
        modeGroupe.add(vsJoueur);
        vsIA.setSelected(true);

        // Choix du mode de jeu
        JRadioButton classique = new JRadioButton("Classique");
        JRadioButton personnalise = new JRadioButton("Personnalisé");
        ButtonGroup typeGroupe = new ButtonGroup();
        typeGroupe.add(classique);
        typeGroupe.add(personnalise);
        classique.setSelected(true);

        JButton lancer = new JButton("Lancer le jeu");
        lancer.addActionListener(e -> {
            frame.dispose();

            boolean isIA = vsIA.isSelected();
            boolean isClassique = classique.isSelected();

            ModeJeu modeJeu;
            if (isClassique && isIA) modeJeu = ModeJeu.CLASSIQUE_VS_IA;
            else if (isClassique) modeJeu = ModeJeu.CLASSIQUE_VS_JOUEUR;
            else if (isIA) modeJeu = ModeJeu.PERSONNALISE_VS_IA;
            else modeJeu = ModeJeu.PERSONNALISE_VS_JOUEUR;

            new ChessGUI(modeJeu); 
        });

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
