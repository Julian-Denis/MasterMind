package pawn;

public class Pawn {
    // Propriétés
    Color couleur;
    int position;

    // Constructeur
    public Pawn(Color col, int pos) {
        this.couleur = col;
        this.position = pos;
    }

    // getter
    public Color getCouleur() {
        return this.couleur;
    }
    public int getPosition() {
        return this.position;
    }

    // setter
    public void setCouleur(Color col) {
        this.couleur = col;
    }
    public void setPosition (int pos) {
        this.position = pos;
    }

    // méthodes
}