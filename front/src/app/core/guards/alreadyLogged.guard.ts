// src/app/core/guards/auth.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root'
})
export class AlreadyLogged implements CanActivate {
  constructor(private sessionService: SessionService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    return this.sessionService.isLogged$().pipe(
      take(1),
      map(isLogged => {
        if (!isLogged) {
          return true;
        } else {
          // Redirige vers la page des posts si l'utilisateur est déjà authentifié
          return this.router.createUrlTree(['/posts'], { queryParams: { returnUrl: state.url }});
        }
      })
    );
  }
}