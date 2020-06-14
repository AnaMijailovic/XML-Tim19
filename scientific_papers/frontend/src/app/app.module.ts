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
import { HttpClientModule } from '@angular/common/http';
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
    AddPaperFormComponent
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
  providers: [AllowedRoutes, AuthenticationService, ScientificPaperService, CoverLetterService],
  bootstrap: [AppComponent]
})
export class AppModule { }
