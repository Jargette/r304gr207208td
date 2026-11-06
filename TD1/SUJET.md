# TD1 — Découvrir une séparation du programme

R304 FI — 90 minutes — groupes de 3 à 5. Cette séance précède le premier cours. Aucune connaissance de Clean Architecture ni des échecs n’est supposée. Un portable par groupe suffit. Pas d’IA pendant le TD.

## Un petit point de départ pour le projet KRK

KRK signifie *King, Rook, King* : un roi et une tour blancs contre un roi noir. Le projet servira à étudier comment organiser et faire évoluer un programme Java.

Aujourd’hui, vous disposez de **cinq fichiers Java, test compris**. Le programme choisit entre **deux positions déjà fournies**. Il ne calcule pas les coups autorisés et ne joue pas une partie. L’évaluateur fourni reconnaît ces deux positions et rejette toute autre position qu’il reçoit.

Vous allez lancer les tests existants, lire tout ce petit programme, suivre les objets et compléter un schéma. **Aucune classe ni aucun test à écrire aujourd’hui.** Conservez une feuille de groupe (papier ou fichier) avec vos réponses et votre schéma. Pas de rapport séparé ni de dépôt à remettre.

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

**Position A : b6, h6, a8.** Le roi noir n’est pas attaqué par la tour. Il n’est donc pas mat.

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

**Position B : b6, h8, a8.** La tour attaque le long de sa ligne et de sa colonne. Le roi blanc contrôle ses cases voisines. Le roi noir est attaqué et ne peut aller ni en a7, ni en b7, ni en b8 : il est **mat**. Le roi ne peut pas se déplacer sur une case contrôlée par l’adversaire.

Au départ, la tour blanche est en h7 et les blancs jouent. Ils déplacent leur tour en h6 pour obtenir A, ou en h8 pour obtenir B. C’est alors aux noirs de jouer. Le programme reçoit directement les positions A et B.

**Règle de choix donnée :** privilégier une position de mat ; si les deux positions ont le même résultat, conserver la première. « Pas mat » ne signifie pas « partie nulle ».

## 0–20 min — Présentation et ouverture du projet

Un membre ouvre le projet pendant la présentation. Les autres suivent la présentation. Alternez ensuite les rôles de lecteur, pilote du clavier et rédacteur.

Le dépôt public est [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). Dans IntelliJ IDEA, choisissez **Get from VCS** (ou **Clone Repository** selon la version) sur l’écran d’accueil, puis saisissez cette URL. Choisissez votre dossier personnel sur votre portable ; sur un poste IUT, utilisez votre espace Z:.

Si vous avez déjà récupéré le dépôt en TP1, conservez ce clone. Ouvrez `TD1/pom.xml` avec **File > Open**, comme projet Maven. Maven prépare la compilation et les bibliothèques de tests. Réglez le SDK du projet et le JDK utilisé par Maven sur **Java 21 minimum**.

Si vous retrouvez un ancien TD1 avec de nombreux fichiers Java, demandez à l’enseignant de vous aider à récupérer la nouvelle version sans écraser vos travaux.

Dans `src/test/java/krk/ChoisirPositionTest.java`, utilisez le triangle vert à côté du nom de la classe pour lancer ses tests. **Résultat attendu : deux tests réussis.** Il n’y a pas de méthode `main` à lancer : les tests exécutent le programme.

**Terminal, uniquement en secours.** Un terminal est la fenêtre dans laquelle on tape des commandes. Dans IntelliJ IDEA, ouvrez **View > Tool Windows > Terminal** (`Alt+F12` sous Windows). Placez-vous dans le dossier `TD1`, celui qui contient `pom.xml` et `mvnw.cmd`. Tapez la ligne suivante puis validez par **Entrée** :

```powershell
.\mvnw.cmd clean test
```

La première utilisation demande Internet pour télécharger les dépendances. Si le démarrage bloque, signalez-le et commencez la lecture sur les fichiers locaux ou les extraits fournis par l’enseignant.

## 20–40 min — Lire les données et suivre un choix

Tous les chemins suivants sont relatifs au dossier `TD1`.

1. Ouvrez `src/main/java/krk/domaine/Position.java`. Un `record` Java décrit ici trois données. Son constructeur reçoit ces données dans l’ordre de leur déclaration ; `equals` compare leurs valeurs. Associez chacun des trois paramètres aux pièces du diagramme. Relevez les valeurs des positions A et B.
2. Ouvrez `src/test/java/krk/ChoisirPositionTest.java`. Dans le premier test, repérez les lignes qui préparent les données, celle qui lance le choix et celle qui vérifie le résultat. Dans `assertEquals(attendu, obtenu)`, quel est le rôle de chacun des deux arguments ?
3. Ouvrez `src/main/java/krk/application/ChoisirPosition.java`. Suivez la méthode `choisir` pour ce premier test. À chaque `if`, notez la position examinée, le résultat attendu de `estMat`, puis si l’exécution continue ou rencontre un `return`. Utilisez les informations données sur les échiquiers ; vous lirez le calcul de `estMat` ensuite.
4. Regardez le second test. Quelle variation introduit-il ? Quelle erreur simple ce test pourrait-il détecter, même si le premier réussissait ?

**Trace à conserver :** quelques lignes décrivant le trajet du premier test. Une autre personne que le pilote l’explique au groupe.

## 40–60 min — Trouver qui fournit l’évaluation

Le service appelle `evaluateur.estMat(...)`. Pour comprendre cet appel, distinguez le **type déclaré** (un nom écrit dans le code) de l’**objet** (une instance créée avec `new`). Une variable dont le type est une interface peut désigner un objet d’une classe qui implémente cette interface.

1. Dans `ChoisirPosition.java`, repérez le champ `evaluateur`, le paramètre du constructeur et l’affectation `this.evaluateur = evaluateur`. Quel type est déclaré ? Le constructeur crée-t-il l’évaluateur ou le reçoit-il ?
2. Lisez entièrement `src/main/java/krk/application/EvaluateurPosition.java`. Que reçoit la méthode ? Que renvoie-t-elle ? Contient-elle le calcul de la réponse ?
3. Lisez entièrement `src/main/java/krk/adaptateur/EvaluationDeuxPositions.java`. À quoi sert `implements` ? Retrouvez les réponses pour A et B. Que fait le programme pour une troisième position ? Ne modifiez pas le code.
4. Revenez au test. Repérez les deux `new` sur la ligne qui construit le service. Quel objet est donné à quel autre objet ? Lors du premier appel à `estMat`, dans quel fichier se trouve le code effectivement exécuté ?

**Indice de lecture volontaire :** l’expression `new X(new Y())` crée d’abord un objet `Y`, puis le passe au constructeur de `X`. `throw new IllegalArgumentException(...)` interrompt l’appel en signalant une entrée refusée.

Faites un point avec l’enseignant avant de passer au schéma. Changez de pilote.

## 60–80 min — Une première lecture de la Clean Architecture

La **Clean Architecture** cherche à séparer les règles et décisions du programme des moyens particuliers utilisés pour les réaliser. Dans ce petit exemple, on découvre seulement une frontière, pas toute une architecture de moteur.

- Le **domaine** décrit les données et les règles propres au problème.
- Le **cas d’utilisation** réalise une action attendue du programme.
- Un **port** est un contrat par lequel le cas d’utilisation demande un service ; il peut être écrit sous forme d’interface Java.
- Un **adaptateur** fournit une réalisation de ce contrat.
- L’**assemblage** crée les objets concrets et les relie.

### Compléter un schéma de types

Une flèche de dépendance va du fichier qui **utilise un type** vers ce **type utilisé**. Une classe qui implémente une interface dépend aussi de cette interface. On étudie ici les noms écrits dans le code, pas l’ordre des appels.

Recopiez les trois noms suivants, puis ajoutez seulement deux flèches : une flèche « utilise comme type » et une flèche « implémente ». Cherchez leur sens dans les déclarations Java.

```text
[ ChoisirPosition ]     [ EvaluateurPosition ]

                       [ EvaluationDeuxPositions ]
```

1. Associez ces trois éléments aux rôles de cas d’utilisation, port et adaptateur. Pour chaque flèche, relevez la déclaration Java qui la justifie.
2. Quel fichier représente ici le domaine ? Quel fichier effectue l’assemblage ?
3. À l’exécution, l’objet service appelle un objet concret. Écrivez son nom de classe. Ce nom apparaît-il dans le fichier du service ? Justifiez en montrant le code.

**La règle des dépendances** concerne les types connus par le code : les détails extérieurs doivent dépendre des contrats intérieurs. Ici, le contrat est placé dans `application` ; l’adaptateur est extérieur à cette application. Vérifiez si vos flèches respectent cette règle. Le nom d’un dossier ne suffit pas : les déclarations et les imports donnent les preuves.

Il n’est pas demandé de dessiner un second schéma d’objets ni de représenter tous les imports.

## 80–90 min — Bilan et préparation de la suite

Sur votre feuille, répondez brièvement :

1. On veut remplacer l’évaluateur par une autre classe qui respecte le même contrat. Où choisirait-on le nouvel objet ? Le service aurait-il besoin d’être modifié ? Justifiez avec votre schéma et une ligne de code.
2. Que prouvent les deux tests sur les positions A et B ? Pourquoi ne prouvent-ils pas que le programme sait jouer aux échecs ?

Gardez vos réponses et le schéma pour la prochaine séance. Chacun doit pouvoir expliquer une flèche, sans soutenance individuelle. Au TD suivant, nous utiliserons cette séparation pour changer l’évaluation dans un test.

**Si le temps manque :** terminez le trajet d’un appel et les deux flèches avec l’enseignant. Les questions de bilan peuvent être discutées collectivement ; aucun travail supplémentaire à domicile n’est imposé.
