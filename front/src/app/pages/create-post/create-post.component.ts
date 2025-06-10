import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticleService } from '../../core/services/article.service';
import { TopicService } from '../../core/services/topic.service';
import { Router } from '@angular/router';
import { catchError, lastValueFrom, Observable, of, Subject, takeUntil } from 'rxjs';
import { Topic } from 'app/core/interfaces/topic.interface';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CreatePostComponent implements OnInit, OnDestroy {
  postForm: FormGroup;
  topics$: Observable<Topic[]>;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private topicService: TopicService,
    private router: Router
  ) {
    this.postForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      topicId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.topics$ = this.topicService.getAllTopics().pipe(
      catchError(error => {
        console.error('Erreur lors du chargement des thèmes', error);
        return of([]);
      }),
      takeUntil(this.destroy$)
    );
  }

  async onSubmit(): Promise<void> {
    if (this.postForm.valid) {
      try {
        const response = await lastValueFrom(
          this.articleService.createArticle(this.postForm.value).pipe(
            catchError((error) => {
              console.error('Erreur lors de la création du post', error);
              throw error;
            })
          )
        );
        this.router.navigate(['/posts']);
      } catch (error) {
        console.error('Erreur lors de la soumission', error);
      }
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}