import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../_service/authentication.service';
import { AllowedRoutes } from '../_service/allowed-routes.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(private authService: AuthenticationService, 
              private router: Router, 
              private allowedRoutes: AllowedRoutes) {}

  ngOnInit() {
    this.authService.logout();
    this.router.navigate(['login']);
    this.allowedRoutes.updateRoutes();
  }

}
