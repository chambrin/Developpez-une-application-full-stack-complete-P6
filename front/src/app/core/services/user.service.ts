import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { tap, map, catchError } from 'rxjs/operators';
import { User } from '../interfaces/user.interface';
import { UpdateUserRequest } from '../interfaces/updateUserRequest.interface';
import { AuthSuccess } from '../interfaces/authSuccess.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userApiPath = 'api/user';
  private readonly USER_CACHE_KEY = 'cached_user_profile';
  
  constructor(private http: HttpClient) {}
  
  getCurrentUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.userApiPath}/me`).pipe(
      tap(user => this.cacheUserLocally(user)),
      catchError(this.fallbackToCache.bind(this))
    );
  }
  
  updateUserProfile(profileData: UpdateUserRequest): Observable<AuthSuccess> {
    return this.http.put(`${this.userApiPath}/update`, profileData).pipe(
      map(this.transformUpdateResponse.bind(this)),
      tap(() => this.clearUserCache()),
      catchError(this.handleProfileUpdateError.bind(this))
    );
  }
  
  private cacheUserLocally(user: User): void {
    sessionStorage.setItem(this.USER_CACHE_KEY, JSON.stringify(user));
  }
  
  private fallbackToCache(): Observable<User> {
    const cachedUser = sessionStorage.getItem(this.USER_CACHE_KEY);
    return cachedUser ? of(JSON.parse(cachedUser)) : throwError('Aucun profil disponible');
  }
  
  private transformUpdateResponse(response: any): AuthSuccess {
    return { ...response, timestamp: Date.now() } as AuthSuccess;
  }
  
  private clearUserCache(): void {
    sessionStorage.removeItem(this.USER_CACHE_KEY);
  }
  
  private handleProfileUpdateError(error: any): Observable<AuthSuccess> {
    console.error('Erreur lors de la mise à jour du profil:', error);
    return throwError('Échec de la mise à jour du profil');
  }
}
