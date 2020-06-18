import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

declare var require: any;
const convert = require('xml-js');

@Injectable({
  providedIn: 'root'
})
export class PublishingProcessService {
  constructor(private http: HttpClient) { }

  getOngoingPublishingProcesses(): Observable<any> {
    return this.http.get('http://localhost:8088/api/publishingProcess/ongoing');
  }

  assignEditor(processId: string): Observable<any> {
    return this.http.put(`http://localhost:8088/api/publishingProcess/assignEditor/${processId}`, null);
  }

  getAllReviewers(): Observable<any> {
    return this.http.get('http://localhost:8088/api/reviewers');
  }

  assignReviewer(processId: string, reviewerId: string): Observable<any>{
    return this.http.put(`http://localhost:8088/api/reviewers/assign/${processId},${reviewerId}`, null);
  }

  updatePaperStatus(processId: string, status: string) {
    return this.http.put(`http://localhost:8088/api/publishingProcess/status/${processId},${status}`, null);
  }
}
