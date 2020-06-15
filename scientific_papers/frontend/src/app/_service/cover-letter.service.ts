import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

const URL = 'http://localhost:8088/api/coverLetters';

@Injectable({
  providedIn: 'root'
})
export class CoverLetterService {

  constructor(private http: HttpClient) { }

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
