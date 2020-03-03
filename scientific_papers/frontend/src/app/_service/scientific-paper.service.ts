import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  getScientificPapers(): Observable<string> {

    return this.http.get( 'http://localhost:8088/api/scientificPapers', { responseType: 'text' });

}
}
