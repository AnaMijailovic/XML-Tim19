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
    this.getScientificPapers();
  }

  getScientificPapers() {
    this.papers = [];

    this.spService.getScientificPapers().subscribe(
      (response) => {

        if (response != null) {
          const jsonResponse = JSON.parse(convert.xml2json(response, { compact: true, spaces: 4 } ));

         // alert(JSON.stringify(jsonResponse.search.paper[0]));
          for ( const paper of jsonResponse.search.paper ) {
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
           // alert(JSON.stringify(scientificPaper));
          }
        }
       // alert(JSON.stringify(this.papers));
      });

  }

  showSuccess() {
    this.toastr.success('Hello world!', 'Toastr fun!');
  }

  showError() {
    this.toastr.error('Hello world!', 'Toastr fun!');
  }

  showInfo() {
    this.toastr.info('Hello world!', 'Toastr fun!');
  }

  showWarning() {
    this.toastr.warning('Hello world!', 'Toastr fun!');
  }

}
