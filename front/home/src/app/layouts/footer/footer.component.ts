import { Component } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss'
})
export class FooterComponent {

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
