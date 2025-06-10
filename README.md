# Guide d'Installation - Plateforme Communautaire MDD

## Vue d'ensemble du Projet

MDD représente une plateforme sociale moderne construite sur une architecture full-stack robuste. L'écosystème se divise en deux composants principaux :

**Backend API** : Développé avec le framework Spring Boot utilisant Java, il constitue le cœur métier de l'application en gérant la persistance des données et l'exposition des services REST.

**Interface Utilisateur** : Réalisée avec Angular et TypeScript, elle offre une expérience utilisateur fluide pour l'interaction avec les contenus, la gestion des profils et les fonctionnalités collaboratives.

## Environnement Technique Requis

### Composants Serveur
- Kit de développement Java (JDK) 22 minimum
- Gestionnaire de build Maven 3.6+
- Serveur de base de données MySQL avec driver 8.0.33
- Framework Spring Boot 3.2.4 (résolution automatique des dépendances)

### Composants Client  
- Runtime Node.js 16 ou version ultérieure
- Gestionnaire de paquets npm inclus
- Angular CLI 18.2.4 (installation globale recommandée)

## Procédure de Déploiement

### Phase 1 : Préparation de la Persistance

1. **Création de l'espace de données**
   - Provisionnez une nouvelle base nommée `mdd` sur votre instance MySQL

2. **Initialisation du schéma** 
   - Importez la structure depuis `back/src/main/resources/data.sql`
   - Ce script génère automatiquement les entités et popule les données de référence

3. **Configuration des accès**
   - Dupliquez `.env.template` vers `.env` dans `back/src/main/resources/`
   - Personnalisez les paramètres suivants :
     - `DB_URL` : Chaîne de connexion (ex: `jdbc:mysql://localhost:3306/mdd`)
     - `DB_USERNAME` : Identifiant d'authentification MySQL
     - `DB_PASSWORD` : Clé d'authentification correspondante

### Phase 2 : Préparation des Services Backend

Naviguez vers le répertoire `back` et procédez à l'installation :
```bash
mvn clean install
```

### Phase 3 : Préparation de l'Interface Client

Accédez au dossier `front` et installez les modules :
```bash
npm ci
```

## Mise en Service

### Activation du Backend
Depuis le répertoire `back`, initialisez les services :
```bash
mvn spring-boot:run
```
*Services accessibles sur http://localhost:3001*

### Activation du Frontend  
Depuis le répertoire `front`, lancez le serveur de développement :
```bash
npm run start
```
*Interface disponible sur http://localhost:4200*

## Capacités Fonctionnelles

La plateforme MDD propose un ensemble complet de fonctionnalités sociales :

- **Gestion d'identité** : Inscription, authentification et sessions sécurisées
- **Publication de contenu** : Création et diffusion d'articles personnalisés
- **Engagement communautaire** : Système de commentaires et d'interactions
- **Exploration sociale** : Navigation et découverte des profils membres

## Architecture des Répertoires

```
📁 front/           → Sources de l'application Angular
📁 back/            → Code source de l'API Spring Boot  
📄 data.sql         → Script d'initialisation de la base
📄 .env.template    → Modèle de configuration environnementale
```

## Résolution des Incidents

**Échecs de connectivité base de données**
- Validez la syntaxe des paramètres dans `.env`
- Confirmez l'état opérationnel du service MySQL
- Vérifiez l'existence de la base `mdd`

**Conflits de ports réseau**
- Backend (3001) : Modifiez `server.port` dans `application.properties`  
- Frontend (4200) : Ajustez la configuration dans `angular.json`

**Dépendances manquantes**
- Backend : Exécutez `mvn dependency:resolve`
- Frontend : Lancez `npm audit fix` pour résoudre les vulnérabilités