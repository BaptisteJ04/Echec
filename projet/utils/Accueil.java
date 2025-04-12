package projet.utils;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

public class Accueil {
    
    // Méthode pour afficher l'écran d'accueil et obtenir le choix de l'utilisateur
    public static void afficherMenu(Scanner scanner) {
        System.out.println("Bienvenue dans le jeu d'échecs !");
        System.out.println("Choisissez un mode de jeu :");
        System.out.println("1. Mode classique terminal (Pièces de base)");
        System.out.println("2. Mode personnalisé terminal (Ajoutez vos pièces)");
        System.out.println("3. Mode classique GUI (Pièces de base)");
        System.out.println("4. Mode personnalisé GUI (Ajoutez vos pièces)");

        int choix = scanner.nextInt();
        scanner.nextLine(); 

        Chess chess = new Chess();
        
        switch (choix) {
            case 1:
                System.out.println("Vous avez choisi le mode classique terminal.");
                chess.game(scanner);  // Lance le jeu en mode classique
                break;
            case 2:
                System.out.println("Vous avez choisi le mode personnalisé terminal.");
                chess.gamePersonnalise(scanner);  // Lance le jeu en mode personnalisé
                break;
            case 3:
                System.out.println("Vous avez choisi le mode classique GUI.");
                chess.gameGUI();  // Lance le jeu en mode personnalisé
                break;
            default:
                System.out.println("Choix invalide. Veuillez sélectionner 1 ou 2.");
                afficherMenu(scanner);  // Affiche à nouveau le menu si le choix est invalide
                break;
        }
    }
    
    public static void afficherMenuGUI(Scanner scanner) {
        JFrame frame = new JFrame("Accueil - Jeu d'Échecs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Bienvenue dans le jeu d'échecs !", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        Chess chess = new Chess();

        JButton classicTerminal = new JButton("1. Mode classique terminal (Pièces de base)");
        classicTerminal.addActionListener(e -> {
            frame.dispose();
            chess.game(scanner);
        });

        JButton customTerminal = new JButton("2. Mode personnalisé terminal (Ajoutez vos pièces)");
        customTerminal.addActionListener(e -> {
            frame.dispose();
            chess.gamePersonnalise(scanner);
        });

        JButton classicGUI = new JButton("3. Mode classique GUI (Pièces de base)");
        classicGUI.addActionListener(e -> {
            frame.dispose();
            chess.gameGUI();
        });

        JButton customGUI = new JButton("4. Mode personnalisé GUI (Ajoutez vos pièces)");
        customGUI.addActionListener(e -> {
            frame.dispose();
            // À implémenter dans ta classe Chess :
            chess.gamePersonnaliseGUI();
        });

        buttonPanel.add(classicTerminal);
        buttonPanel.add(customTerminal);
        buttonPanel.add(classicGUI);
        buttonPanel.add(customGUI);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    
}
