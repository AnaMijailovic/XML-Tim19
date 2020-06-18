import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

const URL = 'http://localhost:8088/api/coverLetters';

@Injectable({
  providedIn: 'root'
})
export class CoverLetterService {

  constructor(private http: HttpClient) { }

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

  addCoverLetter(letterXml: string, processId: string) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/xml',
        'Response-Type': 'text'
      })
    };
    return this.http.post( URL + '?processId=' + processId, letterXml, httpOptions);
  }
}
