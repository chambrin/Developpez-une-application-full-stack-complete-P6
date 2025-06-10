import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticleService } from '../../core/services/article.service';
import { Article } from '../../core/interfaces/article.interface';
import { Observable, Subject, takeUntil } from 'rxjs';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit, OnDestroy {
  postsStream$: Observable<Article[]>;
  isMobileLayout: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private articleService: ArticleService,
    private breakpointObserver: BreakpointObserver
  ) {}

  ngOnInit(): void {
    this.postsStream$ = this.articleService.getArticles();
    
    this.breakpointObserver.observe([Breakpoints.Handset])
      .pipe(
        map(result => result.matches),
        takeUntil(this.destroy$)
      )
      .subscribe(isHandset => {
        this.isMobileLayout = isHandset;
      });
  }

  applySorting(criteria: string): void {
    this.postsStream$ = this.articleService.getArticles();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}