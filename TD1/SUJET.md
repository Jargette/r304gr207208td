# TD1 — Tester le choix d’un coup avec une simulation

R304 FI — 90 minutes — groupes de 3 à 5. Cette séance précède le premier cours. Aucune préparation préalable ni connaissance de Clean Architecture n’est supposée. Un portable par groupe suffit. Pas d’IA pendant le TD.

## Objectif et travail attendu

Vous disposez d’un moteur pour la finale roi et tour blancs contre roi noir (KRK). Vous allez lire une petite partie de son organisation, créer une simulation d’un de ses composants et écrire deux tests JUnit. Les règles et l’algorithme de choix du coup sont fournis : vous ne les réécrivez pas. Votre simulation sert à tester un cas précis ; elle ne remplace pas le véritable composant dans le programme utilisé pour jouer.

À la fin : deux schémas simples, une classe OracleTerminal, deux tests dans TD1Test et un court bilan du groupe. Conservez-les dans votre dossier de travail, avec un commit local. Aucun rapport séparé ni push sur GitHub n’est demandé.

## 0–15 minutes — Récupérer et démarrer

Le dépôt commun est [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). Si vous ne l’avez pas, clonez-le dans votre dossier personnel :

```text
git clone https://github.com/mikalziane/R304-2026-2027.git
cd R304-2026-2027/TD1
```

Si vous avez déjà un clone pour le TP1, commencez par `git status` à sa racine. S’il est propre, récupérez la mise à jour avec `git pull --ff-only`, puis entrez dans `TD1`. Si vous avez des modifications, conservez-les et demandez à l’enseignant la copie ZIP du TD1 : ne réinitialisez pas et n’écrasez pas votre travail.

En secours, extrayez entièrement `base-etudiante.zip`, puis ouvrez le dossier `TD1` obtenu. Il contient `pom.xml`, `mvnw.cmd`, `mvnw`, `.mvn`, `src` et `SUJET.md`. N’ouvrez pas l’archive sans l’extraire. Tous les chemins ci-dessous sont relatifs à ce dossier TD1.

**JDK 21 minimum requis.** Dans IntelliJ, ouvrez `pom.xml` dans ce dossier TD1 comme projet Maven ; choisissez un JDK 21 ou plus récent pour le SDK du projet et pour Maven. Les autres IDE peuvent importer le même pom.xml. Maven télécharge les dépendances lors de la première utilisation ; le lanceur est fourni, donc vous n’avez pas à installer Maven séparément.

Dans un terminal situé dans TD1, sous Windows PowerShell :

```powershell
java -version
.\mvnw.cmd -version
.\mvnw.cmd test
```

Sous macOS/Linux, utilisez `sh ./mvnw -version` puis `sh ./mvnw test`. Vérifiez que Java et Maven annoncent un JDK 21 ou plus récent. Si les versions diffèrent, faites vérifier `JAVA_HOME` et les réglages de l’IDE. Résultat de départ attendu : **33 tests, zéro échec**. Vous pouvez aussi lancer les tests depuis l’IDE.

Si le démarrage bloque plus de dix minutes, signalez-le et passez à la lecture avec les fichiers locaux pendant la résolution du problème. Un ZIP ne fournit pas les dépendances Maven hors ligne.

**Git.** Dans un clone, ne faites pas `git init`. Avec un ZIP, créez une seule fois l’historique dans le dossier TD1 : `git init`, puis `git add .` et `git commit -m "Base de départ TD1"`. Si Git demande une identité, utilisez vos propres valeurs puis relancez le commit :

```text
git config user.name "Votre prénom et nom"
git config user.email "Votre adresse personnelle"
```

Désignez un pilote du clavier, un lecteur du code et un responsable des tests ; répartissez aussi les schémas si vous êtes quatre ou cinq. Changez de pilote vers le milieu de la séance.

## Repères d’échecs utiles

KRK signifie roi blanc et tour blanche contre roi noir. Les colonnes de l’échiquier vont de a à h, les lignes de 1 à 8 ; b6 est la colonne b, ligne 6. Le « camp au trait » est celui qui doit jouer. Un roi en échec et sans aucun coup légal est mat : la partie est perdue pour ce camp.

Dans notre exemple : roi blanc b6, tour blanche h7, roi noir a8, trait aux blancs. Le coup de tour h7 vers h8 donne mat. Ce fait est fourni, vous n’avez pas à démontrer les règles d’échecs.

Une Evaluation indique WIN (gain), DRAW (nulle) ou LOSS (perte), du point de vue du camp au trait dans la position évaluée. Après un coup blanc, on évalue une position où les noirs doivent jouer : leur LOSS est favorable aux blancs. `loss(0)` signifie que ce camp est déjà mat.

## 15–35 minutes — Lire une frontière du programme

Le service ChooseBestMoveService choisit un coup. Pour cela, il demande à un objet « oracle » d’évaluer les positions. Oracle signifie ici composant chargé de fournir cette évaluation ; le véritable oracle utilise une table de finales. Nous étudions uniquement la frontière entre ce service et l’oracle, pas tout le dialogue du moteur avec une interface de jeu.

Sous `src/main/java/org/chesskrk`, ouvrez :

- `application/port/out/EndgameOracle.java` : interface avec `Evaluation evaluate(Position position)` ;
- `application/usecase/ChooseBestMoveService.java` : constructeur recevant un EndgameOracle et méthode chooseMove ;
- `adapter/tablebase/RetrogradeTablebase.java` : lisez seulement la déclaration `implements EndgameOracle` et la signature de evaluate ; le calcul de la table est fourni ;
- `infrastructure/Main.java` : lisez seulement buildUseCase, qui construit l’oracle et le service.

Une interface décrit des opérations attendues ; une classe qui l’implémente fournit leur réalisation. Recevoir un objet au constructeur permet d’utiliser ses opérations sans nommer sa classe concrète. L’assemblage est le code qui choisit et relie les objets.

La méthode chooseMove utilise des streams, une lambda et un Comparator. Vous n’avez pas à maîtriser ces syntaxes aujourd’hui. Elle effectue trois étapes : obtenir les coups légaux ; évaluer la position produite par chaque coup ; retenir le meilleur selon le score fourni. `Optional<Move>` représente soit un coup présent, soit l’absence de coup légal.

**Deux schémas, deux sens de flèches.** Exemple neutre : un service de livraison reçoit un objet de type interface Tarif ; TarifFixe implémente Tarif.

```text
Dépendances de types : ServiceLivraison --utilise--> Tarif
                      TarifFixe --implémente--> Tarif
Appel à l’exécution : objet service --appelle calculer--> objet tarifFixe
```

Sur votre feuille, transposez ce modèle à ChooseBestMoveService, EndgameOracle et RetrogradeTablebase. Dans le schéma de types, ajoutez Main et les types qu’il nomme pour construire les objets. Étiquetez les flèches « utilise », « implémente » ou « construit ». Dans le schéma des appels, représentez seulement un objet service appelant evaluate sur un objet oracle. Le service nomme-t-il RetrogradeTablebase ? Quel fichier choisit cette classe concrète ?

## 35–60 minutes — Créer une simulation limitée

Créez `src/main/java/org/chesskrk/adapter/pedagogie/OracleTerminal.java`, avec le paquetage `org.chesskrk.adapter.pedagogie`. Cette classe publique implémente EndgameOracle. Sa méthode publique evaluate reçoit une Position et retourne une Evaluation :

- si `Rules.status(position) == GameStatus.CHECKMATE`, retourner `Evaluation.loss(0)` ;
- dans tous les autres cas, retourner `Evaluation.DRAW`.

Imports utiles : `org.chesskrk.application.port.out.EndgameOracle`, `org.chesskrk.application.port.out.Evaluation` et, dans `org.chesskrk.domain`, Position, Rules et GameStatus. Les dossiers reflètent les noms de paquetages. Votre IDE peut créer les dossiers et proposer les imports.

Cette simulation ne connaît que le mat immédiat. DRAW veut ici dire « traité comme nul par la simulation », pas « vraiment nul ». Elle possède la bonne signature Java mais ne respecte pas toute la promesse de connaissance parfaite du véritable oracle. Nous la branchons uniquement dans des tests, en laissant Main, le service et les règles inchangés. Les contrôles automatiques d’architecture fournis connaissent déjà le paquetage pedagogie.

## 60–80 minutes — Écrire deux tests et conserver le travail

Créez `src/test/java/org/chesskrk/application/TD1Test.java`. Voici le début à compléter :

```java
package org.chesskrk.application;
import org.chesskrk.adapter.pedagogie.OracleTerminal;
import org.chesskrk.application.port.out.Evaluation;
import org.chesskrk.application.usecase.ChooseBestMoveService;
import org.chesskrk.domain.*;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class TD1Test {
    @Test void choisitLeMatImmediat() {
        Position p = new Position(Square.parse("b6"),
            Square.parse("h7"), Square.parse("a8"), Color.WHITE);
        var service = new ChooseBestMoveService(new OracleTerminal());
        // Appeler service.chooseMove(p).
        // Comparer le résultat au coup h7 vers h8 dans un Optional.
    }
    @Test void simuleUneNulleHorsMat() {
        // Reprendre la même position p, qui n’est pas terminale.
        // Vérifier GameStatus.ONGOING avec Rules.status(p).
        // Vérifier Evaluation.DRAW avec new OracleTerminal().evaluate(p).
    }
}
```

Un test prépare une situation, effectue une action et compare le résultat à une attente choisie à l’avance. `assertEquals(attendu, obtenu)` place l’attendu en premier. Pour le premier test, construisez le coup avec `new Move(Square.parse("h7"), Square.parse("h8"))` et enveloppez-le dans `Optional.of(...)`. Le fichier voisin ChooseBestMoveServiceTest fournit aussi des exemples de syntaxe.

Le premier test doit montrer que le service choisit h7h8 sans utiliser la table réelle. Le second rend visible la limite de la simulation : cette position encore en cours est traitée comme nulle. Une méthode de test sans assertion ne suffit pas.

Relancez la suite : **35 tests attendus après les deux ajouts**, tous réussis. Facultatif si vous avez le temps : ajoutez un troisième test vérifiant directement `Evaluation.loss(0)` pour roi blanc b6, tour h8, roi noir a8, trait aux noirs. Avec ce prolongement, vous obtenez 36 tests.

Consultez `git diff`, puis `git add .` et `git commit -m "Tester le service avec un oracle simule"` depuis TD1. Expliquez quel défaut votre assertion du mat détecterait et pourquoi elle ne démontre pas que la simulation joue parfaitement. Les commits sont locaux : avant de quitter la salle, conservez le dossier `R304-2026-2027` entier si vous avez cloné, ou le dossier `TD1` entier si vous avez utilisé le ZIP, avec son dossier caché `.git` dans les deux cas.

## 80–90 minutes — Bilan

Conservez vos deux schémas et cinq à huit lignes : classes ajoutées, rôle du contrat et de l’assemblage, résultat attendu d’un test, raison pour laquelle cette simulation n’est pas équivalente à l’oracle réel. Le test construit le service avec une autre implémentation : il sert d’assemblage alternatif. Chacun doit pouvoir expliquer une flèche ; pas de recette individuelle.

Le cours donnera les noms : domaine pour les règles, cas d’utilisation pour le choix d’un coup, port pour le contrat et adaptateur pour sa réalisation. Une compatibilité de type ne suffit pas à garantir un même comportement.

## Mode dégradé

L’enseignant fournit ou projette les mêmes extraits. Rédigez classe, assertions et schémas sur papier, puis confrontez-les à l’exécution collective. Reprenez sur machine dès qu’elle est disponible. Si le temps manque, l’enseignant peut fournir OracleTerminal pour concentrer le travail sur les tests et l’explication. Aucun exercice supplémentaire à domicile n’est ajouté.
