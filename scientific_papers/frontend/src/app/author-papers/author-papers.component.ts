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

        if (response != null) {

          const jsonResponse = JSON.parse(convert.xml2json(response, { compact: true, spaces: 4 } ));
          let papers = jsonResponse.search.paper;
          alert(papers);
          // TODO Remove this from here
          // if response is undefined -> no results
          if (!papers) {
            this.toastr.info('No results', '');
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
                processId: paper.process_id._text,
                paperStatus: paper.paper_status._text,
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
