package krk.application;

import krk.domaine.Position;

public class ChoisirPosition {
    private final EvaluateurPosition evaluateur;

    public ChoisirPosition(EvaluateurPosition evaluateur) {
        this.evaluateur = evaluateur;
    }

    /** Privilégie un mat ; conserve la première position en cas d'égalité. */
    public Position choisir(Position premiere, Position seconde) {
        if (evaluateur.estMat(premiere)) {
            return premiere;
        }
        if (evaluateur.estMat(seconde)) {
            return seconde;
        }
        return premiere;
    }
}
