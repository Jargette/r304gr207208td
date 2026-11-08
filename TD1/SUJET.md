# TD1 — Découvrir une séparation du programme

R304 FI — 90 minutes — groupes de 3 à 5. Cette séance précède le premier cours. Aucune connaissance de Clean Architecture ni des échecs n’est supposée. Un portable par groupe suffit. Pas d’IA pendant le TD.

## Un petit point de départ pour le projet KRK

KRK signifie *King, Rook, King* : un roi et une tour blancs contre un roi noir. Le projet servira à étudier comment organiser et faire évoluer un programme Java.

Aujourd’hui, vous disposez de **cinq fichiers Java, test compris**, tous dans le dossier `TD1` :

- `src/main/java/krk/domaine/Position.java`
- `src/main/java/krk/application/ChoisirPosition.java`
- `src/main/java/krk/application/EvaluateurPosition.java`
- `src/main/java/krk/adaptateur/EvaluationDeuxPositions.java`
- `src/test/java/krk/ChoisirPositionTest.java`

Le programme choisit entre **deux positions déjà fournies**. Il ne calcule pas les coups permis et ne joue pas une partie. L’évaluateur fourni reconnaît ces deux positions et rejette toute autre position qu’il reçoit.

Vous allez lancer les tests existants, lire tout ce petit programme, suivre les objets et compléter un schéma. **Aucune classe ni aucun test à écrire aujourd’hui.** Conservez une feuille de groupe (papier ou fichier) avec vos réponses et votre schéma. Pas de rapport séparé ni de dépôt à remettre.

## D’où l’on part, où l’on va

**Aujourd’hui.** La règle de choix est simple : garder la position où les noirs sont mats si elle existe, sinon la première. Le mot « mat » est expliqué plus bas. Pour savoir si une position est un mat, le programme ne calcule rien : il pose la question à un *évaluateur*, qui ne connaît que les deux positions de l’exemple, A et B, dessinées plus bas.

**Un vrai programme d’échecs KRK** fait plus : à partir de n’importe quelle position, il liste les coups permis, évalue la position obtenue après chacun, joue le meilleur et dialogue avec un logiciel qui affiche l’échiquier. Nos cinq fichiers en sont la plus petite tranche : « choisir en demandant à un évaluateur ». La frontière, c’est-à-dire la limite entre la partie qui choisit et la partie qui évalue, que vous allez lire aujourd’hui existe, sous une forme très proche, dans le programme complet.

**Au fil des TD et à la fin**, déroulé prévisionnel précisé séance par séance :

- TD1 : lire cette frontière et la justifier avec le code.
- TD2 : remplacer l’évaluateur dans un test ; vous verrez ce qui doit changer et ce qui ne change pas.
- Ensuite : écrire de vraies règles (attaque, échec, mat) ; choisir un coup parmi tous les coups permis ; changer la façon dont le programme parle à l’extérieur ; brancher un évaluateur fourni qui connaît le résultat exact de toute position.
- L’objectif est qu’à la fin votre groupe ait un petit programme KRK qui fonctionne, que vous aurez fait évoluer étape par étape, avec ses tests, un schéma fidèle au code et une version identifiable dans Git.

Vous saurez alors dire, pour un changement annoncé, quels fichiers bougent et lesquels restent intacts. C’est la compétence visée ; le contrôle individuel de TD, en semaine 8, vous demandera de l’expliquer.

## Les deux positions de l’exemple

Les lettres indiquent les colonnes, les chiffres les lignes : b6 est la colonne b, ligne 6. ♔ est le roi blanc, ♖ la tour blanche et ♚ le roi noir. **Dans les deux positions, les noirs doivent jouer.**

| | a | b | c | d | e | f | g | h |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 8 | ♚ | | | | | | | |
| 7 | | | | | | | | |
| 6 | | ♔ | | | | | | ♖ |
| 5 | | | | | | | | |
| 4 | | | | | | | | |
| 3 | | | | | | | | |
| 2 | | | | | | | | |
| 1 | | | | | | | | |

**Position A : b6, h6, a8.** Le roi noir n’est attaqué ni par la tour ni par le roi blanc. Il n’est donc pas mat ; il peut par exemple aller en b8 et la partie continue.

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

**Position B : b6, h8, a8.** La tour attaque le long de sa ligne et de sa colonne. Le roi blanc attaque ses cases voisines. Un roi ne peut pas se déplacer sur une case attaquée (on dit aussi contrôlée) par l’adversaire. Un roi attaqué qui n’a aucune case sûre, c’est-à-dire libre et non attaquée, est **mat** ; la partie est perdue pour son camp. Ici, le roi noir est attaqué par la tour et aucune case voisine n’est sûre : a7 et b7 sont voisines du roi blanc, b8 est sur la ligne 8 attaquée par la tour. Il est donc mat.

Au départ, la tour blanche est en h7 et les blancs jouent ; cette position de départ n’est pas dessinée. Ils déplacent leur tour en h6 pour obtenir A, ou en h8 pour obtenir B. C’est alors aux noirs de jouer. Le programme reçoit directement les positions A et B.

**Règle de choix donnée :** privilégier une position de mat ; si les deux positions ont le même résultat, conserver la première.

## 0–20 min — Présentation et ouverture du projet

Un membre ouvre le projet pendant la présentation de l’enseignant ; les autres l’écoutent. Alternez ensuite les rôles de lecteur, pilote du clavier et rédacteur.

Le dépôt public est [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). Dans IntelliJ IDEA, choisissez **Get from VCS** (ou **Clone Repository** selon la version) sur l’écran d’accueil, puis saisissez l’URL `https://github.com/mikalziane/R304-2026-2027.git` dans le champ **URL**. Choisissez votre dossier personnel sur votre portable ; sur un poste IUT, utilisez votre espace personnel sur `Z:`. Si vous avez déjà récupéré le dépôt en TP1, ne clonez pas à nouveau : ouvrez ce clone (**File > Open** sur son dossier) et mettez-le à jour avec **Git > Update Project** (`Ctrl+T`), ou `git pull` dans le terminal. Le dossier `TD1/src` doit contenir exactement les cinq fichiers listés plus haut ; sinon, appelez l’enseignant.

Dans les deux cas, choisissez ensuite **File > Open**, ouvrez le dossier `TD1` du dépôt cloné et sélectionnez son fichier `pom.xml`. Si IntelliJ vous le propose, cliquez sur **Open as Project**, puis sur **This Window**. Maven télécharge alors les bibliothèques de tests ; une connexion Internet est nécessaire la première fois. Attendez que la barre de progression en bas de la fenêtre disparaisse : l’import est alors terminé. Dans **File > Project Structure > Project**, choisissez un **JDK 21** ou plus récent dans **SDK**. Si aucun JDK 21 n’apparaît, choisissez **Add SDK > Download JDK** dans ce même écran.

Dans `src/test/java/krk/ChoisirPositionTest.java`, utilisez le triangle vert à côté du nom de la classe pour lancer ses tests. **Résultat attendu : deux tests réussis**, c’est-à-dire une barre verte et « Tests passed: 2 » (« Tests réussis : 2 » si IntelliJ est en français) dans la fenêtre **Run**. Il n’y a pas de méthode `main` à lancer : les tests exécutent le programme.

**Terminal, uniquement en secours.** Un terminal est la fenêtre dans laquelle on tape des commandes. Dans IntelliJ IDEA, ouvrez **View > Tool Windows > Terminal** (`Alt+F12` sous Windows). L’invite affiche le dossier courant. Il faut être dans le dossier `TD1`, celui qui contient `pom.xml` et `mvnw.cmd`. Si l’invite se termine par le nom du dépôt, `R304-2026-2027`, tapez `cd TD1` puis **Entrée** ; si elle montre un autre dossier, appelez l’enseignant. Tapez ensuite la ligne suivante puis validez par **Entrée** :

```powershell
.\mvnw.cmd clean test
```

La première utilisation demande Internet pour télécharger les dépendances. Le terminal doit afficher `Tests run: 2, Failures: 0` puis, parmi les dernières lignes, `BUILD SUCCESS`. Si le démarrage bloque, signalez-le et commencez la lecture. Les cinq fichiers se lisent aussi dans un navigateur, à l’adresse `https://github.com/mikalziane/R304-2026-2027/tree/main/TD1/src`, ou dans les extraits fournis par l’enseignant.

## 20–40 min — Lire les données et suivre un choix

Tous les chemins suivants sont relatifs au dossier `TD1`.

1. Ouvrez `src/main/java/krk/domaine/Position.java`. Un `record` Java décrit ici trois données. Son constructeur reçoit ces données dans l’ordre de leur déclaration ; `equals` compare leurs valeurs. Associez chacun des trois paramètres aux pièces des deux échiquiers. Relevez les valeurs des positions A et B.
2. Ouvrez `src/test/java/krk/ChoisirPositionTest.java`. Dans le premier test, repérez les lignes qui préparent les données et l’objet à tester, celle qui lance le choix et celle qui vérifie le résultat. Dans `assertEquals(attendu, obtenu)`, quel est le rôle de chacun des deux arguments ?
3. Ouvrez `src/main/java/krk/application/ChoisirPosition.java`. Suivez la méthode `choisir` pour ce premier test. À chaque `if`, notez la position examinée, le résultat attendu de `estMat`, puis si l’exécution continue ou rencontre un `return`. Utilisez les informations données sur les échiquiers ; vous lirez le code de `estMat` ensuite.
4. Regardez le second test. Quelle variation introduit-il ? Quelle erreur simple ce test pourrait-il détecter, même si le premier réussissait ?

**Trace à conserver :** quelques lignes décrivant le trajet du premier test. Une autre personne que le pilote l’explique au groupe.

## 40–60 min — Trouver qui fournit l’évaluation

Dans le test, la variable `service` désigne l’objet de classe `ChoisirPosition` ; nous l’appellerons le service. Ce service appelle `evaluateur.estMat(...)`. Pour comprendre cet appel, distinguez le **type déclaré** (un nom écrit dans le code) de l’**objet** (une instance créée avec `new`). Une variable dont le type est une interface peut désigner un objet d’une classe qui implémente cette interface.

1. Dans `ChoisirPosition.java`, repérez le champ `evaluateur`, le paramètre du constructeur et l’affectation `this.evaluateur = evaluateur`. Quel type est déclaré ? Le constructeur crée-t-il l’évaluateur ou le reçoit-il ?
2. Lisez entièrement `src/main/java/krk/application/EvaluateurPosition.java`. Que reçoit la méthode ? Que renvoie-t-elle ? Contient-elle le calcul de la réponse ?
3. Lisez entièrement `src/main/java/krk/adaptateur/EvaluationDeuxPositions.java`. À quoi sert `implements` ? Retrouvez les réponses pour A et B. Que fait le programme pour une troisième position ? Ne modifiez pas le code.
4. Revenez au test. Repérez les deux `new` sur la ligne qui construit le service. Quel objet est donné à quel autre objet ? Lors du premier appel à `estMat`, dans quel fichier se trouve le code effectivement exécuté ?

**Indice de lecture volontaire :** l’expression `new X(new Y())` crée d’abord un objet `Y`, puis le passe au constructeur de `X`. `throw new IllegalArgumentException(...)` interrompt l’appel en signalant une entrée refusée.

Faites un point avec l’enseignant avant de passer au schéma. Changez de pilote.

## 60–80 min — Une première lecture de la Clean Architecture

La **Clean Architecture** cherche à séparer les règles et décisions du programme des moyens particuliers utilisés pour les réaliser. Dans ce petit exemple, on découvre seulement une frontière, pas toute l’architecture d’un vrai programme d’échecs.

- Le **domaine** décrit les données et les règles propres au problème.
- Le **cas d’utilisation** réalise une action attendue du programme.
- Un **port** est un contrat par lequel le cas d’utilisation demande quelque chose à l’extérieur ; il peut être écrit sous forme d’interface Java.
- Un **adaptateur** fournit une réalisation de ce contrat.
- L’**assemblage** crée les objets concrets et les relie.

### Compléter un schéma de types

Une flèche de dépendance va de la classe ou interface qui **utilise un type** vers ce **type utilisé**. Une classe qui implémente une interface dépend aussi de cette interface. On étudie ici les noms écrits dans le code, pas l’ordre des appels.

Recopiez les trois noms suivants, puis ajoutez seulement deux flèches : une flèche « utilise comme type » et une flèche « implémente ». Cherchez la direction de chaque flèche dans les déclarations Java.

```text
[ ChoisirPosition ]     [ EvaluateurPosition ]

                       [ EvaluationDeuxPositions ]
```

1. Associez ces trois éléments aux rôles de cas d’utilisation, port et adaptateur. Pour chaque flèche, relevez la déclaration Java qui la justifie.
2. Quel fichier représente ici le domaine ? Quel fichier effectue l’assemblage ?
3. À l’exécution, l’objet service appelle un objet concret. Écrivez son nom de classe. Ce nom apparaît-il dans le fichier du service ? Justifiez en montrant le code.

**La règle des dépendances** concerne les types connus par le code : les détails extérieurs doivent dépendre des contrats intérieurs. Ici, l’intérieur est le dossier `application`, qui contient le contrat ; l’adaptateur, dans `adaptateur`, lui est extérieur. Vérifiez si vos flèches respectent cette règle. Le nom d’un dossier ne suffit pas : les déclarations et les imports donnent les preuves.

Il n’est pas demandé de dessiner un second schéma d’objets ni de représenter tous les imports.

## 80–90 min — Bilan et préparation de la suite

Sur votre feuille, répondez brièvement :

1. On veut remplacer l’évaluateur par une autre classe qui respecte le même contrat. Où créerait-on le nouvel objet ? Le service aurait-il besoin d’être modifié ? Justifiez avec votre schéma et une ligne de code.
2. Que prouvent les deux tests sur les positions A et B ? Pourquoi ne prouvent-ils pas que le programme sait jouer aux échecs ?

Gardez vos réponses et le schéma pour la prochaine séance. Chacun doit pouvoir expliquer une flèche, sans soutenance individuelle. Au TD suivant, nous utiliserons cette séparation pour changer l’évaluation dans un test.

**Si le temps manque :** terminez le trajet d’un appel et les deux flèches avec l’enseignant. Les questions de bilan peuvent être discutées collectivement ; aucun travail supplémentaire à domicile n’est imposé.
