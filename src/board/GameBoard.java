package board;

import java.util.ArrayList;
import java.util.Scanner;

public class GameBoard {

	// Propriétés

	protected static int NB_ROUND = 12; // nb manches dans une partie
	private int nbSlot; // nb pions dans un arrangement
	private int nbShot = 0; // nb éssais pour deviner l'arrangement caché
	private int nbVictoire = 0; // nb victoires
	private int nbDefaite = 0; // nb defaites
	private int nbTotalParties = 0; // nb total de parties
	private double nbCoupsMoyen = 0; // nb coup moyen pour gagner
	private ArrayList<Integer> hiddenCombination; // combinaison cachée
	private ArrayList<Integer> currentCombination; // combinaison courante utilisateur
	private int[] tentatives; // combinaisons essayées
	private boolean isResolved = false; // est ce que la combinaison est trouvée

	// Constructeur

	public GameBoard (ArrayList<Integer> combi, int nbSlot) {
		this.hiddenCombination = combi;
		this.nbSlot = nbSlot;
	}

	// Getters/Setter

	public static int getNB_ROUND() {
		return NB_ROUND;
	}

	public static void setNB_ROUND(int nB_ROUND) {
		NB_ROUND = nB_ROUND;
	}

	public int getNbSlot() {
		return nbSlot;
	}

	public void setNbSlot(int nbSlot) {
		this.nbSlot = nbSlot;
	}

	public int getNbShot() {
		return nbShot;
	}

	public void setNbShot(int nbShot) {
		this.nbShot = nbShot;
	}

	public int getNbVictoire() {
		return nbVictoire;
	}

	public void setNbVictoire(int nbVictoire) {
		this.nbVictoire = nbVictoire;
	}

	public int getNbDefaite() {
		return nbDefaite;
	}

	public void setNbDefaite(int nbDefaite) {
		this.nbDefaite = nbDefaite;
	}

	public int getNbTotalParties() {
		return nbTotalParties;
	}

	public void setNbTotalParties(int nbTotalParties) {
		this.nbTotalParties = nbTotalParties;
	}

	public double getNbCoupsMoyen() {
		return nbCoupsMoyen;
	}

	public void setNbCoupsMoyen(double nbCoupsMoyen) {
		this.nbCoupsMoyen = nbCoupsMoyen;
	}

	public ArrayList<Integer> getHiddenCombination() {
		return hiddenCombination;
	}

	public String getHiddenCombinationString() {
		return affCombiInt(this.hiddenCombination);
	}

	public void setHiddenCombination(ArrayList<Integer> combi, boolean isAleatoire) {
		if (isAleatoire) {
			this.hiddenCombination = generation_combinaison(nbSlot, true) ;
		} else {
			this.hiddenCombination = creation_table_utilisateur(nbSlot);
		}
	}

	public ArrayList<Integer> getCurrentCombination() {
		return currentCombination;
	}

	public void setCurrentCombination(ArrayList<Integer> currentCombination) {
		this.currentCombination = currentCombination;
	}

	public int[] getTentatives() {
		return tentatives;
	}

	public void setTentatives(int[] tentatives) {
		this.tentatives = tentatives;
	}

	public boolean isResolved() {
		return isResolved;
	}

	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	// Méthodes

	public String affCombiInt (ArrayList<Integer> tab) {
		String s = "[";
		for (int i = 0; i < tab.size() - 1; i ++) {
			s += String.valueOf(tab.get(i) + ", ");
		}
		s += String.valueOf(tab.get(tab.size()-1) + "]"); 
		return s;
	}

	/*public static String affCombiPawn (ArrayList<Pawn> tab) {
        String s = "[";
        for (int i = 0; i < tab.size() - 1; i ++) {
            s += String.valueOf(tab.get(i).couleur) + ", ";
        }
        s += String.valueOf(tab.get(tab.size()-1).couleur) + "]"; 
        return s;
    }*/

	public static ArrayList<Integer> generation_combinaison (int nb_pions, boolean combinaison_sans_double) {	
		ArrayList<Integer> arrCombinaisonAleatoire = new ArrayList<Integer>();
		if (nb_pions>3 && nb_pions<9) {
			for (int i = 0; i < nb_pions; i++) {
				arrCombinaisonAleatoire.add(i, (int)Math.floor((Math.random()*(8-1+1))+1));
				if (combinaison_sans_double == true) {
					for (int j = 0; j < i; j++) {
						if (arrCombinaisonAleatoire.get(j) == arrCombinaisonAleatoire.get(i)) {
							arrCombinaisonAleatoire.remove(i);
							i-=1;
						}
					}
				}  
			}
		} else {
			System.out.println("erreur de saisie");
		}
		return arrCombinaisonAleatoire;
	}

	public static ArrayList<Integer> creation_table_utilisateur(int nb_pions) {
		ArrayList<Integer> arrCombinaisonUtilisateur = new ArrayList<Integer>();
		Scanner input = new Scanner(System.in) ;
		for (int i = 0 ; i < nb_pions; i ++) {
			
			System.out.println(arrCombinaisonUtilisateur.toString());
			arrCombinaisonUtilisateur.add(transformColor(input.nextLine()));
			
		}
		return arrCombinaisonUtilisateur;
	}

	public static int[] comparaison_combinaison (ArrayList<Integer> combi_un, ArrayList<Integer> combi_deux) {
		int presenceCounter = 0;
		int positionCounter = 0;
		int[] arrInfo = new int[2];
		if (combi_un.size() == combi_deux.size()) {
			for (int i = 0; i < combi_un.size(); i++) {
				for (int j = 0; j < combi_un.size(); j ++) {
					if (combi_un.get(i) == combi_deux.get(j)) {
						presenceCounter += 1;
						break;
					}
				}
			}
			for (int i = 0; i < combi_un.size(); i++) {
				if (combi_un.get(i) == combi_deux.get(i)) {
					positionCounter += 1;
				}
			}
			arrInfo[0] = positionCounter;
			arrInfo[1] = presenceCounter - positionCounter;
		}
		return arrInfo;
	}

	private static int transformColor(String color) {
		boolean erreur = true;
		Scanner input = new Scanner(System.in) ;
		while (erreur) {
			switch(color) {
			case "rouge" :
				erreur = false;
				return 1;
			case "bleue" :
				erreur = false;
				return 2;
			case "vert" :
				erreur = false;
				return 3;
			case "jaune" :
				erreur = false;
				return 4;
			case "rose" :
				erreur = false;
				return 5;
			case "violet" :
				erreur = false;
				return 6;
			case "noir" :
				erreur = false;
				return 7;
			case "blanc" :
				erreur = false;
				return 8;
			default :
				erreur = true;
				System.out.println("Erreur de saisie, retaper votre couleur :");
				color = input.nextLine();
			}
		}
		return 1;
	}
}