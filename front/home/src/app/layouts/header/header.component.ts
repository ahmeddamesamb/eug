import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  // providers:[UserService, KeycloakService],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  public newNotifications = [
    { id: 0, title: 'New user registered', icon: 'cilUserFollow', color: 'success' },
    { id: 1, title: 'User deleted', icon: 'cilUserUnfollow', color: 'danger' },
    { id: 2, title: 'Sales report is ready', icon: 'cilChartPie', color: 'info' },
    { id: 3, title: 'New client', icon: 'cilBasket', color: 'primary' },
    { id: 4, title: 'Server overloaded', icon: 'cilSpeedometer', color: 'warning' }
  ];
  
  public urlLogin = environment.urlLogin; 
  public realm_pats = environment.realm_pats;
  public realm_per = environment.realm_per;
  public realm_etudiant = environment.realm_etudiant; 
  public redirectUrl_pats = environment.redirectUrl_pats;
  public redirectUrl_per = environment.redirectUrl_per;
  public redirectUrl_etudiant = environment.redirectUrl_etudiant;
  public clientId = environment.clientId;
  public suiteUrlLogin = environment.suiteUrlLogin;

  public getUrl(realm:string, client:string): string{
    return  `${this.urlLogin}${realm}${this.suiteUrlLogin}${this.clientId}`;
  }
  }

  
 



