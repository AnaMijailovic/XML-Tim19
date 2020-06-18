import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

declare var require: any;
const convert = require('xml-js');

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

  getReviewRequests(): Observable<any> {
    return this.http.get('http://localhost:8088/api/reviewers/reviewRequests');
  }

  rejectReview(processId: string): Observable<any>  {
    return this.http.get(`http://localhost:8088/api/reviewers/accept/${processId}`);
  }
  aceptReview(processId: string): Observable<any>  {
    return this.http.get(`http://localhost:8088/api/reviewers/reject/${processId}`);
  }
}
