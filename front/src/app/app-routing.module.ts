import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { PostsComponent } from './pages/posts/posts.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { LoginComponent } from './pages/login/login.component';
import { AccountComponent } from './pages/account/account.component';
import { RegisterComponent } from './pages/register/register.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AlreadyLogged } from './core/guards/alreadyLogged.guard';

const routes: Routes = [
  {
    path: 'article-detail/:id',
    component: ArticleDetailComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'posts',
    component: PostsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'create-post',
    component: CreatePostComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '',
    component: HomeComponent,
    canActivate: [AlreadyLogged]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [AlreadyLogged]
  },
  {
    path: 'account',
    component: AccountComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [AlreadyLogged]
  },
  {
    path: 'articles',
    redirectTo: '/posts',
    pathMatch: 'full'
  },
  {
    path: 'topics',
    redirectTo: '/themes',
    pathMatch: 'full'
  },
  {
    path: 'profile',
    redirectTo: '/account',
    pathMatch: 'full'
  },
  {
    path: 'create-article',
    redirectTo: '/create-post',
    pathMatch: 'full'
  },
  {
    path: 'themes',
    component: ThemesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
