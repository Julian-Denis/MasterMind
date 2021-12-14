package board;

import java.util.ArrayList;
import java.util.Scanner;

import pawn.Pawn;

public class GameBoard {
    // Propriétés
    protected static int NB_ROUND = 12;

    // nombre de pions puis de tentatives
    int nbSlot;
    int nbShot = 0;

    // combinaison cachée et combinaison courante user
    ArrayList<Integer> hiddenCombination;
    ArrayList<Integer> currentCombination;

    // combinaison essayées
    int[] tentatives;

    // est ce que le joueur à réussi
    boolean isResolved = false;

    // statistique de partie
    // nb victoire / defaite / nb total de parties / nb coup moyen pour la victoire
    int nbVictoire;
    int nbDefaite;
    int nbTotalParties;
    double nbCoupsMoyen = 0;

    // Constructeur
    public GameBoard (ArrayList<Integer> combi, int nbSlot) {
        this.hiddenCombination = combi;
        this.nbSlot = nbSlot;
    }

    // getter
    public static int getNbRound() {
        return NB_ROUND;
    }
    public int getNbSolt() {
        return this.nbSlot;
    }
    public int getNbShot() {
        return this.nbShot;
    }
    public ArrayList<Integer> getHiddenCombination() {
        return this.hiddenCombination;
    }
    public String getHiddenCombinationString() {
        return affCombiInt(this.hiddenCombination);
    }
    public int[] getTentatives() {
        return this.tentatives;
    }
    public boolean getIsResolved () {
        return this.isResolved;
    }

    // setter
    public void setNbSolt(int nb) {
        this.nbSlot = nb;
    }
    public void getNbShot(int nb) {
        this.nbShot = nb;
    }
    public void setHiddenCombination(ArrayList<Integer> combi, boolean isAleatoire) {
        if (isAleatoire) {
            this.hiddenCombination = generation_combinaison(nbSlot, true) ;
        } else {
            this.hiddenCombination = creation_table_utilisateur(nbSlot);
        }
    }
    public void setCurrentCombination (ArrayList<Integer> combi) {
        this.currentCombination = combi;
    }
    public void setIsResolved (boolean isIt) {
        this.isResolved = isIt;
    }
    
    // méthodes
    public String affCombiInt (ArrayList<Integer> tab) {
        String s = "[";
        for (int i = 0; i < tab.size() - 1; i ++) {
            s += String.valueOf(tab.get(i) + ", ");
        }
        s += String.valueOf(tab.get(tab.size()-1) + "]"); 
        return s;
    }

    public static String affCombiPawn (ArrayList<Pawn> tab) {
        String s = "[";
        for (int i = 0; i < tab.size() - 1; i ++) {
            s += String.valueOf(tab.get(i).couleur) + ", ";
        }
        s += String.valueOf(tab.get(tab.size()-1).couleur) + "]"; 
        return s;
    }

    public static ArrayList<Integer> generation_combinaison (int nb_pions, boolean combinaison_sans_double) {	
        ArrayList<Integer> arrCombinaisonAleatoire = new ArrayList<Integer>();
        if (nb_pions>3 && nb_pions<9) {
            for (int i = 0; i < nb_pions; i++) {
                arrCombinaisonAleatoire.add(i, (int)Math.floor((Math.random()*(8-1+1))+1));
                if (combinaison_sans_double) {
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
            arrCombinaisonUtilisateur.add(input.nextInt());
        }
        //input.close();
        return arrCombinaisonUtilisateur;
    }

    public static int[] comparaison_combinaison (ArrayList<Integer> combi_un, ArrayList<Integer> combi_deux) {
        int presenceCounter = 0;
        int positionCounter = 0;
        int[] arrInfo = new int[2];
        if (combi_un.size() == combi_deux.size()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j ++) {
                    if (combi_un.get(i) == combi_deux.get(j)) {
                        presenceCounter += 1;
                        break; // ?
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                if (combi_un.get(i) == combi_deux.get(i)) {
                    positionCounter += 1;
                }
            }
            arrInfo[0] = positionCounter;
            arrInfo[1] = presenceCounter - positionCounter;
        }
    return arrInfo;
    }
}