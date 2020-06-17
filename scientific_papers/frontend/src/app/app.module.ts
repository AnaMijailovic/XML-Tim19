import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AllowedRoutes } from './_service/allowed-routes.service';
import { AuthenticationService } from './_service/authentication.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './home/home.component';
import { LogoutComponent } from './logout/logout.component';
import { MaterialModule } from './material/material.module';
import { ToastrModule } from 'ngx-toastr';
import { SearchFormComponent } from './search-form/search-form.component';
import { PaperCardComponent } from './paper-card/paper-card.component';
import { AddPaperFormComponent } from './add-paper-form/add-paper-form.component';
import { ScientificPaperService } from './_service/scientific-paper.service';
import { CoverLetterService } from './_service/cover-letter.service';
import { TokenInterceptorService } from './_service/token-interceptor.service';
import { AuthorPapersComponent } from './author-papers/author-papers.component';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { ScientificPaperEditorComponent } from './scientific-paper-editor/scientific-paper-editor.component';
import { CoverLetterEditorComponent } from './cover-letter-editor/cover-letter-editor.component';
import { EditorPageComponent } from './editor-page/editor-page.component';
import { PublishingProcessCardComponent } from './publishing-process-card/publishing-process-card.component';
import { AssignReviewerDialogComponentComponent } from './assign-reviewer-dialog-component/assign-reviewer-dialog-component.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    LogoutComponent,
    SearchFormComponent,
    PaperCardComponent,
    AddPaperFormComponent,
    AuthorPapersComponent,
    AuthorPapersComponent,
    ConfirmationDialogComponent,
    ScientificPaperEditorComponent,
    CoverLetterEditorComponent
    EditorPageComponent,
    PublishingProcessCardComponent,
    AssignReviewerDialogComponentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    ToastrModule.forRoot({
      progressBar: true,
      timeOut: 4000,
      closeButton: true,
      positionClass: 'toast-top-right',
      preventDuplicates: true
    }),
    BrowserAnimationsModule

  ],
  providers: [AllowedRoutes,
              AuthenticationService,
              ScientificPaperService,
              CoverLetterService,
              {
                provide: HTTP_INTERCEPTORS,
                useClass: TokenInterceptorService,
                multi: true
              }],
  entryComponents: [AssignReviewerDialogComponentComponent,ConfirmationDialogComponent ],
  bootstrap: [AppComponent]
})
export class AppModule { }
