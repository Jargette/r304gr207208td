# R304 Qualité de développement

Mikal Ziane — BUT Informatique — Année 2026–2027

Ce dépôt public contient les sujets et sources de départ des TP. Les corrigés sont diffusés séparément sur Moodle. Pour le TP1, clonez ce dépôt et faites vos commits localement : aucun fork ni envoi de vos solutions vers ce dépôt n’est demandé.

## TP1

Lire le [sujet Encapsulation et changements](TP1/SUJET.md). Deux projets : `TP1/pile` et `TP1/refuge`. Leurs classes de démarrage sont respectivement `piles.Main` et `Main`. Les défauts de conception sont intentionnels et constituent le travail du TP.

## Prérequis

Un JDK 21 ou plus récent, Git pour le clonage et un IDE Java au choix. La base a été vérifiée avec le JDK 21 sous Windows ; elle compile au niveau Java 17. JUnit Jupiter 5.11.4 est déclaré dans Maven. Il n’est pas nécessaire de l’ajouter à la main.

Le lanceur Maven est inclus. Sa première utilisation télécharge Maven 3.9.9 et les dépendances : une connexion Internet est nécessaire. Un téléchargement ZIP ne remplace pas ce premier téléchargement de dépendances. L’enseignant prévoit une copie locale si l’accès GitHub pose problème.

## Récupérer les sources

Dans un terminal, dans votre dossier de travail :

```text
git clone https://github.com/mikalziane/R304-2026-2027.git
cd R304-2026-2027
git status
```

Vous pouvez aussi utiliser la fonction de clonage de votre IDE. Ne créez pas de fork et ne refaites pas `git init` dans le dossier cloné.

## Dans IntelliJ IDEA

Ouvrez le `pom.xml` à la racine du dépôt comme projet Maven. Sélectionnez un JDK installé (21 minimum) comme SDK du projet et pour Maven. Laissez l’import Maven télécharger les dépendances. Les projets pile et refuge doivent apparaître séparément.

Lancez la méthode `main` de l’exercice depuis l’éditeur. Pour les tests, ouvrez `PileTest` ou `AnimauxTest` dans `src/test/java` et lancez le test depuis l’éditeur. Les autres IDE peuvent importer la même configuration Maven.

## Depuis un terminal

Sous Windows PowerShell, à la racine du dépôt :

```powershell
java -version
.\mvnw.cmd test
java -cp TP1/pile/target/classes piles.Main
java -cp TP1/refuge/target/classes Main
```

Sous macOS ou Linux :

```sh
./mvnw test
java -cp TP1/pile/target/classes piles.Main
java -cp TP1/refuge/target/classes Main
```

Le lanceur utilise `JAVA_HOME` s’il est défini ; ce chemin doit désigner un JDK. Si Maven n’utilise pas le bon Java, contrôlez `JAVA_HOME` et le JDK sélectionné dans l’IDE.

Au départ, **deux tests doivent réussir**, un par exercice. Cela vérifie le démarrage et quelques comportements seulement. Cela ne signifie pas que les défauts de conception ont été corrigés. Le sujet vous demande d’enrichir les tests. Lors du déplacement de Chien et Chat, pensez aux imports du test.

Pour ne tester que la pile : `.\mvnw.cmd -pl TP1/pile test` (ou `./mvnw -pl TP1/pile test`). Même principe avec `TP1/refuge`.

## Copie ZIP en secours

Si vous avez cloné le dépôt, ne lancez pas `git init`. Si l’enseignant vous fournit le ZIP étudiant, extrayez-le entièrement dans un dossier personnel, puis ouvrez un terminal dans le dossier contenant le `pom.xml` principal. Ce dossier contient aussi `TP1` et `mvnw.cmd`. Créez une seule fois votre historique :

```text
git init
git add .
git commit -m "Base de départ TP1"
```

Si Git demande une identité, utilisez vos propres valeurs (les textes entre guillemets ci-dessous sont à remplacer), puis relancez le commit :

```text
git config user.name "Votre prénom et nom"
git config user.email "Votre adresse personnelle"
```

Ces réglages concernent ce dépôt local. Le ZIP fournit les sources, pas les dépendances Maven ; le premier lancement peut encore nécessiter Internet. Aucun push n’est demandé.

## Conserver une étape de travail

Après une modification cohérente, examinez le diff avant le commit :

```text
git status
git diff
git add TP1/pile/src
git commit -m "Encapsuler la pile et vérifier son contrat"
git log --oneline -3
```

Adaptez le chemin et le message au travail réellement effectué. Si Git demande une identité, configurez votre propre nom et adresse dans ce dépôt avec `git config user.name` et `git config user.email`, suivis de vos valeurs. Ne recopiez pas l’identité de quelqu’un d’autre.

Les commits sont locaux et ne constituent pas une sauvegarde en ligne. Avant de quitter un poste de salle, conservez votre dossier de travail complet, y compris son dossier caché `.git`, sur votre support personnel. Il n’y a pas de `git push` à faire pour ce TP.

## En cas de blocage de démarrage

Signalez-le rapidement. Avec la copie locale, vous pouvez poursuivre la lecture et l’exécution dans l’IDE si les dépendances sont disponibles. Sinon, commencez l’analyse des accès et des changements avec les mêmes sources pendant que l’enseignant règle le problème. Ne consacrez pas la séance à installer des outils.

## Règle d’usage de l’IA

Pas d’IA pendant les TP et TD. Hors séance : explications, critiques et idées, pas de génération de code. Une aide exceptionnelle au déblocage doit être signalée à l’enseignant ; elle n’est pas une autorisation générale.

## TD1 — FI

Lire le [sujet du TD1](TD1/SUJET.md). Le moteur fourni sert à étudier la frontière entre le choix d’un coup et sa source d’évaluation. JDK 21 minimum requis.

Depuis la racine du dépôt, entrez dans `TD1`, puis lancez `.\mvnw.cmd test` sous Windows PowerShell ou `sh ./mvnw test` sous macOS/Linux. La base contient 33 tests. Ce projet Maven est autonome : les commandes lancées à la racine pour le TP1 ne lancent pas les tests du TD1.
