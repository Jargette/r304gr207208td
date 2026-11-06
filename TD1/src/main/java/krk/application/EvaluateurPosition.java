package krk.application;

import krk.domaine.Position;

public interface EvaluateurPosition {
    /** Indique si les noirs sont mats ; refuse une position non prise en charge. */
    boolean estMat(Position position);
}
