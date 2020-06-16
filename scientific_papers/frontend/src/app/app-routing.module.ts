import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { LogoutComponent } from './logout/logout.component';
import { AddPaperFormComponent } from './add-paper-form/add-paper-form.component';
import { AuthorPapersComponent } from './author-papers/author-papers.component';
import { EditorPageComponent } from './editor-page/editor-page.component';


const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'logout', component: LogoutComponent},
  { path: 'add-paper', component: AddPaperFormComponent},
  { path: 'my-papers', component: AuthorPapersComponent},
  { path: 'submissions-in-process', component: EditorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
