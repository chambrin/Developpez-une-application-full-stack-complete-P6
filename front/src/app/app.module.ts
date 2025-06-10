import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SubscribedTopicComponent } from './components/subscribedTopic/subscribedTopic.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { PostsComponent } from './pages/posts/posts.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { AccountComponent } from './pages/account/account.component';
import { HeaderComponent } from './components/header/header.component';
import * as fr from '@angular/common/locales/fr';
import { registerLocaleData } from '@angular/common';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { HomeComponent } from './pages/home/home.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ArticleService } from './core/services/article.service';
import { MatOption } from '@angular/material/core';
import { HttpClientModule, HTTP_INTERCEPTORS, provideHttpClient, withInterceptors } from '@angular/common/http';
import { baseUrlInterceptor } from './core/interceptors/base-url.interceptor';
import { jwtInterceptor } from './core/interceptors/jwt.interceptor';
import { UserMessageComponent } from './components/user-message/user-message.component';
import { SubjectComponent } from './components/subject/subject.component';
import { PostComponent } from './components/post/post.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PostComponent,
    SubscribedTopicComponent,
    RegisterComponent,
    LoginComponent,
    PostsComponent,
    ThemesComponent,
    CreatePostComponent,
    ArticleDetailComponent,
    AccountComponent,
    HeaderComponent,
    NotFoundComponent,
    UserMessageComponent,
    SubjectComponent
  ],
  bootstrap: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MatButtonModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatToolbarModule,
    MatMenuModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatIconModule,
    FormsModule,
    MatOption,
    MatSelectModule,
    MatSidenavModule,
    MatListModule,
    MatSnackBarModule
  ],
  providers: [
    provideClientHydration(),
    { provide: LOCALE_ID, useValue: 'fr-FR' },
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptors([baseUrlInterceptor, jwtInterceptor])
    ),
    ArticleService
  ]
})
export class AppModule {
  constructor() {
    registerLocaleData(fr.default);
  }
}