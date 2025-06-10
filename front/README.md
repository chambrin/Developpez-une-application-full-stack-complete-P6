# Interface Utilisateur MDD

Cette application frontend a été développée avec [Angular CLI](https://github.com/angular/angular-cli) dans sa version 14.2.1, constituant la couche présentation de la plateforme communautaire MDD.

## Environnement de Développement

### Lancement du serveur local
Initialisez le serveur de développement avec la commande suivante :
```bash
ng serve
```

L'interface sera disponible à l'adresse `http://localhost:4200/`. Le système intègre un mécanisme de rechargement automatique qui reflète instantanément toute modification apportée aux fichiers sources.

## Génération d'Éléments Structurels

### Création de composants
Pour générer un nouveau composant Angular :
```bash
ng generate component nom-du-composant
```

### Autres éléments architecturaux
L'outil CLI permet également de créer diverses structures :
```bash
ng generate directive|pipe|service|class|guard|interface|enum|module
```

Chaque générateur produit les fichiers nécessaires avec la structure et les imports appropriés.

## Processus de Construction

### Compilation de production
Exécutez la commande de build pour compiler l'application :
```bash
ng build
```

Les artefacts de compilation seront placés dans le répertoire `dist/`, optimisés pour le déploiement en environnement de production.

### Options de build avancées
- `ng build --prod` : Build optimisé avec minification
- `ng build --watch` : Compilation continue lors des modifications

## Validation et Tests

### Tests unitaires
Lancez la suite de tests unitaires via [Karma](https://karma-runner.github.io) :
```bash
ng test
```

Cette commande démarre le runner de tests en mode surveillance, exécutant automatiquement les tests lors des changements de code.

### Tests d'intégration
Pour les tests end-to-end, utilisez :
```bash
ng e2e
```

> **Note** : Cette fonctionnalité nécessite l'installation préalable d'un package de test e2e compatible (Cypress, Protractor, etc.).

## Ressources et Documentation

### Support technique
Pour obtenir de l'aide sur l'utilisation d'Angular CLI :
```bash
ng help
```

### Documentation officielle
Consultez la [documentation complète d'Angular CLI](https://angular.io/cli) pour un aperçu détaillé des commandes et options disponibles.

## Architecture du Projet

```
📁 src/
  📁 app/           → Composants et services principaux
  📁 assets/        → Ressources statiques (images, fonts)
  📁 environments/  → Configuration par environnement
📄 angular.json     → Configuration du workspace
📄 package.json     → Dépendances et scripts npm
📄 tsconfig.json    → Configuration TypeScript
```

## Scripts Disponibles

| Commande | Description |
|----------|-------------|
| `npm start` | Démarre le serveur de développement |
| `npm run build` | Compile l'application pour la production |
| `npm test` | Exécute les tests unitaires |
| `npm run lint` | Vérifie la qualité du code |