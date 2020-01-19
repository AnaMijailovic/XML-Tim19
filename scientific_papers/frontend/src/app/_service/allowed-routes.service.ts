import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import * as jwt_decode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AllowedRoutes {
  private routes = new BehaviorSubject([]);
  currentRoutes = this.routes.asObservable();

  constructor() { 
    this.updateRoutes();
  }

  updateRoutes() {
    let token = localStorage.getItem("token");
    let components = [];

    if (!token) {
      components.push({path: "login", label: "Login"});
      components.push({path: "register", label: "Register"});

      this.routes.next(components);
    } else {
      let decodedToken = jwt_decode(token);
      
      // TODO: Dopuniti sa putanjama!
      decodedToken.roles.forEach(role => {
        if (role.authority == "ROLE_EDITOR") {
          
        }
        
        if (role.authority == "ROLE_AUTHOR") {
          components.push({path: "submitted-papers", label: "Submitted papers"});
        }

        if (role.authority == "ROLE_REVIEWER") {
          components.push({path: "reviewes", label: "Reviewes"});
        }
      });

      components.push({path: "profile", label: "Profile"});
      components.push({path: "logout", label:"Logout"})
      this.routes.next(components);
    }
  }
}
