import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Topic } from '../interfaces/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private readonly THEME_ENDPOINTS = {
    available: 'api/topics/not-subscribed',
    complete: 'api/topics/all',
    trending: 'api/topics/trending'
  } as const;
  
  private readonly CACHE_DURATION = 5 * 60 * 1000; // 5 minutes
  private themesCache: { data: Topic[], timestamp: number } | null = null;
  
  constructor(private http: HttpClient) {}
  
  getAvailableTopics(): Observable<Topic[]> {
    if (this.isCacheValid()) {
      return of(this.themesCache!.data);
    }
    
    return this.http.get<Topic[]>(this.THEME_ENDPOINTS.available).pipe(
      tap(themes => this.updateCache(themes))
    );
  }
  
  getAllTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.THEME_ENDPOINTS.complete).pipe(
      map(themes => this.sortThemesByPopularity(themes))
    );
  }
  
  private isCacheValid(): boolean {
    return this.themesCache !== null && 
           (Date.now() - this.themesCache.timestamp) < this.CACHE_DURATION;
  }
  
  private updateCache(themes: Topic[]): void {
    this.themesCache = {
      data: themes,
      timestamp: Date.now()
    };
  }
  
  private sortThemesByPopularity(themes: Topic[]): Topic[] {
    return themes.sort((a, b) => a.title.localeCompare(b.title));
  }
}
