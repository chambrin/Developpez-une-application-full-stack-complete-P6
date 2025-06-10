import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'app/core/services/user.service';
import { SubscriptionService } from 'app/core/services/subscription.service';
import { catchError, firstValueFrom, Observable, of, Subject, takeUntil } from 'rxjs';
import { User } from '../../core/interfaces/user.interface';
import { Topic } from 'app/core/interfaces/topic.interface';
import { AuthService } from 'app/core/services/auth.service';
import { SessionService } from 'app/core/services/session.service';
import { UpdateUserRequest } from 'app/core/interfaces/updateUserRequest.interface';
import { AuthSuccess } from 'app/core/interfaces/authSuccess.interface';
import { MatSnackBar } from '@angular/material/snack-bar';
import { passwordValidator } from 'app/core/validators/passwordValidator.validator';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit, OnDestroy {
  accountForm: FormGroup;
  public onError = false;
  public onSuccess = false;
  subscriptions$: Observable<Topic[]>;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private subscriptionService: SubscriptionService,
    private sessionService: SessionService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.accountForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [passwordValidator(false)]]
    });
  }

  public ngOnInit(): void {
    this.subscriptions$ = this.subscriptionService.getUserSubscriptions().pipe(
      catchError(error => {
        console.error('Erreur lors du chargement des thèmes', error);
        return of([]);
      }),
      takeUntil(this.destroy$)
    );

    this.userService.getCurrentUserProfile().pipe(
      takeUntil(this.destroy$)
    ).subscribe(
      (user: User) => this.accountForm.patchValue({
        username: user.username,
        email: user.email
      })
    )
  }

  async onSave(): Promise<void> {
    if (this.accountForm.valid) {
      try {
        const updateRequest = this.accountForm.value as UpdateUserRequest;

        const response: AuthSuccess = await firstValueFrom(this.userService.updateUserProfile(updateRequest));

        if (response.token) {
          localStorage.setItem('token', response.token);
          this.onSuccess = true;
  
          const user: User = await firstValueFrom(this.userService.getCurrentUserProfile());
  
          this.sessionService.logIn(user, response.token);
          this.sessionService['checkInitialLoginState']();
          this.snackBar.open('Les informations ont été modifiées avec succès !', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
        }
      } catch (error) {
        this.onError = true;
        console.error('Erreur lors de la mise à jour du profil', error);
      }
    }
  }

  onLogout(): void {
    this.sessionService.logOut();
    this.router.navigate(['/login']);
  }

  onUnsubscribe(topicId: number): void {
    this.subscriptionService.unsubscribeFromTopic(topicId).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => {
        this.subscriptions$.subscribe(subscriptions => {
          const filteredSubscriptions = subscriptions.filter(sub => sub.id !== topicId);
          this.subscriptions$ = of(filteredSubscriptions);
        });
      },
      error: (error) => console.error('Erreur lors du désabonnement', error)
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }  
}