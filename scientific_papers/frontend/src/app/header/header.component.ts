import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { AllowedRoutes } from '../_service/allowed-routes.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  links = [];

  generalSearchForm: FormGroup;
  @Output()
  sendSearchData = new EventEmitter<string>();

  constructor(private routes: AllowedRoutes,
              public router: Router,
              private formBuilder: FormBuilder) {

    this.routes.currentRoutes.subscribe(routes => (this.links = routes));
   }

  ngOnInit() {
    this.routes.currentRoutes.subscribe(routes => (this.links = routes));

    this.createForm();
  }

  goToHome() {
    this.router.navigate(['/']);
  }

  createForm() {
    this.generalSearchForm = this.formBuilder.group({
      searchData: ['', []]
    });
  }

  generalSearch() {
    this.sendSearchData.emit(this.generalSearchForm.controls.searchData.value as string);

  }

}
