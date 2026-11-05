# R304 TP1 Encapsulation et changements

Mikal Ziane — BUT Informatique — 2026–2027

Durée : 3 heures. Public : FI et FA. Cette séance précède le premier cours.

Dans ce TP, vous allez modifier deux petits programmes Java pour faciliter des changements précis. À la fin, vous devrez pouvoir expliquer quelle partie du programme a changé, quels clients ont été protégés et comment vous avez vérifié que le comportement attendu est conservé.

Les exercices utilisent les sources du TP1 « pile » et « refuge ». Aucune préparation avant la séance n’est nécessaire. Les sources sont dans le dépôt [mikalziane/R304-2026-2027](https://github.com/mikalziane/R304-2026-2027). L’IA n’est pas utilisée pendant le TP.

## Démarrage 15 minutes

Sur un poste de l’IUT, créez votre dossier de projet dans votre espace personnel sur **Z:**. Choisissez ce dossier pour cloner le dépôt ou extraire le ZIP. Le disque C: ne doit pas servir à stocker votre projet.

Dans **IntelliJ IDEA**, choisissez **Get from VCS** (ou **Clone Repository**) et collez `https://github.com/mikalziane/R304-2026-2027.git`. Si vous avez déjà cloné le dépôt, ouvrez votre copie. Si vous utilisez le ZIP de secours, extrayez-le puis ouvrez le dossier qui contient le `pom.xml`.

Importez le projet Maven et sélectionnez un JDK 21 ou plus récent. Ouvrez `PileTest` et `AnimauxTest`, puis lancez chaque test avec le triangle vert dans l’éditeur. Les deux tests doivent réussir. Le premier import Maven peut nécessiter Internet.

**Si IntelliJ IDEA n’est pas disponible**, ouvrez un terminal dans votre dossier de travail et utilisez les commandes suivantes. Si vous avez déjà les sources, ouvrez un terminal à leur racine et passez aux tests.

```text
git clone https://github.com/mikalziane/R304-2026-2027.git
cd R304-2026-2027
```

À la racine du projet, lancez `.\mvnw.cmd test` sous PowerShell ou `sh ./mvnw test` sous macOS/Linux. Après les tests, vous pouvez lancer la pile avec `java -cp TP1/pile/target/classes piles.Main`.

Les deux exercices se trouvent dans `TP1/pile` et `TP1/refuge`. Dans IntelliJ IDEA, ouvrez `piles.Main` et cliquez sur le triangle vert à côté de `main`. Repérez `Pile` et son client `Main` : un client est ici une classe qui utilise les services d’une autre classe.

Dans IntelliJ IDEA, utilisez la fenêtre **Commit** pour examiner vos modifications et enregistrer des étapes cohérentes. Avec le ZIP, activez d’abord Git dans **VCS > Enable Version Control Integration**. Un clone est déjà un dépôt Git. Si IntelliJ IDEA n’est pas disponible, utilisez `git status`, `git add .` et `git commit` depuis la racine du projet. Avec un ZIP, lancez d’abord `git init`. Vos commits restent locaux. Si la récupération bloque, demandez la copie locale prévue par l’enseignant.

Pour les tests, l’enseignant montre comment exécuter une méthode JUnit Jupiter. Un test prépare une situation, effectue une action et compare le résultat obtenu à un résultat attendu déterminé à l’avance. Dans `assertEquals(attendu, obtenu)`, l’attendu vient en premier.

## Exercice 1 La pile 45 minutes

Une pile suit la règle « dernier entré, premier sorti ». La capacité de la pile reste fixée à sa création. Dans cet exercice, utilisez uniquement des appels valides : ne dépilez pas une pile vide et n’empilez pas dans une pile pleine.

### Observer les dépendances 10 minutes

1. Exécutez `Main` et retrouvez dans le code les opérations qui produisent chaque affichage.
2. Relevez les accès directs de `Main` aux attributs de `Pile`. Quels rôles jouent `t`, `cpt` et `taille` ?
3. Supposons que le tableau soit remplacé par une `LinkedList<Integer>`, en conservant une capacité maximale. Quelles instructions de `Main` faudra-t-il examiner ou modifier ? Justifiez sur deux exemples.

**Rappel utile.** Encapsuler une représentation consiste à la cacher aux clients et à leur proposer des opérations dont le sens ne dépend pas de cette représentation. Remplacer l’accès à `t` par un `getTableau()` qui retourne ce même tableau ne résout pas le problème.

### Protéger la représentation 15 minutes

4. Rendez les attributs de `Pile` privés. Adaptez `Main` en utilisant les méthodes déjà présentes : `empiler`, `depiler`, `getSommet`, `estVide`, `estPleine`. Gardez le nom de classe `Pile`.
5. Vérifiez que les résultats affichés sont conservés. Expliquez pourquoi il n’est pas nécessaire d’exposer `cpt` au client.

### Vérifier le contrat 15 minutes

Le contrat précise le comportement visible : `getSommet()` consulte sans retirer, `depiler()` retire et retourne le dernier élément empilé, et la capacité maximale reste respectée.

6. Ouvrez le fichier fourni `TP1/pile/src/test/java/piles/PileTest.java` (chemin depuis la racine du dépôt). Exécutez ce premier test :

```java
package piles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PileTest {
    @Test
    void consulterNeRetirePas() {
        Pile p = new Pile(2);
        p.empiler(5);
        assertEquals(5, p.getSommet());
        assertEquals(5, p.getSommet());
        assertFalse(p.estVide());
    }
}
```

Le test fourni doit déjà réussir sur la version initiale. Il vérifie un comportement, pas l’encapsulation : vous devez aussi vérifier les accès dans le code.

7. Écrivez un second test : pile de capacité 2 initialement vide ; empilement de 5 puis 7 ; pile pleine ; dépilement de 7 puis 5 ; pile à nouveau vide. Vérifiez les valeurs et les états intermédiaires. Expliquez le défaut que détecterait l’ordre attendu 7 puis 5.

### Bilan 5 minutes

8. Après encapsulation, quels fichiers devraient changer pour passer du tableau à la liste ? Les tests devraient-ils changer ? Sous quelle condition ? Enregistrez votre modification dans Git si disponible.

**Prolongement si vous avez terminé.** Remplacez effectivement le tableau par une liste en conservant la capacité maximale, le nom `Pile`, les signatures et le comportement des méthodes. Réexécutez les mêmes tests et le même client sans les modifier.

## Exercice 2 Le refuge 110 minutes

Le refuge accueille actuellement au plus 5 chiens et 10 chats. Le programme demande à l’utilisateur quel animal ajouter, puis affiche des messages sur les cris et la nourriture. Ces messages sont des données fictives de l’exercice.

Trois changements sont envisagés : modifier les capacités, ajouter une espèce, remplacer l’interface console. Nous souhaitons que ces changements imposent aussi peu de modifications que possible à la gestion du refuge. L’interface console peut encore connaître les espèces qu’elle propose et construire les animaux : nous ne cherchons pas à rendre tout le programme indépendant de tout.

### Comprendre et prévoir 15 minutes

1. Exécutez le programme avec un chien et un chat. Repérez dans `Refuge` les lectures au clavier, les affichages, le stockage, les capacités et les comportements particuliers aux espèces.
2. Pour chacun des trois changements, relevez deux endroits à examiner. Ne comptez pas seulement les lignes : expliquez pourquoi elles dépendent du choix qui change.

### Séparer la console 30 minutes

**Rappel utile.** Un paquetage regroupe des classes. Le placer « sous » un autre dans son nom ne lui donne pas d’accès privilégié. Une dépendance de code existe notamment lorsqu’une classe nomme une autre classe ou appelle ses méthodes.

3. Créez `refuge.ihm` et placez-y `Main`. Déplacez dans cette classe `session`, les questions au clavier et les affichages. Toutes les utilisations de `Scanner`, `System.in` et `System.out` doivent sortir de `Refuge`.
4. Donnez à `Refuge` des méthodes appelables sans dialogue au clavier : vérifier une place disponible, ajouter un animal et obtenir les résultats à afficher. Procédez par petites étapes en recompilant. Des méthodes provisoires propres aux chiens et aux chats sont acceptées à cette étape.
**Aide par étapes si les déplacements ne compilent pas.** Les tableaux et compteurs restent privés dans `Refuge` : ne les rendez pas publics pour résoudre les erreurs.

- Déplacez d’abord seulement `Main` dans le paquetage `refuge.ihm` et importez `refuge.Refuge`. Le nom de la classe à lancer devient `refuge.ihm.Main`.
- Dans `Main`, créez un objet `Refuge`. Déplacez ensuite la boucle de dialogue dans une méthode `session(Refuge refuge)` de `Main` ; les saisies restent dans cette méthode.
- Lorsqu’une ligne déplacée utilise un tableau ou un compteur privé, remplacez cet accès par une demande à l’objet `refuge`. Vous pouvez d’abord créer dans `Refuge` les méthodes publiques `boolean ajouterChien(Chien chien)` et `boolean ajouterChat(Chat chat)` : elles vérifient le quota, ajoutent si possible et indiquent si l’ajout a réussi. `Main` construit l’animal et affiche le résultat.
- Pour les cris et la nourriture, faites retourner une `String` par `lesAnimauxCrient()` et `nourrirLesAnimaux()`. La boucle qui prépare ces textes reste dans `Refuge`. Construisez une chaîne contenant les messages séparés par des retours à la ligne ; `Main` affiche cette chaîne avec `System.out.print`.
- Compilez après chaque petit changement. Ces méthodes propres aux espèces sont provisoires : l’étape suivante les remplacera par `ajouter(IAnimal animal)`.

5. Exécutez à nouveau le même scénario. Qu’aura-t-on à remplacer pour utiliser une autre interface utilisateur ?

Pour limiter le travail, les méthodes de cris et de nourriture peuvent encore retourner des chaînes prêtes à afficher. Cette simplification conserve une dépendance au format des messages ; elle ne rend pas le refuge indépendant de toute présentation.

### Traiter les espèces par un même contrat 40 minutes

**Rappel utile.** Une interface Java décrit des services communs. Une variable de type `IAnimal` peut désigner un `Chien` ou un `Chat` qui implémente ce contrat. L’appel d’une méthode exécute le comportement de l’objet concerné : c’est le polymorphisme.

6. Créez dans `refuge` l’interface `IAnimal` avec quatre méthodes retournant une chaîne : `getNom()`, `getEspece()`, `getCri()` et `getNourriture()`.
7. Placez `Chien` et `Chat` dans `refuge.animaux` et faites-leur implémenter cette interface. Par exemple, un chien retourne `"chien"` pour son espèce et `"aboie"` pour son cri. Adaptez les imports dans le test fourni `AnimauxTest` après le déplacement des classes. Une classe abstraite commune est facultative : elle peut attendre.
8. Remplacez les deux tableaux et leurs compteurs par une collection de `IAnimal`. Utilisez les méthodes de l’interface pour traiter tous les animaux par la même boucle. `Refuge` ne doit plus nommer `Chien` ou `Chat`, ni tester ces types avec `instanceof`.
9. Faites fournir les capacités au constructeur de `Refuge` sous la forme d’une `Map<String, Integer>`. La configuration `chien = 5`, `chat = 10` est créée dans `Main`. `Refuge` conserve une copie de cette map. Pour une espèce absente de la configuration, la capacité est 0. Une capacité configurée est positive ou nulle. Aide Java : `new HashMap<>(capacites)` copie une map, et `placesMax.getOrDefault(espece, 0)` lit une capacité en utilisant 0 lorsque la clé est absente.
10. Créez dans `Refuge` la méthode `public boolean placeDisponiblePour(String espece)` en comptant les animaux déjà présents de cette espèce. La méthode `boolean ajouter(IAnimal animal)` retourne `false` sans modifier la collection si aucune place n’est disponible ; sinon elle ajoute l’animal et retourne `true`. L’IHM affiche le résultat. Le contrôle doit donc rester effectif même sans console.

**Aide collections.** Une liste contient les animaux présents ; une map associe à chaque nom d’espèce sa capacité. Dans `Main`, créez la configuration avant le refuge :

```java
Map<String, Integer> capacites = new HashMap<>();
capacites.put("chien", 5);
capacites.put("chat", 10);
Refuge refuge = new Refuge(capacites);
```

Importez `java.util.Map` et `java.util.HashMap`. Les valeurs 5 et 10 appartiennent à la configuration, pas au code interne de `Refuge`.

Les noms d’espèces sont des identifiants convenus, identiques dans la configuration et dans `getEspece()` ; ils restent constants pour un animal donné. Il n’est pas demandé de charger un fichier de configuration ni de traiter des valeurs nulles.

### Vérifier le changement 15 minutes

11. Sans appeler `Main`, testez avec JUnit un refuge autorisant un seul chien : premier ajout accepté, second refusé, plus de place disponible pour cette espèce. Aucun clavier ne doit être requis.
Créez le fichier `TP1/refuge/src/test/java/refuge/RefugeTest.java`. Vous pouvez partir de ce squelette ; les trois assertions sont à écrire :

```java
package refuge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import refuge.animaux.Chien;

class RefugeTest {
    @Test void refuseUnSecondChien() {
        Refuge refuge = new Refuge(Map.of("chien", 1));
        // Vérifier que ajouter(new Chien("Rex")) renvoie true.
        // Vérifier que ajouter(new Chien("Milo")) renvoie false.
        // Vérifier que placeDisponiblePour("chien") renvoie false.
    }
}
```

`Map.of("chien", 1)` crée ici une configuration avec une seule association. Utilisez `assertTrue(...)` pour une condition attendue vraie et `assertFalse(...)` pour une condition attendue fausse. Un test sans assertion ne vérifie pas le résultat attendu.

12. Indiquez où ajouter un lapin : nouvelle classe, capacité et choix dans la console. Quelle modification reste nécessaire dans `Refuge` ? Vérifiez votre réponse dans le code. L’implémentation de `Lapin` est un prolongement si le temps le permet.

### Bilan 10 minutes

13. Dessinez les trois paquetages et les classes/interfaces principales. Tracez une flèche de A vers B quand A utilise B, en distinguant le lien d’implémentation d’une interface. Situez les créations `new Chien(...)` et `new Chat(...)`.
14. En quelques phrases, expliquez ce que vous avez caché, de quel client, et pour quels changements. Enregistrez une étape cohérente de votre travail.

**Si vous manquez de temps.** Terminez une étape qui compile et notez précisément les dépendances restantes. Priorité à la séparation de la console, puis au contrat commun des animaux. Ne supprimez pas les limites d’accueil pour faire passer le programme. L’enseignant organise la synthèse à partir des étapes atteintes.

## Synthèse de séance 10 minutes

Pour la pile puis le refuge, donnez un exemple de changement désormais localisé et un exemple de changement qui peut encore toucher le client. Expliquez pourquoi une suite de tests réussie ne suffit pas à prouver que les dépendances interdites ont disparu.

Conservez le code, les tests et vos réponses courtes avec les fichiers de travail. Les commits restent locaux ; les modalités d’une éventuelle remise seront indiquées par l’enseignant. Les corrigés seront diffusés sur Moodle. Aucun rapport séparé n’est demandé.
