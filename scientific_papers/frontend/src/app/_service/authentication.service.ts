import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from "src/environments/environment";

@Injectable()
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post(
      `${environment.apiUrl}/api/users/login`,
      {username: username, password: password},
      {responseType: "text"}
    );
  }

  register(
    name: string,
    surname: string,
    username: string,
    email: string,
    password: string,
    confirmPassword: string
  ) {
    return this.http.post(
      `${environment.apiUrl}/api/users/register`,
      {
        name: name,
        surname: surname,
        username: username,
        email: email,
        password: password,
        passwordConfirm: confirmPassword,
        isEditor: false
      },
      {responseType: "text"}
    );
  }

  logout() {
    localStorage.removeItem("token");
  }
}
