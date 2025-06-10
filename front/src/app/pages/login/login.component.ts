import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'app/core/interfaces/authSuccess.interface';
import { LoginRequest } from 'app/core/interfaces/loginRequest.interface';
import { User } from 'app/core/interfaces/user.interface';
import { AuthService } from 'app/core/services/auth.service';
import { SessionService } from 'app/core/services/session.service';
import { UserService } from 'app/core/services/user.service';
import { tap, switchMap, catchError, throwError, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  public errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private sessionService: SessionService,
    private userService: UserService,
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  async onSubmit(): Promise<void> {
    if (this.loginForm.valid) {
      this.loginForm.disable();
      const loginRequest = this.loginForm.value as LoginRequest;
      let token = '';


      this.authService.login(loginRequest).pipe(
        tap((response: AuthSuccess) => {
          token = response.token;
          localStorage.setItem('token', token);
        }),
        switchMap(() => this.userService.getCurrentUserProfile()), // Récupérer les infos de l'utilisateur connecté
        tap((user: User) => {
          this.sessionService.logIn(user, token); // Mettre à jour la session avec les infos utilisateur et le token
          this.sessionService['checkInitialLoginState']();
          this.router.navigate(['/posts']); // Rediriger vers la page des posts
        }),
        catchError((error) => {
          // Gestion de l'erreur, on affiche le message d'erreur et on réactive le formulaire
          this.errorMessage = error.error || 'Échec de la connexion.';
          this.loginForm.enable(); // Réactiver le formulaire après l'erreur
          return throwError(() => new Error('Login failed'));
        })
      ).subscribe();
    }
  }
  
}