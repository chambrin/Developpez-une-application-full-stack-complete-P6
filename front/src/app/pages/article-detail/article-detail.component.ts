import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../core/services/article.service';
import { Article } from 'app/core/interfaces/article.interface';
import { Comment } from 'app/core/interfaces/comment.interface';
import { BehaviorSubject, catchError, Observable, of, Subject, switchMap, takeUntil, tap } from 'rxjs';
import { CommentService } from 'app/core/services/comment.service';

@Component({
  selector: 'app-post-details',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit, OnDestroy {
  currentPost$: Observable<Article>;
  userMessages$: Observable<Comment[]>;
  private messagesDataSource = new BehaviorSubject<Comment[]>([]);
  userInput: string = '';
  private cleanup$ = new Subject<void>();

  constructor(
    private routeHandler: ActivatedRoute,
    private postService: ArticleService,
    private messageService: CommentService
  ) {}

  ngOnInit() {
    this.currentPost$ = this.routeHandler.params.pipe(
      switchMap(routeParams => {
        const postId = +routeParams['id'];
        return this.postService.getArticleById(postId).pipe(
          catchError(loadError => {
            console.error('Erreur lors du chargement de l\'article', loadError);
            // Return a default Article object with correct string types for dates
            return of({
              id: 0,
              title: 'Article non trouvé',
              content: 'Cet article n\'existe pas ou n\'est plus disponible.',
              author: 'Système',
              topic: 'Inconnu',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString()
            } as Article);
          })
        );
      }),
      takeUntil(this.cleanup$)
    );

    this.loadUserMessages();
  }

  loadUserMessages(): void {
    this.userMessages$ = this.routeHandler.params.pipe(
      switchMap(routeParams => {
        const postId = +routeParams['id'];
        return this.messageService.getCommentsByArticleId(postId).pipe(
          tap(messagesList => this.messagesDataSource.next(messagesList)),
          catchError(loadError => {
            console.error('Erreur lors du chargement des commentaires', loadError);
            return of([]);
          })
        );
      }),
      takeUntil(this.cleanup$)
    );
  }

  publishMessage(): void {
    if (this.userInput.trim()) {
      this.currentPost$.pipe(
        switchMap(currentPost => {
          if (!currentPost) {
            throw new Error('Article non trouvé');
          }
          const messageData = {
            articleId: currentPost.id,
            content: this.userInput,
          };
          return this.messageService.addComment(messageData);
        }),
        tap(publishedMessage => {
          const existingMessages = this.messagesDataSource.value;
          this.messagesDataSource.next([...existingMessages, publishedMessage]);
          this.userInput = '';
          this.loadUserMessages();
        }),
        catchError(publishError => {
          console.error('Erreur lors de l\'ajout du commentaire', publishError);
          return of(null);
        }),
        takeUntil(this.cleanup$)
      ).subscribe();
    }
  }

  ngOnDestroy(): void {
    this.cleanup$.next();
    this.cleanup$.complete();
  }
}