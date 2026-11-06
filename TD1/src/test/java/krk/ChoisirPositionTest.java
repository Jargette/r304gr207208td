package krk;

import krk.adaptateur.EvaluationDeuxPositions;
import krk.application.ChoisirPosition;
import krk.domaine.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChoisirPositionTest {
    @Test
    void choisitLaPositionDeMatQuandElleEstSeconde() {
        Position premiere = new Position("b6", "h6", "a8");
        Position seconde = new Position("b6", "h8", "a8");
        ChoisirPosition service = new ChoisirPosition(new EvaluationDeuxPositions());

        Position resultat = service.choisir(premiere, seconde);

        assertEquals(seconde, resultat);
    }

    @Test
    void choisitLaPositionDeMatQuandElleEstPremiere() {
        Position premiere = new Position("b6", "h8", "a8");
        Position seconde = new Position("b6", "h6", "a8");
        ChoisirPosition service = new ChoisirPosition(new EvaluationDeuxPositions());

        Position resultat = service.choisir(premiere, seconde);

        assertEquals(premiere, resultat);
    }
}
