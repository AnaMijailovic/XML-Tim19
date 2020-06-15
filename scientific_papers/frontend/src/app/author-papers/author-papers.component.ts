import { Component, OnInit } from '@angular/core';
import { UtilService } from '../_service/util.service';
import { ScientificPaper } from '../_model/scientificPaper.model';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ToastrService } from 'ngx-toastr';

declare var require: any;
const convert = require('xml-js');

@Component({
  selector: 'app-author-papers',
  templateUrl: './author-papers.component.html',
  styleUrls: ['./author-papers.component.scss']
})
export class AuthorPapersComponent implements OnInit {
  papers: ScientificPaper[] = [];

  constructor(private utilService: UtilService,
              private spService: ScientificPaperService,
              private toastr: ToastrService) { }

  ngOnInit() {
  }

  getScientificPapers(params: string) {
    this.papers = [];

    this.spService.getScientificPapers(params).subscribe(
      (response) => {
        this.papers = this.spService.responseToArray(response);
      });

  }

  sendSearchData(searchText: string) {

    this.getScientificPapers('?searchText=' + searchText +
                             '&loggedAuthor=' + this.utilService.getLoggedUser() );
  }

  sendSearchParams(params: string) {
    if (params === '') {
      params = 'loggedAuthor=' + this.utilService.getLoggedUser();
    } else {
      params = params + '&loggedAuthor=' + this.utilService.getLoggedUser();
    }

    alert('papehf: ' + params);
    this.getScientificPapers('?' + params);

  }
}
