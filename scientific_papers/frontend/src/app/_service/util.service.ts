import { Injectable } from '@angular/core';
import * as jwt_decode from 'jwt-decode';
import { decode } from 'punycode';

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

  getLoggedUser() {
    const token = localStorage.getItem('token');

    if (!token) {
      return '';

    } else {
      const decodedToken = jwt_decode(token);
      // alert(JSON.stringify(decodedToken));
      // alert('Username: ' + decodedToken.sub);
      return decodedToken.sub;
    }
  }
}
