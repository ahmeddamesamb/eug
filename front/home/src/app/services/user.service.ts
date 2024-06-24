import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { KeycloakEventType, KeycloakService } from 'keycloak-angular';
import { NavigationExtras, Router } from '@angular/router';
import { ENVIRONMENT, KeycloakConfig} from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  public isLoggedIn$: Observable<boolean> = this._isLoggedIn.asObservable();
  public accessToken: string = "";


  constructor(private keycloakService: KeycloakService,  private router: Router // Injecter le service Router
) {
   // this.handleAuthEvents();
  }

  async initialize() {
    try {
      const initSuccess = await this.keycloakService.init({
        config: KeycloakConfig.keycloak, 
        initOptions: {
          pkceMethod: 'S256',
          redirectUri: ENVIRONMENT.UrlEspacePats,
          checkLoginIframe: false,
          
        }
      });

      if (initSuccess) {
        console.log("Keycloak initialisé avec succès");
      } else {
        console.error('Erreur lors de l\'initialisation de Keycloak');
      }
    } catch (error) {
      console.error('Erreur lors de l\'initialisation de Keycloak', error);
    }
  }


  async login() {
    try {
      await this.keycloakService.login();
    } catch (error) {
      console.error('Erreur lors de la connexion', error);
    }
  }

  

  
getToken(){
  return localStorage.getItem("token");
}

saveToken(token: string) {
  localStorage.setItem("token", token);
}


}
