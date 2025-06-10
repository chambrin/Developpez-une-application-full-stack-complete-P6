import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Topic } from '../interfaces/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiUrl = 'api/topics';
  private subscriptionCache = new Map<number, boolean>();

  constructor(private http: HttpClient) {}

  getUserSubscriptions(): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.apiUrl}/subscribed`).pipe(
      tap(subscriptions => {
        subscriptions.forEach(sub => this.subscriptionCache.set(sub.id, true));
      }),
      catchError((error) => {
        console.error('Erreur lors du chargement des abonnements', error);
        return of([]);
      })
    );
  }

  subscribeToTopic(topicId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/subscribe/${topicId}`, {}).pipe(
      tap(() => this.subscriptionCache.set(topicId, true)),
      map((response: any) => response),
      catchError((error) => {
        console.error('Erreur lors de l\'abonnement', error);
        return of(null);
      })
    );
  }

  unsubscribeFromTopic(topicId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/unsubscribe/${topicId}`, {}).pipe(
      tap(() => this.subscriptionCache.delete(topicId)),
      map((response: any) => response),
      catchError((error) => {
        console.error('Erreur lors du désabonnement', error);
        return of(null);
      })
    );
  }
  
  isUserSubscribedToTheme(themeId: number): boolean {
    return this.subscriptionCache.has(themeId);
  }
}
