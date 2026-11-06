# R304 Qualité de développement

Mikal Ziane — BUT Informatique — Année 2026–2027

Ce dépôt public contient les sujets et sources de départ des TP. Les corrigés sont diffusés séparément sur Moodle. Clonez ce dépôt une seule fois. Pour chaque séance, ouvrez dans IntelliJ le `pom.xml` du dossier de la séance. Pour vos TP, créez aussi un dépôt privé sur votre compte GitHub : vous y sauvegarderez vos commits par un push. Ne faites pas de fork et ne poussez pas vos solutions vers le dépôt public du cours. Cette configuration personnelle sert aux TP ; elle ne définit pas l’organisation future des dépôts de groupe du projet en TD.

## TP1

Lire le [sujet Encapsulation et changements](TP1/SUJET.md). Les deux exercices sont dans le même projet. Leurs dossiers sont `TP1/pile` et `TP1/refuge`. Leurs classes de démarrage sont respectivement `piles.Main` et `Main`. Les défauts de conception sont intentionnels et constituent le travail du TP.

## Prérequis

Un JDK 21 ou plus récent, Git, IntelliJ IDEA et votre compte GitHub personnel auquel vous pouvez vous connecter. Maven fournit JUnit Jupiter 5.11.4.

Le lanceur Maven est inclus. Sa première utilisation télécharge Maven 3.9.9 et les dépendances : une connexion Internet est nécessaire.

## Récupérer les sources

Sur les postes de l’IUT, créez votre dossier de projet dans votre espace personnel sur **Z:**. Clonez le dépôt dans cet espace. Le disque C: ne doit pas servir à stocker votre projet.

Dans IntelliJ IDEA, choisissez **Get from VCS** (ou **Clone Repository**) et collez `https://github.com/mikalziane/R304-2026-2027.git`. Si vous avez déjà un clone, ouvrez votre copie.

## Dans IntelliJ IDEA

Choisissez **File > Open**, sélectionnez `TP1/pom.xml` dans votre clone puis cliquez sur **Open as Project** si IntelliJ le propose. Ce fichier indique à IntelliJ quels exercices et bibliothèques charger. Dans **File > Project Structure > Project**, choisissez un JDK 21 ou plus récent dans **SDK**.

Lancez la méthode `main` de l’exercice depuis l’éditeur. Pour les tests, ouvrez `PileTest` ou `AnimauxTest` dans `src/test/java` et lancez le test depuis l’éditeur. Les autres IDE peuvent importer la même configuration Maven.

## Créer votre dépôt personnel de TP une seule fois

Le **clone** est votre copie locale du cours, dans votre espace personnel sur Z: à l’IUT ou dans votre dossier de travail sur votre ordinateur. Un **dépôt distant** est une copie hébergée sur GitHub. Vous conserverez deux adresses : `origin` désignera votre dépôt privé, pour sauvegarder votre travail ; `upstream` désignera le dépôt public du cours, pour récupérer les supports suivants. Ces noms sont des repères enregistrés dans Git, pas des dossiers.

Effectuez cette configuration après avoir ouvert `TP1/pom.xml` et lancé les deux tests de départ. Réutilisez le clone déjà créé : ne créez pas un deuxième projet Java ni un deuxième historique Git.

1. Connectez-vous à votre compte sur GitHub. Cliquez sur **+ > New repository**. Comme propriétaire (**Owner**), choisissez votre compte. Nommez le dépôt `R304-travaux`, choisissez **Private**, laissez désactivée l’ajout d’un README et ne choisissez ni fichier `.gitignore` ni licence. Cliquez sur **Create repository**. Il doit être vide car votre clone contient déjà ces fichiers et un historique.
2. Sur la page du dépôt vide, copiez son URL **HTTPS**, de la forme `https://github.com/VOTRE-COMPTE/R304-travaux.git`. Utilisez votre véritable nom de compte, pas le texte `VOTRE-COMPTE`.
3. Dans IntelliJ, ouvrez **Git > Manage Remotes**. Si plusieurs dépôts apparaissent, sélectionnez le dépôt parent `R304-2026-2027`, pas un nouveau dépôt dans `TP1`.
4. Sélectionnez `origin`, cliquez sur **Edit** (crayon) et renommez cette adresse en `upstream`, en conservant l’URL du cours. Validez. Si votre version ne présente pas de crayon, ouvrez la fenêtre **Git**, onglet **Log**, puis faites un clic droit sur une branche distante sous `origin` et choisissez **Edit Remote**.
5. Dans **Git > Manage Remotes**, cliquez sur **+** pour ajouter une nouvelle adresse : nom `origin`, URL HTTPS de **votre dépôt privé** copiée à l’étape 2. Validez. Vérifiez que `origin` pointe vers votre compte et `upstream` vers `mikalziane`.
6. Choisissez **Git > Push**. Après le renommage, la destination proposée peut encore être `upstream/main`. Cliquez sur cette destination et choisissez **origin**, branche **main** (ou **Define remote** si proposé, avec le nom origin et votre URL privée). Vérifiez que l’envoi vise votre compte, puis cliquez sur **Push**. Si une connexion est demandée, utilisez votre compte GitHub personnel via la connexion proposée dans le navigateur. Vérifiez cette destination à chaque push.
7. Actualisez la page de votre dépôt sur GitHub : elle doit afficher la mention **Private** et les fichiers du cours. Ce premier push copie l’historique initial ; vos propres modifications viendront ensuite.

Si le push est refusé ou si la destination est le compte `mikalziane`, arrêtez-vous et demandez de l’aide : n’utilisez pas **Force Push**. Si votre dépôt personnel existe déjà, vérifiez les deux adresses et réutilisez-le sans recréer le dépôt ni réinitialiser Git.

**Sur une autre machine**, clonez votre dépôt privé `R304-travaux`, puis ouvrez `TP1/pom.xml`. Les adresses distantes autres que `origin` ne sont pas copiées par le clonage : ajoutez à nouveau `upstream` si vous en avez besoin. Le dossier local peut alors s’appeler `R304-travaux` au lieu de `R304-2026-2027` ; le dossier `TP1` reste le même.

Pour les séances suivantes, les nouveaux sujets seront publiés sur `upstream`. Un push vers `origin` ne les récupère pas. L’enseignant indiquera comment les intégrer ; ne recopiez pas les fichiers par-dessus votre travail et ne fusionnez pas au hasard.

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

## Enregistrer puis sauvegarder une étape de travail

**Exemple.** Vous venez d’ajouter un test dans `PileTest.java`. Il passe et vous souhaitez conserver cette étape avant de poursuivre.

Un **commit** enregistre une version dans votre dépôt local. Un **push** transmet les commits à votre dépôt privé sur GitHub. Une modification qui n’a pas été enregistrée dans un commit n’est pas envoyée par un push.

### Dans IntelliJ IDEA

1. Ouvrez la fenêtre **Commit** (menu **View > Tool Windows > Commit**, ou **Git > Commit** selon votre version).
2. Sélectionnez les fichiers modifiés de `TP1/pile/src`, y compris les nouveaux tests. Ouvrez le diff d’un fichier en double-cliquant dessus : il compare son ancienne version et votre version. Vérifiez les ajouts et suppressions. Ne sélectionnez pas les dossiers `target` ni les fichiers de configuration personnels de l’IDE.
3. Saisissez un message qui décrit votre changement, par exemple `Ajouter un test pour la pile`, puis cliquez sur **Commit**. Si l’IDE demande une identité Git, indiquez votre propre nom et votre adresse associée à GitHub (l’adresse de confidentialité GitHub convient aussi). Cette identité identifie l’auteur des commits ; elle ne remplace pas la connexion à GitHub.
4. Choisissez **Git > Push**, vérifiez la destination **origin/main** sur votre compte personnel, puis cliquez sur **Push**.
5. Sur la page GitHub de votre dépôt privé, ouvrez `TP1/pile/src`. Vérifiez que votre dernier message de commit apparaît et ouvrez un fichier modifié pour vérifier son contenu.

Pour le refuge, répétez ce parcours avec ses fichiers et un message adapté. Un commit décrit une étape réelle, pas seulement la fin de la séance.

### En terminal si IntelliJ IDEA n’est pas disponible

La création du dépôt privé vide sur GitHub reste la même. Dans le dossier du clone, configurez les adresses **une seule fois**, en remplaçant `VOTRE-COMPTE` par votre compte :

```text
git remote rename origin upstream
git remote add origin https://github.com/VOTRE-COMPTE/R304-travaux.git
git remote -v
git push -u origin main
```

`remote rename` conserve le lien vers le cours sous le nom `upstream` ; `remote add` ajoute votre dépôt privé sous le nom `origin`. `remote -v` affiche les adresses à vérifier. Le premier `push -u` envoie la branche et établit son suivi vers `origin/main`. Si les deux adresses sont déjà configurées, vérifiez-les avec `git remote -v` et ne relancez pas les commandes rename/add.

Une fois votre test passé, exécutez ces commandes depuis le dossier **TP1**.

```text
git status
git diff
git add pile/src
git diff --cached
git commit -m "Ajouter un test pour la pile"
git push origin main
git log --oneline -3
```

- `git status` indique les fichiers modifiés, nouveaux et déjà sélectionnés pour le commit.
- `git diff` montre les changements non encore sélectionnés dans les fichiers déjà suivis. Il ne montre pas le contenu des nouveaux fichiers non suivis.
- `git add pile/src` sélectionne les modifications, nouveaux fichiers et suppressions de ce dossier pour le prochain commit. Il ne crée pas de commit et n’envoie rien sur GitHub. Depuis la racine du clone, le chemin serait `TP1/pile/src`.
- `git diff --cached` permet de relire ce qui sera enregistré, nouveaux fichiers sélectionnés compris.
- `git commit` enregistre cette sélection localement ; `git push origin main` transmet les commits à votre dépôt personnel.
- `git log --oneline -3` affiche les trois derniers commits locaux, du plus récent au plus ancien, sur une ligne chacun. Cela ne vérifie pas le succès du push : vérifiez aussi la page GitHub.

Si Git demande une identité, configurez-la pour ce dépôt avec `git config user.name "Votre nom"` et `git config user.email "Votre adresse"`, en remplaçant les valeurs par les vôtres, puis relancez le commit.

Avant de quitter la salle, vérifiez sur GitHub que votre dernier commit a été transmis. Gardez aussi votre dossier local sur Z:, y compris `.git` ; sur votre machine personnelle, gardez-le dans votre dossier de travail. Un push ne sauvegarde ni les fichiers ignorés ni les modifications non commitées. Aucune invitation de l’enseignant ni remise évaluée n’est demandée à cette étape.

## Règle d’usage de l’IA

Pas d’IA pendant les TP et TD. Hors séance : explications, critiques et idées, pas de génération de code. Une aide exceptionnelle au déblocage doit être signalée à l’enseignant ; elle n’est pas une autorisation générale.

## TD1 — FI

Lire le [sujet du TD1](TD1/SUJET.md). Le moteur de finale roi et tour blancs contre roi noir (KRK) sert à étudier la frontière entre le choix d’un coup et sa source d’évaluation. JDK 21 minimum requis.

Depuis la racine du dépôt, entrez dans `TD1`, puis lancez `.\mvnw.cmd test` sous Windows PowerShell ou `sh ./mvnw test` sous macOS/Linux. La base contient 33 tests. Ce projet Maven est autonome : les commandes lancées dans `TP1` ne lancent pas les tests du TD1.
