import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AllowedRoutes } from '../_service/allowed-routes.service';
import { AuthenticationService } from '../_service/authentication.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private router: Router,
    private allowedRoutes: AllowedRoutes,
    private toastr: ToastrService
  ) {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {}

  login() {
    const params = this.form.value;

    if (params.username && params.password) {
      this.authService.login(params.username, params.password).subscribe(
        token => { localStorage.setItem('token', token);
                   this.router.navigate(['/']);
                   this.allowedRoutes.updateRoutes();
      },
      response => {
        try {
          const errorResponse = JSON.parse(response.error);
          let errorAlert = '';
          errorResponse.errors.array.forEach(e => {
            errorAlert += `${e.defaultMessage}\n`;
          });
          this.toastr.error('Error', errorAlert);
        } catch (e) {
          this.toastr.error('Error', response.error);
        }
      });
    }
  }
}
