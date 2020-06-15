import { Injectable } from '@angular/core';
import { HttpInterceptor } from '@angular/common/http';
import { HttpRequest } from '@angular/common/http';
import { HttpHandler } from '@angular/common/http';
import { HttpEvent, HttpHeaders } from '@angular/common/http';
import { Injector } from '@angular/core';
import { Observable } from 'rxjs';
import * as jwt_decode from 'jwt-decode';

@Injectable()
export class TokenInterceptorService implements HttpInterceptor {

  constructor(private inj: Injector) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.getToken();
    if (token) {
      /*alert('adding token');
      const decodedToken = jwt_decode(token);
      decodedToken.roles.forEach(role => {
        alert('role: ' + JSON.stringify(role));
      }); */
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.getToken()}`
        }
      });
    } else {
      request = request.clone();
    }


    return next.handle(request);
  }
  getToken(): string {
    return localStorage.getItem('token');
  }

}
