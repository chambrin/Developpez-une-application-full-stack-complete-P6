import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { catchError, filter, finalize, Observable, of, Subject, Subscription, takeUntil, tap } from 'rxjs';
import { AuthService } from './core/services/auth.service';
import { SessionService } from './core/services/session.service';
import { User } from './core/interfaces/user.interface';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { UserService } from './core/services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  applicationName = 'social-hub-client';
  
  public isMainPage = true;
  public isAuthPage = false;
  public isMobileView: boolean = false;
  public showLoader = false;
  private navigationSubscription: Subscription = new Subscription();
  private componentDestroyed$ = new Subject<void>();
 

  constructor(
    private routerService: Router,
    private userManagementService: UserService,
    private authSessionService: SessionService,
    private responsiveObserver: BreakpointObserver
  ) {}

  ngOnInit(): void {
    this.responsiveObserver.observe(['(max-width: 900px)']).subscribe(result => {
      this.isMobileView = result.matches;
    });

    this.navigationSubscription = this.routerService.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.isMainPage = event.url === '/' || event.url === '';
      this.isAuthPage = event.url === '/login' || event.url === '/register';
    });
  }
  
  public getAuthenticationStatus(): Observable<boolean> {
    return this.authSessionService.isLogged$();
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
    this.componentDestroyed$.next();
    this.componentDestroyed$.complete();
  }

  public performAutoAuthentication(): void {
    const currentRoute = this.routerService.url;
    let authToken = localStorage.getItem('token');
    if (authToken && (currentRoute === '/' || currentRoute.includes('login') || currentRoute.includes('register'))) {
      this.showLoader = true;
      this.userManagementService.getCurrentUserProfile().pipe(
        tap((userData: User) => this.authSessionService.logIn(userData, authToken)),
        catchError((error) => {
          console.error('Échec de la connexion automatique:', error);
          this.authSessionService.logOut();
          return of(null);
        }),
        finalize(() => this.showLoader = false),
        takeUntil(this.componentDestroyed$)
      ).subscribe();
    }
  }
}