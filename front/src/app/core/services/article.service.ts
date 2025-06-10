import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article.interface';
import { sendArticleRequest } from '../interfaces/sendArticleRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = 'api/articles';

  constructor(private http: HttpClient) {}

  getArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}`);
  }

  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`);
  }

  createArticle(article: sendArticleRequest): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}`, article);
  }
}
