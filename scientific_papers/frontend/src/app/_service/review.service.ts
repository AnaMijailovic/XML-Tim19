import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

declare var require: any;
const convert = require('xml-js');

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  constructor(private http: HttpClient) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/xml'
    })
  };

  getReviewRequests(): Observable<any> {
    return this.http.get('http://localhost:8088/api/reviewers/reviewRequests');
  }

  acceptReview(processId: string): Observable<any>  {
    return this.http.post(`http://localhost:8088/api/reviewers/accept/${processId}`, null);
  }
  rejectReview(processId: string): Observable<any>  {
    return this.http.post(`http://localhost:8088/api/reviewers/reject/${processId}`, null);
  }

  getAssignedReviews(): Observable<any>  {
    return this.http.get('http://localhost:8088/api/reviewers/assignedReviews');
  }

  submitEvaluationForm(reviewXml: string, processId: string): Observable<any> {
    return this.http.post(`http://localhost:8088/api/evaluationForms/${processId}`, reviewXml, this.httpOptions);
  }

  getTemplate(): Observable<string> {
    return this.http.get('http://localhost:8088/api/evaluationForms/template', { responseType: 'text' });
  }
}
