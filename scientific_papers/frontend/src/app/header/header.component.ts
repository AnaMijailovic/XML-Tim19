import { Component, OnInit } from '@angular/core';
import { AllowedRoutes } from '../_service/allowed-routes.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  links = [];
  
  constructor(private routes: AllowedRoutes) {
    this.routes.currentRoutes.subscribe(routes => (this.links = routes));
   }

  ngOnInit() {
    this.routes.currentRoutes.subscribe(routes => (this.links = routes))
  }

}
