import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Article } from 'app/core/interfaces/article.interface';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {
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