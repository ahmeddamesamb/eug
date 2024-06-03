import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { KeycloakEventType, KeycloakService } from 'keycloak-angular';
import { NavigationExtras, Router } from '@angular/router';
import { environment} from '../config/environment';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  public isLoggedIn$: Observable<boolean> = this._isLoggedIn.asObservable();
  public accessToken: string = "";
  public userFirstName: string | undefined;
  public userLastName: string | undefined;
  public userEmail: string | undefined;
  public username: string | undefined;

  constructor(private keycloakService: KeycloakService,  private router: Router // Injecter le service Router
) {
    this.handleAuthEvents();
  }

  async initialize() {
    try {
      const initSuccess = await this.keycloakService.init({
        config: environment.keycloak, 

        initOptions: {
          pkceMethod: 'S256',
          redirectUri: 'http://localhost:4200/',
          checkLoginIframe: false,
         // onLoad: 'login-required'
          
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
        this.accessToken = "";
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
      await this.keycloakService.logout('http://localhost:4201');
      this._isLoggedIn.next(false);
      this.accessToken = "";
      this.userFirstName = undefined;
      this.userLastName = undefined;
      this.userEmail = undefined;
      this.username = undefined;
    } catch (error) {
      console.error('Erreur lors de la déconnexion', error);
    }
  }

  public async updateLoginStatus() {
   
    const userInfo = await this.keycloakService.loadUserProfile();
    console.log("Informations de l'utilisateur :", userInfo);

    this.accessToken = await this.keycloakService.getToken();
    this.saveToken(this.accessToken);

    // Stocker les informations de l'utilisateur dans les variables appropriées
    this.userFirstName = userInfo.firstName;
    this.userLastName = userInfo.lastName;
    this.userEmail = userInfo.email;
    this.username = userInfo.username;

    this._isLoggedIn.next(true);
   

}
  // public async updateLoginStatus() {
   
  //     // Récupérer les rôles de l'utilisateur
  //     const roles = await this.keycloakService.getUserRoles();
  //     const hasPatRole = roles.includes('pats');

  //     if (hasPatRole) {
  //       const userInfo = await this.keycloakService.loadUserProfile();
  //       console.log("Informations de l'utilisateur :", userInfo);

  //       this.accessToken = await this.keycloakService.getToken();
  //       this.saveToken(this.accessToken);


  //       // Stocker les informations de l'utilisateur dans les variables appropriées
  //       this.userFirstName = userInfo.firstName;
  //       this.userLastName = userInfo.lastName;
  //       this.userEmail = userInfo.email;
  //       this.username = userInfo.username;

  //       this._isLoggedIn.next(true);
  //     } 
  //     else {
  //       console.log("L'utilisateur n'a pas le rôle 'pats'");
  //       this._isLoggedIn.next(false);
  //       await this.logout();

  //       // Rediriger l'utilisateur vers la page NotAuthorizedComponent
  //       const navigationExtras: NavigationExtras = {
  //         queryParamsHandling: 'preserve',
  //         preserveFragment: true
  //       };
  //       this.router.navigate(['/404'], navigationExtras);
      
  //   } 
  
  // }
  public getUserFirstName(): string | undefined {
    return this.userFirstName;
  }

  // Nouvelle méthode pour récupérer le lastName
  public getUserLastName(): string | undefined {
    return this.userLastName;
  }

  
 // Méthode pour récupérer le token depuis le localStorage
getToken(){
  return localStorage.getItem("token");
}

// Méthode pour enregistrer le token dans le localStorage
saveToken(token: string) {
  localStorage.setItem("token", token);
}

  async initializeWithToken(token: string) {
    console.log("Token: ", token);
    try {
      const initSuccess = await this.keycloakService.init({
        config: environment.keycloak, 
        initOptions: {
          pkceMethod: 'S256',
          redirectUri: 'http://localhost:4200/', // Remplacez par l'URL de redirection de votre application
          checkLoginIframe: false,
          onLoad: 'check-sso',
          token: token // Fournir le token d'accès existant
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

}
