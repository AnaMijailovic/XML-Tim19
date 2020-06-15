import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ScientificPaper } from '../_model/scientificPaper.model';

declare var require: any;
const convert = require('xml-js');

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  papers: ScientificPaper[] = [];
  allPapers: ScientificPaper[] = [];

  constructor(private toastr: ToastrService,
              private spService: ScientificPaperService) { }

  ngOnInit() {
    this.getScientificPapers('');
  }

  getScientificPapers(params: string) {
    this.papers = [];

    this.spService.getScientificPapers(params).subscribe(
      (response) => {
        this.papers = this.spService.responseToArray(response);
        this.allPapers = this.papers;
      });

  }

  sendSearchData(searchText: string) {
    this.getScientificPapers('?searchText=' + searchText);
  }

  sendSearchParams(searchText: string) {
    this.getScientificPapers('?' + searchText);
  }

  showSuccess() {
    this.toastr.success('Hello world!', 'Toastr fun!');
  }

  showError() {
    this.toastr.error('Hello world!', 'Toastr fun!');
  }

  showInfo(message: string, header: string) {
    this.toastr.info(message, header);
  }

  showWarning() {
    this.toastr.warning('Hello world!', 'Toastr fun!');
  }

}
