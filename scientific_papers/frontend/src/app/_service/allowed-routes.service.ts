import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import * as jwt_decode from 'jwt-decode';

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
    const token = localStorage.getItem('token');
    const components = [];

    if (!token) {
      components.push({path: '/login', label: 'Login'});
      components.push({path: '/register', label: 'Register'});

      this.routes.next(components);
    } else {
      const decodedToken = jwt_decode(token);

      // TODO: Dopuniti sa putanjama!
      decodedToken.roles.forEach(role => {
        if (role.authority === 'ROLE_EDITOR') {
          components.push({path: '/submissions-in-process', label: 'Submissions in process'});
        }

        if (role.authority === 'ROLE_AUTHOR') {
          components.push({path: '/add-paper', label: 'Add paper'});
          components.push({path: '/my-papers', label: 'My papers'});
        }

        if (role.authority === 'ROLE_REVIEWER') {
          components.push({path: '/review-requests', label: 'Review requests'});
          components.push({path: '/reviews', label: 'Reviews'});
        }
      });

      //components.push({path: '/profile', label: 'Profile'});
      components.push({path: '/logout', label: 'Logout'});
      this.routes.next(components);
    }
  }
}
