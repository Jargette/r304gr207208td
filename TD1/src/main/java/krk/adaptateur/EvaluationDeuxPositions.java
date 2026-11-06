package krk.adaptateur;

import krk.application.EvaluateurPosition;
import krk.domaine.Position;

/** Deux résultats connus, sans calcul des règles d'échecs. */
public class EvaluationDeuxPositions implements EvaluateurPosition {
    @Override
    public boolean estMat(Position position) {
        if (position.equals(new Position("b6", "h8", "a8"))) {
            return true;
        }
        if (position.equals(new Position("b6", "h6", "a8"))) {
            return false;
        }
        throw new IllegalArgumentException("Position non prise en charge dans cet exemple");
    }
}
