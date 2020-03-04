import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() { }

  generateParams(data: any): string {

    return Object.keys(data).map(k => {
      if (data[k]) {
        if (data[k] instanceof Date) {
          return encodeURIComponent(k) + '=' + encodeURIComponent( + data[k]);
        } else {
          return encodeURIComponent(k) + '=' + encodeURIComponent(data[k]);
        }
      }
    }).filter(Boolean).join('&');

  }
}
