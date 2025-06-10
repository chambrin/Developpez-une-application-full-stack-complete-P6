import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SessionService } from 'app/core/services/session.service';
import { Observable, Subject, takeUntil, tap } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  public isHandset: boolean = false;
  public isMenuOpen: boolean = false;
  public isLogged$: Observable<boolean>;
  private destroy$ = new Subject<void>();

  constructor(
    private sessionService: SessionService,
    private breakpointObserver: BreakpointObserver
  ) { }
  
  ngOnInit(): void {
    this.isLogged$ = this.sessionService.isLogged$().pipe(
      tap(isLogged => console.log('Is logged (from session service):', isLogged))
    );

    this.breakpointObserver.observe(['(max-width: 900px)'])
    .pipe(takeUntil(this.destroy$))
    .subscribe(result => {
      this.isHandset = result.matches;
    });
  }

  openMenu(): void {
    this.isMenuOpen = true;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

}
