import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

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
}
