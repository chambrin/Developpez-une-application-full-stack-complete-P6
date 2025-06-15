// IMPORTS ANGULAR - Démonstration des concepts fondamentaux
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticleService } from '../../core/services/article.service';
import { Article } from '../../core/interfaces/article.interface';
import { Observable, Subject, takeUntil } from 'rxjs';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

/**
 * COMPOSANT POSTS - Page d'affichage des articles
 * 
 * ARCHITECTURE ANGULAR DÉMONTRÉE :
 * - Lifecycle Hooks (OnInit, OnDestroy)
 * - Injection de dépendances (Services)
 * - Programmation réactive (RxJS)
 * - Design responsive (BreakpointObserver)
 * - Typage fort (TypeScript)
 * 
 */
@Component({
  selector: 'app-posts',           // Sélecteur pour utiliser le composant
  templateUrl: './posts.component.html',  // Template HTML séparé
  styleUrls: ['./posts.component.scss']   // Styles SCSS séparés
})
export class PostsComponent implements OnInit, OnDestroy {
  
  // PROPRIÉTÉS RÉACTIVES - Démonstration de la programmation réactive
  
  /** 
   * Stream d'articles - Observable pour la gestion asynchrone
   * Avantages :
   * - Gestion automatique des états de chargement
   * - Mise à jour automatique du template
   * - Gestion des erreurs centralisée
   */
  postsStream$: Observable<Article[]>;
  
  /** 
   * État responsive - Adaptation mobile/desktop
   * Utilise BreakpointObserver pour détecter la taille d'écran
   */
  isMobileLayout: boolean = false;
  
  /** 
   * Subject pour la gestion des subscriptions
   * Pattern de nettoyage pour éviter les memory leaks
   * Principe : Unsubscribe automatique à la destruction du composant
   */
  private destroy$ = new Subject<void>();

  /**
   * INJECTION DE DÉPENDANCES - Principe fondamental d'Angular
   * 
   * @param articleService Service pour récupérer les articles (communication API)
   * @param breakpointObserver Service Angular CDK pour le responsive design
   * 
   * Avantages de l'injection :
   * - Découplage des dépendances
   * - Facilite les tests unitaires (mocking)
   * - Gestion centralisée des instances
   */
  constructor(
    private articleService: ArticleService,
    private breakpointObserver: BreakpointObserver
  ) {}

  /**
   * LIFECYCLE HOOK - ngOnInit
   * 
   * Exécuté après l'initialisation du composant
   * Utilisé pour :
   * - Initialiser les données
   * - Configurer les observables
   * - Démarrer les subscriptions
   */
  ngOnInit(): void {
    // CHARGEMENT DES ARTICLES
    // Communication avec le backend via le service
    this.postsStream$ = this.articleService.getArticles();
    
    // GESTION RESPONSIVE
    // Observation des changements de taille d'écran
    this.breakpointObserver.observe([Breakpoints.Handset])
      .pipe(
        // TRANSFORMATION : result.matches → boolean
        map(result => result.matches),
        
        // GESTION MEMORY LEAK : Unsubscribe automatique
        // Quand destroy$ émet, toutes les subscriptions se terminent
        takeUntil(this.destroy$)
      )
      .subscribe(isHandset => {
        // MISE À JOUR DE L'ÉTAT RESPONSIVE
        // Le template s'adapte automatiquement grâce au data binding
        this.isMobileLayout = isHandset;
      });
  }

  /**
   * MÉTHODE DE TRI - Fonctionnalité utilisateur
   * 
   * @param criteria Critère de tri (non implémenté dans cette version)
   * 
   * Note : Actuellement recharge tous les articles
   * Amélioration possible : Tri côté client ou paramètre API
   */
  applySorting(criteria: string): void {
    // RECHARGEMENT DES DONNÉES
    // Nouvelle requête au service pour récupérer les articles
    this.postsStream$ = this.articleService.getArticles();
  }

  /**
   * LIFECYCLE HOOK - ngOnDestroy
   * 
   * Exécuté avant la destruction du composant
   * ESSENTIEL pour éviter les memory leaks
   * 
   * Pattern de nettoyage :
   * 1. Émettre un signal de destruction
   * 2. Compléter le Subject
   * 3. Toutes les subscriptions avec takeUntil(destroy$) se terminent
   */
  ngOnDestroy(): void {
    // SIGNAL DE DESTRUCTION
    // Déclenche l'unsubscribe de tous les observables
    this.destroy$.next();
    
    // NETTOYAGE COMPLET
    // Marque le Subject comme terminé
    this.destroy$.complete();
  }
}