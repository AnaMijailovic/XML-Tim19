import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { ScientificPaper } from '../_model/scientificPaper.model';

declare var require: any;
const convert = require('xml-js');

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  getScientificPapers(params: string): Observable<string> {

    return this.http.get('http://localhost:8088/api/scientificPapers' + params, { responseType: 'text' });

  }

  addScientificPaper(paperXml: string) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/xml',
        'Response-Type': 'text'
      })
    };
    return this.http.post('http://localhost:8088/api/scientificPapers', paperXml, httpOptions);
  }

  responseToArray(response: any): ScientificPaper[] {
    const returnPapers: ScientificPaper[] = [];
    if (response != null) {

      const jsonResponse = JSON.parse(convert.xml2json(response, { compact: true, spaces: 4 } ));
      let papers = jsonResponse.search.paper;

      // if response is undefined -> no results
      if (!papers) {
        return returnPapers;
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

        returnPapers.push(scientificPaper);

      }
    }
    return returnPapers;
  }
}
