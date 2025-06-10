import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSuccess } from 'app/core/interfaces/authSuccess.interface';
import { RegisterRequest } from 'app/core/interfaces/registerRequest.interface';
import { User } from 'app/core/interfaces/user.interface';
import { AuthService } from 'app/core/services/auth.service';
import { SessionService } from 'app/core/services/session.service';
import { passwordValidator } from 'app/core/validators/passwordValidator.validator';
import { UserService } from 'app/core/services/user.service';
import { tap, switchMap, catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  public errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private sessionService: SessionService,
    private userService: UserService,
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordValidator()]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.registerForm.disable();
      const registerRequest = this.registerForm.value as RegisterRequest;
      let token = '';
      this.authService.register(registerRequest).pipe(
        tap((response: AuthSuccess) => {
          token = response.token;
          localStorage.setItem('token', token)
        }),
        switchMap(() => this.userService.getCurrentUserProfile()),
        tap((user: User) => {
          this.sessionService.logIn(user, token);
          this.router.navigate(['/articles']);
        }),
        catchError((error) => {
          this.errorMessage = error.error || 'Échec de l\'inscription.';
          this.registerForm.enable();
          return throwError(() => new Error('Registration failed'));
        })
      ).subscribe();
    }
  }
}
