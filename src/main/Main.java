package main;

import pawn.Pawn;
import board.GameBoard;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {

        int startStop = 1;
        int nbPawn = 4;
        int isUnique = 1;
        int[] tableResult = new int[2];

        // Création partie

        Scanner input = new Scanner(System.in);
        System.out.println("Une nouvelle partie s'est crée...");
        System.out.println("pressez 1 pour jouer, 0 pour quitter");
        startStop = input.nextInt();

        // jeu
        
        while (startStop == 1) {

            // Initialisation game board

            System.out.println(
                "Veuillez sélectionner le nombre de pions avec lequel vous souhaitez jouer\n"+
                "Il doit être compris entre 4 et 8 (compris)"
            );
            nbPawn = input.nextInt();

            if (nbPawn < 3 || nbPawn > 9) {
                nbPawn = 4;
                System.out.println(
                    "Vous avez fait une erreur en choisissant,\n"+
                    "le nombre de pions à été mis à 4 par défaut"
                );
            }

            GameBoard gameBoardOne = new GameBoard(GameBoard.generation_combinaison(nbPawn, true), nbPawn);
            System.out.println(
                "Veuillez maintenant sélectionner si les pions de la combinaisons cachée doivent être unique\n"+
                "(1) sinon (0)");
            isUnique = input.nextInt();

            if (isUnique == 0) {
                gameBoardOne = new GameBoard(GameBoard.generation_combinaison(nbPawn, false), nbPawn);
            } else if (isUnique == 1) {
            } else {
                System.out.println("Erreur de saisie, les pions seront uniques par défault");
            }
            System.out.println(gameBoardOne.getHiddenCombinationString());
            while (gameBoardOne.isResolved() == false && gameBoardOne.getNbShot() < 12) {
                gameBoardOne.setNbShot(gameBoardOne.getNbShot() + 1);
                System.out.println("il vous reste : " + (13 - gameBoardOne.getNbShot()) + " éssais...");
                System.out.println("Entrez votre selection de pions :");
                gameBoardOne.setCurrentCombination(gameBoardOne.creation_table_utilisateur(nbPawn));
                tableResult = gameBoardOne.comparaison_combinaison(gameBoardOne.getHiddenCombination(), gameBoardOne.getCurrentCombination());
                if (tableResult[0] == 4) {
                    gameBoardOne.setResolved(true);
                } else {
                    System.out.println(
                        "Vous avez " + 
                        tableResult[0] + " pions bien positionnés et de bonne couleur " + 
                        tableResult[1] + " pions de bonne couleur."
                    );
                }
            }

            if (gameBoardOne.isResolved() == false) {
                System.out.println(
                    "vous venez de perdre la partie, la combinaison cachée était :" +
                    gameBoardOne.affCombiInt(gameBoardOne.getHiddenCombination())
                );
            } else {
                System.out.println(
                    "Vous venez de trouver la combinaison cachée : " +
                    gameBoardOne.affCombiInt(gameBoardOne.getHiddenCombination())
                );
                gameBoardOne.setNbVictoire(gameBoardOne.getNbVictoire() + 1);
                gameBoardOne.setNbCoupsMoyen((gameBoardOne.getNbShot() + gameBoardOne.getNbCoupsMoyen() * (gameBoardOne.getNbVictoire() - 1)) / gameBoardOne.getNbVictoire());
                gameBoardOne.setResolved(false);
            }
            gameBoardOne.setNbShot(0); //set
            gameBoardOne.setNbTotalParties(gameBoardOne.getNbTotalParties()+1);
            gameBoardOne.setNbDefaite(gameBoardOne.getNbTotalParties()-gameBoardOne.getNbVictoire());

            System.out.println("pressez 1 pour rejouer, 0 pour quitter");
            startStop= input.nextInt();

            if (startStop == 0) {
                System.out.println(
                    "Merci d'avoir joué, votre résultat est le suivant :\n" +
                    gameBoardOne.getNbVictoire() + " parties gagnées " +
                    " sur " + gameBoardOne.getNbTotalParties() +
                    " parties jouées.\n" + 
                    "Et en moyenne " + gameBoardOne.getNbCoupsMoyen() +
                    " coups pour résoudre une partie"
                );
            }
        }
        input.close();
    }
}