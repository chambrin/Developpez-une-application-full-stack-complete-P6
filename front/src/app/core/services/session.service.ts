import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private isLoggedSubject = new BehaviorSubject<boolean>(false);
  private userSubject = new BehaviorSubject<User | undefined>(undefined);

  constructor() {
    this.checkInitialLoginState();
  }

  private checkInitialLoginState(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.isLoggedSubject.next(true);
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        this.userSubject.next(JSON.parse(storedUser));
      }
    } else {
      this.isLoggedSubject.next(false);
      this.userSubject.next(undefined);
    }
  }

  public isLogged$(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public user$(): Observable<User | undefined> {
    return this.userSubject.asObservable();
  }

  public logIn(user: User, token: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    this.userSubject.next(user);
    this.isLoggedSubject.next(true);
  }

  public logOut(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.userSubject.next(undefined);
    this.isLoggedSubject.next(false);
  }

  public getUser(): User | undefined {
    return this.userSubject.getValue();
  }

  public isLoggedIn(): boolean {
    return this.isLoggedSubject.getValue();
  }
}