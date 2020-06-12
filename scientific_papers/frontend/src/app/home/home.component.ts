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

  constructor(private toastr: ToastrService,
              private spService: ScientificPaperService) { }

  ngOnInit() {
    this.getScientificPapers('');
  }

  getScientificPapers(params: string) {
    this.papers = [];

    this.spService.getScientificPapers(params).subscribe(
      (response) => {

        if (response != null) {

          const jsonResponse = JSON.parse(convert.xml2json(response, { compact: true, spaces: 4 } ));
          let papers = jsonResponse.search.paper;

          // TODO Remove this from here
          // if response is undefined -> no results
          if (!papers) {
            this.showInfo('No results', '');
            return;
          }

          // if not an array -> one result
          if (!Array.isArray(papers)) {
            papers = [papers];
          }

          for ( const paper of papers ) {
            const authorsList = [];
            for (const author of paper.author) {
              authorsList.push(author._text);
            }

            const keywordsList = [];
            for (const keyword of paper.keyword) {
              keywordsList.push(keyword._text);
            }

            const scientificPaper: ScientificPaper = {
                id : paper.id._text,
                title : paper.title._text,
                acceptedDate : paper.accepted_date._text,
                authors : authorsList,
                keywords: keywordsList
            };

            this.papers.push(scientificPaper);

          }
        }
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
