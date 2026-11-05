# R304 Qualité de développement

Mikal Ziane — BUT Informatique — Année 2026–2027

Ce dépôt public contient les sujets et sources de départ des TP. Les corrigés sont diffusés séparément sur Moodle. Clonez ce dépôt une seule fois. Pour chaque séance, ouvrez dans IntelliJ le `pom.xml` du dossier de la séance. Faites vos commits localement : aucun fork ni envoi de vos solutions vers ce dépôt n’est demandé.

## TP1

Lire le [sujet Encapsulation et changements](TP1/SUJET.md). Les deux exercices sont dans le même projet. Leurs dossiers sont `TP1/pile` et `TP1/refuge`. Leurs classes de démarrage sont respectivement `piles.Main` et `Main`. Les défauts de conception sont intentionnels et constituent le travail du TP.

## Prérequis

Un JDK 21 ou plus récent, Git et IntelliJ IDEA. Maven fournit JUnit Jupiter 5.11.4.

Le lanceur Maven est inclus. Sa première utilisation télécharge Maven 3.9.9 et les dépendances : une connexion Internet est nécessaire.

## Récupérer les sources

Sur les postes de l’IUT, créez votre dossier de projet dans votre espace personnel sur **Z:**. Clonez le dépôt dans cet espace. Le disque C: ne doit pas servir à stocker votre projet.

Dans IntelliJ IDEA, choisissez **Get from VCS** (ou **Clone Repository**) et collez `https://github.com/mikalziane/R304-2026-2027.git`. Si vous avez déjà un clone, ouvrez votre copie.

## Dans IntelliJ IDEA

Choisissez **File > Open**, sélectionnez `TP1/pom.xml` dans votre clone puis cliquez sur **Open as Project** si IntelliJ le propose. Ce fichier indique à IntelliJ quels exercices et bibliothèques charger. Dans **File > Project Structure > Project**, choisissez un JDK 21 ou plus récent dans **SDK**.

Lancez la méthode `main` de l’exercice depuis l’éditeur. Pour les tests, ouvrez `PileTest` ou `AnimauxTest` dans `src/test/java` et lancez le test depuis l’éditeur. Les autres IDE peuvent importer la même configuration Maven.

## Si IntelliJ IDEA n’est pas disponible

Depuis votre dossier de travail, lancez `git clone https://github.com/mikalziane/R304-2026-2027.git`, puis `cd R304-2026-2027/TP1`. Si les sources sont déjà présentes, ouvrez un terminal dans leur dossier `TP1`.

Sous Windows PowerShell, dans le dossier `TP1` :

```powershell
java -version
.\mvnw.cmd test
java -cp pile/target/classes piles.Main
java -cp refuge/target/classes Main
```

Sous macOS ou Linux :

```sh
sh ./mvnw test
java -cp pile/target/classes piles.Main
java -cp refuge/target/classes Main
```

Le lanceur utilise `JAVA_HOME` s’il est défini ; ce chemin doit désigner un JDK. Si Maven n’utilise pas le bon Java, contrôlez `JAVA_HOME` et le JDK sélectionné dans l’IDE.

Au départ, **deux tests doivent réussir**, un par exercice. Cela vérifie le démarrage et quelques comportements seulement. Cela ne signifie pas que les défauts de conception ont été corrigés. Le sujet vous demande d’enrichir les tests. Lors du déplacement de Chien et Chat, pensez aux imports du test.

Pour ne tester que la pile : `.\mvnw.cmd -pl pile test` (ou `sh ./mvnw -pl pile test`). Même principe avec `refuge`.

## Conserver une étape de travail

Dans IntelliJ IDEA, ouvrez la fenêtre **Commit** pour examiner les modifications et enregistrer une étape cohérente. Si IntelliJ IDEA n’est pas disponible, utilisez le terminal depuis le dossier `TP1` :

```text
git status
git diff
git add pile/src
git commit -m "Encapsuler la pile et vérifier son contrat"
git log --oneline -3
```

Adaptez le chemin et le message au travail réellement effectué. En terminal, si Git demande une identité, configurez votre propre nom et adresse dans ce dépôt avec `git config user.name` et `git config user.email`, suivis de vos valeurs. Ne recopiez pas l’identité de quelqu’un d’autre.

Avant de quitter un poste de salle, conservez le dossier `R304-2026-2027` complet, y compris son dossier caché `.git`, dans votre espace personnel sur Z:. Vos commits restent locaux.

## Règle d’usage de l’IA

Pas d’IA pendant les TP et TD. Hors séance : explications, critiques et idées, pas de génération de code. Une aide exceptionnelle au déblocage doit être signalée à l’enseignant ; elle n’est pas une autorisation générale.

## TD1 — FI

Lire le [sujet du TD1](TD1/SUJET.md). Le moteur de finale roi et tour blancs contre roi noir (KRK) sert à étudier la frontière entre le choix d’un coup et sa source d’évaluation. JDK 21 minimum requis.

Depuis la racine du dépôt, entrez dans `TD1`, puis lancez `.\mvnw.cmd test` sous Windows PowerShell ou `sh ./mvnw test` sous macOS/Linux. La base contient 33 tests. Ce projet Maven est autonome : les commandes lancées dans `TP1` ne lancent pas les tests du TD1.
