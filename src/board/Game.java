package board;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
	private static Scanner input = new Scanner(System.in);
	private static GameBoard gameBoardOne;
	private static int isUnique = 1;
	private static int startStop = 1;
	private static int nbPawn = 4;
	private static int[] tableResult = new int[2];

	public Game() {
		gameBoardOne = new GameBoard(GameBoard.generation_combinaison(4, true), 4);
		start();
	}

	public static void start() {
		
		startStop = 2;
		System.out.println("Une nouvelle partie s'est crée...");
		do {
			try {
				System.out.println("pressez (1) pour jouer, (0) pour quitter");
				startStop = input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Vous avez fait une erreur en choisissant,\n"+"Ressaisissez : ");
				input.nextLine(); // clears the buffer
			}
		} while (startStop != 1 && startStop != 0);
		
		int jouerContreBot = 2;
		do {
			try {
				System.out.println("pressez (1) pour défier le robot, (0) sinon");
				jouerContreBot = input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Vous avez fait une erreur en choisissant,\n"+"Ressaisissez : ");
				input.nextLine(); // clears the buffer
			}
		} while (jouerContreBot != 1 && jouerContreBot != 0);

		while(startStop == 1) {
			if(jouerContreBot == 1) {
				startStop = 0;
				botPlay();
			} else {
				gameBoardInit();
				chooseGameModeType();
			}
		}
	}
	
	private static int gameBoardInit () {
		int nbPawn = 0;
		do {
			try {
				System.out.println(
						"Veuillez sélectionner le nombre de pions avec lequel vous souhaitez jouer\n"+
								"Il doit être compris entre 4 et 8 (compris)"
						);
				nbPawn = input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Vous avez fait une erreur en choisissant,\n"+"Ressaisissez : ");
				input.nextLine(); // clears the buffer
			}
		} while (nbPawn < 3 || nbPawn > 9);
		return nbPawn;
	}

	private static void chooseGameModeType() {
		isUnique = 2;
		do {
			try {
				System.out.println(
						"Veuillez maintenant sélectionner si les pions de la combinaisons cachée doivent être unique\n"+
						"(1) sinon (0)");
				isUnique = input.nextInt();
				switch(isUnique) {
				case 0 :
					gameBoardOne.setHiddenCombination(GameBoard.generation_combinaison(gameBoardOne.getNbSlot(), false), false); // bug
					System.out.println("les pions sont générés aléatoirement...");
					break;
				case 1 :
					gameBoardOne.setHiddenCombination(GameBoard.generation_combinaison(gameBoardOne.getNbSlot(), true),true);
					System.out.println("les pions sont générés aléatoirement sans doublons...");
				default :
					System.out.println("Erreur de saisie, ressaisissez :");
					input.nextLine(); // clears the buffer

				}
			} catch (InputMismatchException e) {
				System.out.println("((1) sinon (0))");
				input.nextLine(); // clears the buffer
			}
		} while (isUnique != 0 && isUnique != 1);
		play();
	}

	private static void play() {
		System.out.println(gameBoardOne.getHiddenCombinationString());
		while (gameBoardOne.isResolved() == false && gameBoardOne.getNbShot() < 12) {
			gameBoardOne.setNbShot(gameBoardOne.getNbShot() + 1);
			System.out.println("il vous reste : " + (13 - gameBoardOne.getNbShot()) + " éssais...");
			System.out.println("Entrez votre selection de pions :");
			gameBoardOne.setCurrentCombination(GameBoard.creation_table_utilisateur(nbPawn));
			tableResult = GameBoard.comparaison_combinaison(gameBoardOne.getHiddenCombination(), gameBoardOne.getCurrentCombination());
			if (tableResult[0] == gameBoardOne.getHiddenCombination().size()) {
				gameBoardOne.setResolved(true);
			} else {
				System.out.println(
						"Vous avez " + 
								tableResult[0] + " pions bien positionnés et de bonne couleur " + // bug ?
								tableResult[1] + " pions de bonne couleur mais mal placés."
						);
			}
		}
		isGameFinished();
	}

	private static void isGameFinished() {
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
		gameBoardOne.setNbShot(0);
		gameBoardOne.setNbTotalParties(gameBoardOne.getNbTotalParties() + 1);
		gameBoardOne.setNbDefaite(gameBoardOne.getNbTotalParties() - gameBoardOne.getNbVictoire());
		replay();
	}

	private static void replay() {
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

	private static void botPlay() {
		// Le bot essaye de deviner

		int nbParties = 0;
		int nbManches = 0;
		int nbMancheVictoireMin;
		int[] tableResult = new int[2];
		ArrayList<Integer> arrUser = new ArrayList<Integer>();

		// Création partie

		Scanner input = new Scanner(System.in);
		System.out.println("Vous jouez contre l'ordinateur...");
		System.out.println("Créez votre arrangement d'entier compris entre 1 et 8 (compris)...");
		for (int i = 0; i < 4; i++) {
			arrUser.add(input.nextInt());
		}
		GameBoard gameBoardOne = new GameBoard(arrUser, 4);    	
		System.out.println("Veuillez sélectionner le nombre d'itération (partie) que le bot doit jouer");
		nbParties = input.nextInt();
		System.out.println("Veuillez maintenant sélectionner le nombre de manches par partie");
		nbManches = input.nextInt();
		nbMancheVictoireMin = nbManches;

		// Brut Force aléatoire

		for (int i=0; i<nbParties; i++) {

			while (gameBoardOne.isResolved() == false && gameBoardOne.getNbShot() < nbManches) {
				gameBoardOne.setNbShot(gameBoardOne.getNbShot() + 1);
				gameBoardOne.setCurrentCombination(GameBoard.generation_combinaison(gameBoardOne.getNbSlot(), false));
				tableResult = GameBoard.comparaison_combinaison(gameBoardOne.getHiddenCombination(), gameBoardOne.getCurrentCombination());
				if (tableResult[0] == gameBoardOne.getHiddenCombination().size()) {
					if (nbMancheVictoireMin >= gameBoardOne.getNbShot()) {
						nbMancheVictoireMin = gameBoardOne.getNbShot();
					}
					gameBoardOne.setResolved(true);
					gameBoardOne.setNbVictoire(gameBoardOne.getNbVictoire() + 1);
					gameBoardOne.setNbCoupsMoyen((gameBoardOne.getNbShot() + gameBoardOne.getNbCoupsMoyen() * (gameBoardOne.getNbVictoire() - 1)) / gameBoardOne.getNbVictoire());
				}
			}
			gameBoardOne.setResolved(false);
			gameBoardOne.setNbShot(0);
			gameBoardOne.setNbTotalParties(gameBoardOne.getNbTotalParties() + 1);
			gameBoardOne.setNbDefaite(gameBoardOne.getNbTotalParties() - gameBoardOne.getNbVictoire());
		}
		System.out.println(
				"Résultat Brut Force Aléatoire :\n"+gameBoardOne.getNbVictoire()+" parties gagnées sur "+gameBoardOne.getNbTotalParties()+" parties jouées.\nEt en moyenne " +gameBoardOne.getNbCoupsMoyen() +" coups pour résoudre une partie.");
		if (gameBoardOne.getNbVictoire() > 0) {
			System.out.println("La partie ayant pris le moins de coup pour être résolue a été résolue en "+nbMancheVictoireMin +" coups.");
		}
		gameBoardOne.setNbTotalParties(0);
		gameBoardOne.setNbVictoire(0);
		gameBoardOne.setNbDefaite(0);
		gameBoardOne.setNbCoupsMoyen(0);
		nbMancheVictoireMin = nbManches;

		// Brut Force pas à pas

		for (int i = 0; i<nbParties; i++) {
			while (gameBoardOne.isResolved() == false && gameBoardOne.getNbShot() < nbManches) {
				gameBoardOne.setNbShot(gameBoardOne.getNbShot() + 1);

				// pas à pas
				ArrayList<Integer> arrPas = new ArrayList<Integer>();
				for (int m = 1; m < 9; m ++) {
					for (int j = 1; j < 9; j++) {
						for (int k = 1; k < 9; k ++) {
							for (int l = 1; l < 9; l++) {
								arrPas.add(m);
								arrPas.add(j);
								arrPas.add(k);
								arrPas.add(l);
								gameBoardOne.setCurrentCombination(arrPas);
								tableResult = GameBoard.comparaison_combinaison(gameBoardOne.getHiddenCombination(), gameBoardOne.getCurrentCombination());
								if (tableResult[0] == gameBoardOne.getHiddenCombination().size()) {
									if (nbMancheVictoireMin > gameBoardOne.getNbShot()) {
										nbMancheVictoireMin = gameBoardOne.getNbShot();
									}
									gameBoardOne.setResolved(true);
									gameBoardOne.setNbVictoire(gameBoardOne.getNbVictoire() + 1);
									gameBoardOne.setNbCoupsMoyen((gameBoardOne.getNbShot() + gameBoardOne.getNbCoupsMoyen() * (gameBoardOne.getNbVictoire() - 1)) / gameBoardOne.getNbVictoire());
									m = 10;
									j = 10;
									k = 10;
									l = 10;
								}
								arrPas.clear();
							}
						}
					}
				}
			}
			gameBoardOne.setResolved(false);
			gameBoardOne.setNbShot(0);
			gameBoardOne.setNbTotalParties(gameBoardOne.getNbTotalParties() + 1);
			gameBoardOne.setNbDefaite(gameBoardOne.getNbTotalParties() - gameBoardOne.getNbVictoire());
		}
		System.out.println(
				"Résultat Brut Force Pas à Pas :\n" + gameBoardOne.getNbVictoire() + " parties gagnées sur " + 
						gameBoardOne.getNbTotalParties() + " parties jouées.\nEt en moyenne " + gameBoardOne.getNbCoupsMoyen() + 
				" coups pour résoudre une partie.");
		if (gameBoardOne.getNbVictoire() > 0) {
			System.out.println("La partie ayant pris le moins de coup pour être résolue a été résolue en "+nbMancheVictoireMin+" coups.");
		}
		gameBoardOne.setNbTotalParties(0);
		gameBoardOne.setNbVictoire(0);
		gameBoardOne.setNbDefaite(0);
		gameBoardOne.setNbCoupsMoyen(0);
		nbMancheVictoireMin = nbManches;

		// Heuristique Logique

		for (int i = 0; i<nbParties; i++) {
			while (gameBoardOne.isResolved() == false && gameBoardOne.getNbShot() < nbManches) {
				gameBoardOne.setNbShot(gameBoardOne.getNbShot() + 1);
				ArrayList<Integer> arrPas = new ArrayList<Integer>();
				for (int m = 1; m < 9; m ++) {
					gameBoardOne.setCurrentCombination(arrPas);
					tableResult = GameBoard.comparaison_combinaison(gameBoardOne.getHiddenCombination(), gameBoardOne.getCurrentCombination());
					if (tableResult[0] == gameBoardOne.getHiddenCombination().size()) {
						if (nbMancheVictoireMin > gameBoardOne.getNbShot()) {
							nbMancheVictoireMin = gameBoardOne.getNbShot();
						}
						gameBoardOne.setResolved(true);
						gameBoardOne.setNbVictoire(gameBoardOne.getNbVictoire() + 1);
						gameBoardOne.setNbCoupsMoyen((gameBoardOne.getNbShot() + gameBoardOne.getNbCoupsMoyen() * (gameBoardOne.getNbVictoire() - 1)) / gameBoardOne.getNbVictoire());
						gameBoardOne.setResolved(false);
					}
				}
			}
			gameBoardOne.setResolved(false);
			gameBoardOne.setNbShot(0);
			gameBoardOne.setNbTotalParties(gameBoardOne.getNbTotalParties() + 1);
			gameBoardOne.setNbDefaite(gameBoardOne.getNbTotalParties() - gameBoardOne.getNbVictoire());
		}
		System.out.println(
				"Résultat logique :\n" + gameBoardOne.getNbVictoire() + " parties gagnées sur " + 
						gameBoardOne.getNbTotalParties() + " parties jouées.\nEt en moyenne " + gameBoardOne.getNbCoupsMoyen() + 
				" coups pour résoudre une partie.");
		if (gameBoardOne.getNbVictoire() > 0) {
			System.out.println(
					"La partie ayant pris le moins de coup pour être résolue a été résolue en "+
							nbMancheVictoireMin + " coups.");
		}
		gameBoardOne.setNbTotalParties(0);
		gameBoardOne.setNbVictoire(0);
		gameBoardOne.setNbDefaite(0);
		gameBoardOne.setNbCoupsMoyen(0);
		nbMancheVictoireMin = nbManches;

		input.close();
	}
}
