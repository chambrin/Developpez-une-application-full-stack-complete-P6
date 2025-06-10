import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Article } from 'src/app/core/interfaces/article.interface';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent {
  @Input() articleData: Article;

  constructor(private navigationService: Router) {}

  onCardClick(): void {
    if (this.articleData && this.articleData.id) {
      this.navigationService.navigate(['/article-detail', this.articleData.id]);
    } else {
      console.warn('Article identifier is not available');
    }
  }
}