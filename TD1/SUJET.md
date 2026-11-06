# TD1 — Tester le choix d’un coup avec une simulation

R304 FI — 90 minutes — groupes de 3 à 5. Cette séance précède le premier cours. Aucune préparation préalable ni connaissance de Clean Architecture n’est supposée. Un portable par groupe suffit. Pas d’IA pendant le TD.

## Le projet KRK

Pendant les TD de R304, votre groupe fera évoluer un programme qui choisit des coups dans une finale d’échecs. Une finale est la dernière phase d’une partie, quand il reste peu de pièces. Dans la finale **KRK** (*King, Rook, King*), les blancs ont un roi et une tour, et les noirs ont un roi.

Le programme fourni reçoit une position et propose un coup au camp qui doit jouer. Au fil du projet, vous apprendrez à modifier une partie de ce programme, à vérifier son comportement avec des tests et à enregistrer votre travail dans Git. L’objectif est de comprendre comment faire évoluer une partie du code sans devoir retoucher toutes les autres.

## Comprendre la finale

La tour avance en ligne droite, horizontalement ou verticalement, sans sauter par-dessus une pièce. Le roi avance d’une case dans n’importe quelle direction et ne peut pas aller sur une case attaquée. Les blancs cherchent à faire **mat** au roi noir. Il est mat s’il est attaqué et n’a aucun coup légal. S’il n’a aucun coup légal sans être attaqué, c’est un **pat** et la partie est nulle. Elle est aussi nulle si le roi noir prend la tour et qu’il ne reste que les deux rois.

Voici la position utilisée dans le test du TD1. Les lettres repèrent les colonnes et les chiffres les lignes. Par exemple, b6 désigne la colonne b, ligne 6. ♔ représente le roi blanc, ♖ la tour blanche et ♚ le roi noir.

**Avant le coup.** Les blancs doivent jouer.

|  | a | b | c | d | e | f | g | h |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 8 | ♚ |  |  |  |  |  |  |  |
| 7 |  |  |  |  |  |  |  | ♖ |
| 6 |  | ♔ |  |  |  |  |  |  |
| 5 |  |  |  |  |  |  |  |  |
| 4 |  |  |  |  |  |  |  |  |
| 3 |  |  |  |  |  |  |  |  |
| 2 |  |  |  |  |  |  |  |  |
| 1 |  |  |  |  |  |  |  |  |

**Après le coup h7 → h8.** Les noirs doivent jouer.

|  | a | b | c | d | e | f | g | h |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 8 | ♚ |  |  |  |  |  |  | ♖ |
| 7 |  |  |  |  |  |  |  |  |
| 6 |  | ♔ |  |  |  |  |  |  |
| 5 |  |  |  |  |  |  |  |  |
| 4 |  |  |  |  |  |  |  |  |
| 3 |  |  |  |  |  |  |  |  |
| 2 |  |  |  |  |  |  |  |  |
| 1 |  |  |  |  |  |  |  |  |

La tour blanche en h8 attaque le roi noir en a8. Depuis ce coin, les seules cases voisines du roi noir sont a7, b7 et b8. Le roi blanc contrôle a7 et b7, et la tour contrôle b8. Le roi noir est donc mat. Votre test vérifiera que le programme choisit ce coup.

## Ce que vous faites au TD1

Pour choisir un coup, le moteur recense les coups autorisés et demande à un composant d’évaluer les positions obtenues. Évaluer une position consiste ici à indiquer si le camp qui doit jouer peut gagner, perd ou fait nulle. Vous allez lire comment ces deux parties communiquent, puis écrire une petite simulation de l’évaluateur qui reconnaît le mat immédiat. Dans un test JUnit, cette simulation vous permet de vérifier le choix du moteur sans calculer la valeur de toutes les positions. Le moteur et les règles du jeu sont fournis.

À la fin : deux schémas du programme, une classe OracleTerminal, deux tests dans TD1Test et un court bilan du groupe. Conservez-les dans votre dossier de travail, avec un commit local. Aucun rapport séparé ni push sur GitHub n’est demandé.

## 0–20 minutes — Présentation et démarrage en parallèle

Le dépôt commun est [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). Si vous ne l’avez pas, ouvrez PowerShell dans votre dossier personnel. Tapez les commandes suivantes, une ligne à la fois, en validant chacune avec **Entrée**.

```text
git clone https://github.com/mikalziane/R304-2026-2027.git
cd R304-2026-2027/TD1
```

Si vous avez déjà un clone pour le TP1, commencez par `git status` à sa racine. S’il est propre, récupérez la mise à jour avec `git pull --ff-only`, puis entrez dans `TD1`. Si vous avez des modifications, conservez-les et demandez à l’enseignant la copie ZIP du TD1 : ne réinitialisez pas et n’écrasez pas votre travail.

En secours, extrayez entièrement `base-etudiante.zip`, puis ouvrez le dossier `TD1` obtenu. Il contient `pom.xml`, `mvnw.cmd`, `mvnw`, `.mvn`, `src` et `SUJET.md`. N’ouvrez pas l’archive sans l’extraire. Tous les chemins ci-dessous sont relatifs à ce dossier TD1.

**JDK 21 minimum requis.** Dans IntelliJ, ouvrez `pom.xml` dans ce dossier TD1 comme projet Maven ; choisissez un JDK 21 ou plus récent pour le SDK du projet et pour Maven. Les autres IDE peuvent importer le même pom.xml. Maven télécharge les dépendances lors de la première utilisation ; le lanceur est fourni, donc vous n’avez pas à installer Maven séparément.

Dans IntelliJ IDEA, ouvrez **View > Tool Windows > Terminal** (`Alt+F12`). Vous pouvez aussi continuer dans PowerShell. Vérifiez que le terminal se trouve dans `TD1`, puis lancez les commandes suivantes.

```powershell
java -version
.\mvnw.cmd -version
.\mvnw.cmd test
```

Vérifiez que Java et Maven annoncent un JDK 21 ou plus récent. Si les versions diffèrent, faites vérifier `JAVA_HOME` et les réglages de l’IDE. Résultat de départ attendu : **33 tests, zéro échec**. Vous pouvez aussi lancer les tests depuis l’IDE.

Si le démarrage bloque plus de dix minutes, signalez-le et passez à la lecture avec les fichiers locaux pendant la résolution du problème. Un ZIP ne fournit pas les dépendances Maven hors ligne.

**Git.** Dans un clone, ne faites pas `git init`. Avec un ZIP, créez une seule fois l’historique dans le dossier TD1 : `git init`, puis `git add .` et `git commit -m "Base de départ TD1"`. Si Git demande une identité, utilisez vos propres valeurs puis relancez le commit :

```text
git config user.name "Votre prénom et nom"
git config user.email "Votre adresse personnelle"
```

Désignez un pilote du clavier, un lecteur du code et un responsable des tests ; répartissez aussi les schémas si vous êtes quatre ou cinq. Changez de pilote vers le milieu de la séance.

## Lire le résultat d’une évaluation

Une Evaluation indique WIN (gain), DRAW (nulle) ou LOSS (perte), du point de vue du camp qui doit jouer dans la position évaluée. Après un coup blanc, on évalue une position où les noirs doivent jouer : leur LOSS est favorable aux blancs. `loss(0)` signifie que ce camp est déjà mat.

## 20–32 minutes — Découvrir le code à partir d’un coup

Travaillez en groupe, sans modifier le code. Prenez la position du premier échiquier. Imaginez que le programme examine le coup h7 → h8. Votre but est de repérer où le programme trouve les coups possibles, construit la position suivante et demande une évaluation.

1. Dans `src/main/java/org/chesskrk/domain/Position.java`, lisez la déclaration de `Position`. Repérez ce qui décrit les trois pièces et le camp qui doit jouer. Relevez les valeurs correspondantes sur le premier échiquier, puis indiquez celles qui changent sur le second.
2. Dans le même paquetage, ouvrez `Rules.java`. Repérez les signatures de `legalMoves` et de `apply`. Associez chaque méthode à une question : « Quels coups puis-je jouer ? » ou « Quelle position obtiendrai-je si je joue ce coup ? ». Dites ce qu’on lui donne et ce qu’elle renvoie. L’implémentation de ces méthodes peut attendre.
3. Ouvrez `src/main/java/org/chesskrk/application/usecase/ChooseBestMoveService.java`. Dans `chooseMove`, retrouvez les deux appels précédents, puis l’appel à `evaluate`. Pour lire cet appel imbriqué, commencez par le plus intérieur, `Rules.apply(position, move)`. Quel résultat reçoit `evaluate` ? De quel camp est-ce alors le tour ? Reformulez le trajet en trois courtes phrases.

Le lecteur montre les lignes au groupe, et une autre personne explique le trajet à voix haute. Si les streams, la lambda ou le comparateur vous arrêtent, repérez seulement les appels nommés. Pour la suite, `Optional<Move>` désigne un résultat qui peut contenir un coup ou être vide.

## 32–45 minutes — Lire la frontière entre le choix et l’évaluation

Vous venez de suivre une position jusqu’à l’appel `oracle.evaluate(...)`. Cherchez maintenant d’où vient l’objet `oracle`. On parle de **frontière** parce que le choix du coup et le calcul de l’évaluation sont deux tâches distinctes qui communiquent par un contrat Java.

1. Restez dans `ChooseBestMoveService.java`. Lisez la déclaration du champ `oracle` et le constructeur. Relevez le **type** de l’objet que le service reçoit et la ligne qui le mémorise.
2. Sous `src/main/java/org/chesskrk`, ouvrez `application/port/out/EndgameOracle.java`. Lisez la signature de `evaluate`. Notez l’entrée et le résultat que le service peut demander grâce à ce contrat.
3. Dans `infrastructure/Main.java`, cherchez seulement `buildUseCase`. Quel objet est passé au constructeur du service ? Ouvrez `adapter/tablebase/RetrogradeTablebase.java` et cherchez `implements` pour vérifier le lien entre cette classe et le contrat.

Le **type** se lit dans les déclarations et signatures. L’**objet** est l’instance effectivement utilisée quand le programme tourne. Pour expliquer une flèche, montrez soit le nom du type dans le fichier, soit l’appel que l’objet recevra à l’exécution.

**Deux schémas, deux sens de flèches.** Exemple neutre : un service de livraison reçoit un objet de type interface Tarif ; TarifFixe implémente Tarif.

```text
Dépendances de types : ServiceLivraison --utilise--> Tarif
                      TarifFixe --implémente--> Tarif
Appel à l’exécution : objet service --appelle calculer--> objet tarifFixe
```

Sur votre feuille, transposez ce modèle à `ChooseBestMoveService`, `EndgameOracle` et `RetrogradeTablebase`. Dans le schéma de types, ajoutez `Main` et les types qu’il nomme pour construire les objets. Étiquetez les flèches « utilise », « implémente » ou « construit ». Dans le schéma des appels, représentez un objet service appelant `evaluate` sur un objet oracle. Expliquez en une phrase pourquoi les deux schémas diffèrent.

### Repères de Clean Architecture

C’est votre premier exemple de **Clean Architecture**. Le code est organisé pour que les règles et les décisions du programme puissent évoluer séparément des détails qui les entourent. Voici les rôles que vous allez rencontrer :

- Le **domaine** représente les positions et les coups, et détermine les coups autorisés.
- Le **cas d’utilisation** réalise une action du programme. Ici, `ChooseBestMoveService` choisit un coup en s’appuyant sur les règles et sur une évaluation des positions.
- Le **port** décrit le service dont le cas d’utilisation a besoin. Ici, l’interface `EndgameOracle` déclare comment demander une évaluation. L’**adaptateur** fournit ce service. Dans le moteur, `RetrogradeTablebase` évalue les positions à partir de la table de finales.
- L’**assemblage** choisit les objets concrets et les relie. Ici, `Main` donne au service l’objet qui évalue les positions. Dans votre test, c’est le test lui-même qui construit et relie les objets.

La **règle des dépendances** porte sur les types que le code connaît. Les dépendances vont vers l’intérieur : les adaptateurs peuvent connaître l’application, et l’application peut connaître le domaine. Ici, le port d’évaluation appartient à l’application. Le service le connaît pour demander une évaluation, et l’adaptateur le connaît parce qu’il l’implémente. Pendant l’exécution, le service appelle l’objet adaptateur à travers le port. Dans le code du service, la dépendance vers l’évaluateur s’arrête au port.

## 45–65 minutes — Créer une simulation limitée

Créez `src/main/java/org/chesskrk/adapter/pedagogie/OracleTerminal.java`, avec le paquetage `org.chesskrk.adapter.pedagogie`. Cette classe publique implémente EndgameOracle. Sa méthode publique evaluate reçoit une Position et retourne une Evaluation :

- si `Rules.status(position) == GameStatus.CHECKMATE`, retourner `Evaluation.loss(0)` ;
- dans tous les autres cas, retourner `Evaluation.DRAW`.

Imports utiles : `org.chesskrk.application.port.out.EndgameOracle`, `org.chesskrk.application.port.out.Evaluation` et, dans `org.chesskrk.domain`, Position, Rules et GameStatus. Les dossiers reflètent les noms de paquetages. Votre IDE peut créer les dossiers et proposer les imports.

Cette simulation ne connaît que le mat immédiat. DRAW veut ici dire « traité comme nul par la simulation », pas « vraiment nul ». Elle possède la bonne signature Java mais ne respecte pas toute la promesse de connaissance parfaite du véritable oracle. Nous la branchons uniquement dans des tests, en laissant Main, le service et les règles inchangés. Les contrôles automatiques d’architecture fournis connaissent déjà le paquetage pedagogie.

## 65–83 minutes — Écrire deux tests et conserver le travail

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

## 83–90 minutes — Bilan

Conservez vos deux schémas et cinq à huit lignes : classes ajoutées, rôle du contrat et de l’assemblage, résultat attendu d’un test, raison pour laquelle cette simulation n’est pas équivalente à l’oracle réel. Le test construit le service avec une autre implémentation : il sert d’assemblage alternatif. Chacun doit pouvoir expliquer une flèche ; pas de recette individuelle.

Dans le schéma des types, ajoutez votre simulation et son lien vers `EndgameOracle`. Indiquez où le test la relie au cas d’utilisation. Quel fichier faudrait-il modifier pour utiliser votre simulation si le service créait lui-même la table de finales ? Comparez avec les fichiers réellement modifiés pendant ce TD. Vous utiliserez ce raisonnement dans la suite du projet pour décider où placer un nouveau contrat.

## Mode dégradé

L’enseignant fournit ou projette les mêmes extraits. Rédigez classe, assertions et schémas sur papier, puis confrontez-les à l’exécution collective. Reprenez sur machine dès qu’elle est disponible. Si le temps manque, l’enseignant peut fournir OracleTerminal pour concentrer le travail sur les tests et l’explication. Aucun exercice supplémentaire à domicile n’est ajouté.
