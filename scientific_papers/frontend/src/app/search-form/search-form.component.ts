import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { UtilService } from '../_service/util.service';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html',
  styleUrls: ['./search-form.component.scss']
})
export class SearchFormComponent implements OnInit {

  @Output()
  sendSearchParams = new EventEmitter<string>();
  searchForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private utilService: UtilService) { }

  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.searchForm = this.formBuilder.group({
      title: ['', []],
      author: ['', []],
      affiliation: ['', []],
      keyword: ['', []],
      fromDate: ['', []],
      toDate: ['', []],
    });

  }

  onSearchSubmit() {
    const params: string = this.utilService.generateParams(this.searchForm.value);
    alert('Params: ' + params);
  }

}
