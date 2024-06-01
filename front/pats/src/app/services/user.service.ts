import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { KeycloakEventType, KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  public isLoggedIn$: Observable<boolean> = this._isLoggedIn.asObservable();
  public accessToken: string | undefined;
  public userFirstName: string | undefined;
  public userLastName: string | undefined;
  public userEmail: string | undefined;
  public username: string | undefined;

  constructor(private keycloakService: KeycloakService) {
    this.handleAuthEvents();
  }

  async initialize() {
    try {
      const initSuccess = await this.keycloakService.init({
        config: {
          url: 'http://localhost:8080/',
          realm: 'e-ugb',
          clientId: 'pats-client'
        },
        initOptions: {
          pkceMethod: 'S256',
          redirectUri: 'http://localhost:4200/',
          checkLoginIframe: false
        }
      });

      if (initSuccess) {
        console.log("Keycloak initialisé avec succès");
        this.updateLoginStatus();
      } else {
        console.error('Erreur lors de l\'initialisation de Keycloak');
      }
    } catch (error) {
      console.error('Erreur lors de l\'initialisation de Keycloak', error);
    }
  }

  private handleAuthEvents() {
    this.keycloakService.keycloakEvents$.subscribe(event => {
      if (event.type === KeycloakEventType.OnAuthSuccess) {
        this.updateLoginStatus();
      } else if (event.type === KeycloakEventType.OnAuthLogout) {
        this._isLoggedIn.next(false);
        this.accessToken = undefined;
        this.userFirstName = undefined;
        this.userLastName = undefined;
        this.userEmail = undefined;
        this.username = undefined;
      }
    });
  }

  async login() {
    try {
      await this.keycloakService.login();
    } catch (error) {
      console.error('Erreur lors de la connexion', error);
    }
  }

  async logout() {
    try {
      await this.keycloakService.logout();
      this._isLoggedIn.next(false);
      this.accessToken = undefined;
      this.userFirstName = undefined;
      this.userLastName = undefined;
      this.userEmail = undefined;
      this.username = undefined;
    } catch (error) {
      console.error('Erreur lors de la déconnexion', error);
    }
  }

  private async updateLoginStatus() {
    const isLoggedIn = await this.keycloakService.isLoggedIn();
    console.log("isLoggedIn", isLoggedIn);
  
    if (isLoggedIn) {
      this.accessToken = await this.keycloakService.getToken();
  
      // Récupérer les informations de l'utilisateur
      const userInfo = await this.keycloakService.loadUserProfile();
      console.log("Informations de l'utilisateur :", userInfo);
      
      // Stocker les informations de l'utilisateur dans les variables appropriées
      this.userFirstName = userInfo.firstName;
      this.userLastName = userInfo.lastName;
      this.userEmail = userInfo.email;
      this.username = userInfo.username;
    }
  
    this._isLoggedIn.next(isLoggedIn);
  }
}
