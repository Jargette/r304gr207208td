# TD1 — Un moteur d’échecs KRK qui joue mal, puis bien

R304 FI — 90 minutes — groupes de 3 à 5. Cette séance précède le premier cours. Aucune connaissance de Clean Architecture ni des échecs n’est supposée. Un portable par groupe suffit. Pas d’IA pendant le TD.

## Le projet des TD : construire un petit moteur d’échecs

Pendant les TD, votre groupe va écrire et surtout concevoir un petit **moteur d’échecs** pour la finale **KRK**, *King, Rook, King* : un roi et une tour blancs contre un roi noir. Un moteur d’échecs est un programme qui joue : à partir d’une position, il liste les coups permis, évalue la position obtenue après chacun, joue le meilleur, et dialogue avec l’extérieur, une console ou un logiciel qui affiche l’échiquier.

Aujourd’hui, vous recevez un moteur complet qui fonctionne. Il joue mal. Vous allez jouer contre lui, comprendre comment il est organisé, puis le rendre imbattable en changeant **une ligne** (et son import). Aucune classe ni aucun test à écrire. Conservez une feuille de groupe (papier ou fichier) avec vos réponses et votre schéma. Pas de rapport séparé ni de dépôt à remettre.

Au TD suivant, le code fourni aura un défaut, et ce sera à vous de le trouver, de le corriger et de prouver qu’il a disparu. Les séances suivantes construisent le moteur étape par étape, jusqu’à une version que vous aurez fait évoluer vous-mêmes, avec ses tests, un schéma fidèle au code et une version identifiable dans Git. Ce déroulé est prévisionnel et sera précisé séance par séance.

## La finale roi et tour contre roi

Les lettres indiquent les colonnes, les chiffres les lignes : b6 est la colonne b, ligne 6. ♔ est le roi blanc, ♖ la tour blanche et ♚ le roi noir. Dans la console du programme, ces pièces s’écrivent `K`, `R` et `k`. Un camp est **au trait** quand c’est à lui de jouer.

Trois règles suffisent aujourd’hui. La tour attaque toutes les cases de sa ligne et de sa colonne, sans traverser une pièce. Le roi se déplace d’une case dans toutes les directions ; il attaque donc ses cases voisines, et il ne peut pas aller sur une case attaquée par l’adversaire (on dit aussi contrôlée). Un roi attaqué est en **échec**. Un roi en échec est **mat** s’il n’a aucune case sûre : aucune case voisine libre et non attaquée, et pas de tour voisine qu’il puisse prendre sans danger. La partie est alors perdue pour son camp.

**Le départ de vos parties, les blancs au trait** : roi blanc e1, tour a1, roi noir e5.

| | a | b | c | d | e | f | g | h |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 8 | | | | | | | | |
| 7 | | | | | | | | |
| 6 | | | | | | | | |
| 5 | | | | | ♚ | | | |
| 4 | | | | | | | | |
| 3 | | | | | | | | |
| 2 | | | | | | | | |
| 1 | ♖ | | | | ♔ | | | |

**Un exemple de mat, les noirs au trait** : le roi noir est attaqué par la tour le long de la ligne 8 ; a7 et b7 sont attaquées par le roi blanc, b8 par la tour ; aucune case sûre.

| | a | b | c | d | e | f | g | h |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 8 | ♚ | | | | | | | ♖ |
| 7 | | | | | | | | |
| 6 | | ♔ | | | | | | |
| 5 | | | | | | | | |
| 4 | | | | | | | | |
| 3 | | | | | | | | |
| 2 | | | | | | | | |
| 1 | | | | | | | | |

Avec un roi et une tour contre un roi seul, les blancs peuvent toujours forcer le mat, en seize coups au plus s’ils jouent parfaitement. Mais si les blancs ne savent pas s’y prendre, la partie est **nulle** : quand la tour est prise, quand le roi noir, sans être en échec, n’a plus aucun coup permis (c’est le **pat**), ou après cinquante coups de chaque camp sans mat.

## Les responsabilités du moteur, et ses couches

Le moteur fourni est découpé en **responsabilités**. Une responsabilité, c’est ce dont une partie du programme s’occupe, et qui a sa propre raison de changer. On la nomme par un verbe.

| Responsabilité | Ce qu’elle sait | Où, dans le code |
|---|---|---|
| **Connaître les règles** : cases, positions, coups permis, échec, mat, pat. | Ce qui est légal. | `org.chesskrk.domain` : `Square`, `Position`, `Move`, `Rules`, `GameStatus`, `Color` |
| **Choisir un coup** : parmi les coups permis, prendre celui qui mène à la meilleure position. | Comment décider, à condition qu’on lui dise ce que vaut une position. | `org.chesskrk.application` : `ChooseBestMoveService` ; `ChooseMoveUseCase`, contrat de choix d’un coup ; `EndgameOracle`, contrat d’évaluation d’une position, et `Evaluation`, la réponse de l’évaluateur |
| **Évaluer une position** : dire si elle est gagnée, nulle ou perdue, et en combien de coups. | La valeur des positions, par une méthode ou une autre. | `org.chesskrk.adapter.naive` : `NaiveOracle` ; `org.chesskrk.adapter.tablebase` : `RetrogradeTablebase` |
| **Parler à l’extérieur** : afficher, lire les coups de l’adversaire. | Le dialogue, en console ou par le protocole UCI d’un logiciel d’échecs. | `org.chesskrk.adapter.console` : `ConsoleGame` ; `org.chesskrk.adapter.uci` : `UciSession`, `FenCodec`, `UciMoveCodec` |
| **Assembler** : créer les objets et les relier au démarrage. | Quels objets concrets composent le programme. | `org.chesskrk.infrastructure` : `Main` |

Ces responsabilités sont rangées en **couches**, du centre vers l’extérieur : le **domaine** (les règles), l’**application** (le choix, le contrat de choix d’un coup qu’il offre et le contrat d’évaluation d’une position qu’il exige), les **adaptateurs** (évaluer, parler), l’**infrastructure** (assembler). C’est le découpage appelé **Clean Architecture** ; vous le retrouverez en cours. Sa règle unique, que vous vérifierez aujourd’hui sur un cas : **une couche ne connaît que les couches plus au centre qu’elle**. Les règles ne connaissent personne. Le choix connaît les règles, mais pas la console ni la table. Seul l’assemblage connaît tout le monde.

Entre deux responsabilités passe une **frontière** ; une seule nous occupe aujourd’hui, celle entre la responsabilité de choisir un coup et celle d’évaluer une position. Le reste du moteur, UCI compris, est là pour plus tard. Les pièces, roi et tour, sont supposées ne jamais changer : elles vivent dans le domaine.

## 0–20 min — Présentation et ouverture du projet

Un membre ouvre le projet pendant la présentation de l’enseignant ; les autres l’écoutent. Alternez ensuite les rôles de lecteur, pilote du clavier et rédacteur.

Le dépôt public est [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). Dans IntelliJ IDEA, choisissez **Get from VCS** (ou **Clone Repository** selon la version) sur l’écran d’accueil, puis saisissez l’URL `https://github.com/mikalziane/R304-2026-2027.git` dans le champ **URL**. Choisissez votre dossier personnel sur votre portable ; sur un poste IUT, utilisez votre espace personnel sur `Z:`. Si vous avez déjà récupéré le dépôt en TP1, ne clonez pas à nouveau : ouvrez ce clone (**File > Open** sur son dossier) et mettez-le à jour avec **Git > Update Project** (`Ctrl+T`), ou `git pull` dans le terminal. Le dossier `TD1/src/main/java/org/chesskrk` doit contenir les dossiers `domain`, `application`, `adapter` et `infrastructure` ; sinon, appelez l’enseignant.

Dans les deux cas, choisissez ensuite **File > Open**, ouvrez le dossier `TD1` du dépôt cloné et sélectionnez son fichier `pom.xml`. Si IntelliJ vous le propose, cliquez sur **Open as Project**, puis sur **This Window**. Maven, l’outil qui compile le projet et télécharge ses bibliothèques, se met alors au travail ; une connexion Internet est nécessaire la première fois. Attendez que la barre de progression en bas de la fenêtre disparaisse : l’import est alors terminé. Dans **File > Project Structure > Project**, choisissez un **JDK 21** ou plus récent dans **SDK**. Si aucun JDK 21 n’apparaît, choisissez **Add SDK > Download JDK** dans ce même écran.

Ouvrez `src/main/java/org/chesskrk/infrastructure/Main.java` et lancez-le avec le triangle vert à côté de `main`. **Résultat attendu :** dans la fenêtre **Run**, un échiquier en lettres et la ligne « Votre roi (e5) va en : ». Vous jouez le roi noir ; le moteur joue les blancs. Tapez la case d’arrivée de votre roi, par exemple `d4`, puis **Entrée**. Un coup illégal est refusé. `q` abandonne la partie. Pour rejouer, relancez `Main`.

**Terminal, uniquement en secours.** Un terminal est la fenêtre dans laquelle on tape des commandes. Dans IntelliJ IDEA, ouvrez **View > Tool Windows > Terminal** (`Alt+F12` sous Windows). L’invite affiche le dossier courant. Il faut être dans le dossier `TD1`, celui qui contient `pom.xml` et `mvnw.cmd`. Si l’invite se termine par le nom du dépôt, `R304-2026-2027`, tapez `cd TD1` puis **Entrée** ; si elle montre un autre dossier, appelez l’enseignant. Tapez ensuite chaque ligne puis validez par **Entrée** ; la première ligne évite que les accents s’affichent mal :

```powershell
chcp 65001
.\mvnw.cmd -q compile
java -cp target/classes org.chesskrk.infrastructure.Main
```

La première utilisation demande Internet pour télécharger les dépendances. Si le démarrage bloque, signalez-le et passez à la lecture : les fichiers se lisent aussi dans un navigateur, à l’adresse `https://github.com/mikalziane/R304-2026-2027/tree/main/TD1/src`, et l’enseignant projette une partie.

## 20–35 min — Jouer et observer

Jouez deux ou trois parties, en changeant de pilote. **Défi : tenez cinquante coups sans vous faire mater.**

Sur votre feuille, notez ce que fait le moteur. Que fait sa tour ? Donne-t-il des échecs ? Laisse-t-il sa tour en prise ? Vous menace-t-il un jour sérieusement de mat ? Puis répondez : ce qui lui manque, est-ce la connaissance des règles (joue-t-il un coup interdit ?) ou le jugement sur les positions ? Justifiez par ce que vous avez vu.

## 35–55 min — La carte du moteur et le trajet d’un coup

Tous les chemins suivants sont relatifs à `TD1/src/main/java/org/chesskrk`.

1. Dans le panneau **Project** d’IntelliJ, dépliez les quatre dossiers `domain`, `application`, `adapter` et `infrastructure`. Sur votre feuille, dessinez quatre cercles emboîtés, domaine au centre, puis application, adaptateurs, infrastructure, et placez chaque classe dans son cercle à l’aide du tableau des responsabilités. Les fichiers `package-info.java` décrivent chaque dossier en une phrase.
2. Ouvrez `adapter/console/ConsoleGame.java`. Repérez la méthode `whiteMoves` de ce fichier et la ligne où la console demande un coup au moteur : `engine.chooseMove(position)`. Quel est le **type déclaré** du champ `engine`, c’est-à-dire le type écrit devant son nom dans sa déclaration ? Est-ce une classe ou une **interface** (mot-clé `interface`, méthodes sans corps) ? Le résultat `Optional<Move>` est « un coup, ou rien s’il n’y a aucun coup permis ».
3. Ouvrez `application/usecase/ChooseBestMoveService.java`. Dans `chooseMove`, suivez ce qui se passe pour un coup : `Rules.legalMoves` donne les coups permis, `Rules.apply` calcule la position après chaque coup, `oracle.evaluate(...)` demande la valeur de cette position, et `min` garde le meilleur. L’écriture `stream()…min(...)` est une façon compacte de parcourir la liste et de garder le plus petit score ; inutile de la détailler. Quelle responsabilité ce fichier exerce-t-il ? Laquelle délègue-t-il, et à qui ?
4. Ouvrez `application/port/out/EndgameOracle.java` en entier. C’est une interface Java, c’est-à-dire un contrat : des méthodes promises, sans leur code. Ici, c’est le **contrat d’évaluation d’une position**. Il promet une méthode, `evaluate`, qui reçoit une position et rend une `Evaluation` : gain, nulle ou perte pour le camp au trait, et nombre de demi-coups jusqu’au mat (un demi-coup est le coup d’un seul camp). Qui a besoin de ce contrat ? Qui le tient ?
5. Ouvrez `adapter/naive/NaiveOracle.java`. Le mot `implements` signifie que cette classe tient le contrat d’évaluation d’une position. Lisez `evaluate` : quelles sont ses trois règles de débutant ? Que répond-elle pour toute autre position ? Après un coup des blancs, ce sont les noirs qui sont au trait : l’évaluateur répond de leur point de vue, et ce qui est perdu pour eux est bon pour le moteur. Les nombres 2 et 50 sont des ordres de grandeur, pas un vrai compte de demi-coups. Reliez ces réponses à ce que vous avez observé en jouant.

**Trace à conserver :** le trajet d’un coup en quatre lignes, un fichier par ligne, de la console à l’évaluateur naïf. Une autre personne que le pilote l’explique au groupe.

**Indice de lecture volontaire :** une variable dont le type déclaré est une interface peut désigner un objet de n’importe quelle classe qui l’implémente, comme l’exemple `Tarif` et `TarifFixe` de la présentation. Ce qui est écrit dans le fichier, le type déclaré, et ce qui s’exécute, la classe de l’objet, sont deux choses différentes.

## 55–70 min — Le graphe de dépendances, et un pari

Une flèche de dépendance va de la classe ou interface qui **utilise un type** vers ce **type utilisé** : type d’un champ, d’un paramètre, d’une variable ou d’un résultat, objet créé avec `new`, ou interface implémentée. On regarde les noms écrits dans le code, pas l’ordre des appels à l’exécution.

Recopiez ces cinq noms et tracez les flèches de dépendance entre eux, en cherchant chaque flèche dans les déclarations Java :

```text
[ ConsoleGame ]   [ ChooseBestMoveService ]   [ EndgameOracle ]

                  [ NaiveOracle ]   [ RetrogradeTablebase ]
```

1. Pour chaque flèche, relevez la déclaration qui la justifie. Vous pouvez ignorer les flèches vers le domaine, que tout le monde utilise.
2. `ChooseBestMoveService` et `NaiveOracle` : y a-t-il une flèche entre eux, dans un sens ou dans l’autre ? Comparez avec ce qui se passe à l’exécution, puis expliquez avec l’indice de lecture.
3. **Le pari.** On veut que le moteur évalue les positions avec `RetrogradeTablebase`, une table qui connaît la valeur exacte de toutes les positions KRK, au lieu de `NaiveOracle`. D’après votre graphe, quels fichiers faudra-t-il modifier, et lesquels resteront intacts ? `ChooseBestMoveService` doit-il changer ? Écrivez votre réponse sur la feuille avant de continuer.
4. Une fois le pari écrit, ouvrez `infrastructure/Main.java` et ajoutez-le au graphe avec ses flèches. Que remarquez-vous ? Votre pari tient-il toujours ?

Faites un point avec l’enseignant avant de continuer. Changez de pilote. La section suivante est repliée : ne l’ouvrez qu’après ce point.

## 70–85 min — Le verdict du pari

<details>
<summary>À ouvrir après le point avec l’enseignant</summary>

Dans `infrastructure/Main.java`, méthode `buildUseCase`, remplacez la ligne

```java
EndgameOracle oracle = new NaiveOracle();
```

par

```java
EndgameOracle oracle = RetrogradeTablebase.build();
```

IntelliJ souligne `RetrogradeTablebase` en rouge : placez le curseur dessus, appuyez sur `Alt+Entrée` et choisissez **Import class**. L’import de `NaiveOracle` devient inutile et apparaît en gris ; vous pouvez le laisser ou le supprimer. Relancez `Main` et rejouez. Le calcul de la table prend une seconde au démarrage.

1. Combien de coups avez-vous tenu cette fois ? Le moteur fait-il encore des échecs sans suite ?
2. Comparez avec votre pari : combien de fichiers ont changé ? Combien de lignes ? Votre graphe a-t-il changé ?
3. À l’exécution, quel objet reçoit maintenant l’appel `oracle.evaluate(...)` ? Le nom de sa classe apparaît-il dans `ChooseBestMoveService.java` ? Montrez le code.

Si vous avez le temps, lancez les tests : clic droit sur le dossier `src/test/java`, **Run 'All Tests'**. **Résultat attendu : 41 tests réussis.** Parmi eux, `CleanArchitectureTest` vérifie automatiquement la règle des couches : si une classe du choix nommait la console ou la table, ce test échouerait.

</details>

## 85–90 min — Bilan et préparation de la suite

Sur votre feuille, répondez brièvement :

1. Qu’est-ce qui a rendu le moteur imbattable : un changement des règles, du choix, de l’évaluation, ou de l’assemblage ?
2. Pourquoi une seule ligne a-t-elle suffi ? Répondez avec les mots « contrat d’évaluation » et « interface ».
3. Si demain on écrivait une troisième façon d’évaluer, quels fichiers changeraient ?

Gardez vos réponses, la trace du coup et le graphe pour la prochaine séance. Chacun doit pouvoir expliquer une flèche, sans soutenance individuelle. Au TD suivant, le code fourni aura un défaut de ce genre, une classe qui connaît ce qu’elle ne devrait pas, et vous le ferez disparaître.

**Si le temps manque :** jouez une seule partie, faites le trajet du coup et le pari avec l’enseignant, puis la ligne de `Main`. Les questions de bilan peuvent être discutées collectivement ; aucun travail supplémentaire à domicile n’est imposé.
