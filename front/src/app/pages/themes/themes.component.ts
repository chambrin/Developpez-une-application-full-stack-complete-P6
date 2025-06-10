import { Component, OnInit, OnDestroy } from '@angular/core';
import { TopicService } from '../../core/services/topic.service';
import { SubscriptionService } from '../../core/services/subscription.service';
import { Topic } from '../../core/interfaces/topic.interface';
import { Observable, Subject, takeUntil, combineLatest } from 'rxjs';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit, OnDestroy {
  availableThemes$: Observable<Topic[]>;
  subscriptions$: Observable<Topic[]>;
  isHandset: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private topicService: TopicService,
    private subscriptionService: SubscriptionService,
    private breakpointObserver: BreakpointObserver
  ) {}

  ngOnInit(): void {
    this.subscriptions$ = this.subscriptionService.getUserSubscriptions();
    
    // Filtrer les thèmes disponibles pour exclure ceux auxquels l'utilisateur est déjà abonné
    this.availableThemes$ = combineLatest([
      this.topicService.getAllTopics(),
      this.subscriptions$
    ]).pipe(
      map(([allThemes, subscriptions]) => {
        const subscribedIds = subscriptions.map(sub => sub.id);
        return allThemes.filter(theme => !subscribedIds.includes(theme.id));
      })
    );
    
    this.breakpointObserver.observe([Breakpoints.Handset])
      .pipe(
        map(result => result.matches),
        takeUntil(this.destroy$)
      )
      .subscribe(isHandset => {
        this.isHandset = isHandset;
      });
  }

  onSubscribe(topicId: number): void {
    this.subscriptionService.subscribeToTopic(topicId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => {
        // Recharger les abonnements après un nouvel abonnement
        this.subscriptions$ = this.subscriptionService.getUserSubscriptions();
        // Les thèmes disponibles se mettront à jour automatiquement grâce à combineLatest
      },
      error: (error) => console.error('Erreur lors de l\'abonnement', error)
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}