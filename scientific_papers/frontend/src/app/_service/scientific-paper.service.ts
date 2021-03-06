import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { ScientificPaper } from '../_model/scientificPaper.model';

declare var require: any;
const convert = require('xml-js');

const URL = 'http://localhost:8088/api/scientificPapers';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/xml',
      'Response-Type': 'text'
    })
  };

  getTemplate(): Observable<string> {
    return this.http.get(URL + '/template', { responseType: 'text' });
  }

  getPdf(paperId: string) {
    window.open(URL + '/pdf/' + paperId, '_blank');
  }

  getXml(paperId: string) {
    window.open(URL + '/xml/' + paperId, '_blank');
  }

  getHtml(paperId: string) {
    window.open(URL + '/html/' + paperId, '_blank');
  }

  getLetterPdf(paperId: string) {
    window.open(URL + '/' + paperId + '/coverLetter/pdf', '_blank');
  }

  getLetterXml(paperId: string) {
    window.open(URL + '/' + paperId + '/coverLetter/xml', '_blank');
  }

  getLetterHtml(paperId: string) {
    window.open(URL + '/' + paperId + '/coverLetter/html', '_blank');
  }

  getMetadataRdf(paperId: string): Observable<string> {
    return this.http.get(URL + '/metadata/xml/' + paperId, { responseType: 'text' });
  }

  getMetadataJson(paperId: string): Observable<string> {
    return this.http.get(URL + '/metadata/json/' + paperId, { responseType: 'text' });
  }

  getScientificPapers(params: string): Observable<string> {
    return this.http.get(URL + params, { responseType: 'text' });
  }

  getQuotedBy(paperId: string): Observable<string> {
    return this.http.get(URL + '/' + paperId + '/quotes', { responseType: 'text' });
  }

  addScientificPaper(paperXml: string) {

    return this.http.post(URL, paperXml, this.httpOptions);
  }

  addPaperReview(paperXml: string, processId: string) {

    return this.http.post(URL + '/revision?processId=' + processId, paperXml, this.httpOptions);
  }

  withdrawPaper(paperId: string) {
    return this.http.delete(URL + '/' + paperId, this.httpOptions);
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
            recievedDate: paper.recieved_date._text,
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
