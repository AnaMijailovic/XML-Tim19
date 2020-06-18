import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { LogoutComponent } from './logout/logout.component';
import { AddPaperFormComponent } from './add-paper-form/add-paper-form.component';
import { AuthorPapersComponent } from './author-papers/author-papers.component';
import { ScientificPaperEditorComponent } from './scientific-paper-editor/scientific-paper-editor.component';
import { CoverLetterEditorComponent } from './cover-letter-editor/cover-letter-editor.component';
import { EditorPageComponent } from './editor-page/editor-page.component';
import { QuotedPapersComponent } from './quoted-papers/quoted-papers.component';



const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'logout', component: LogoutComponent},
  { path: 'add-paper', component: AddPaperFormComponent},
  { path: 'add-paper-editor', component:  ScientificPaperEditorComponent},
  { path: 'add-cover-letter-editor', component: CoverLetterEditorComponent},
  { path: 'my-papers', component: AuthorPapersComponent},
  { path: 'submissions-in-process', component: EditorPageComponent},
  { path: 'paper-quoted-by/:id', component: QuotedPapersComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
