import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  providers:[UserService, KeycloakService],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  isLoggedIn: boolean = false;
  
  userFirstName: string | undefined;
  userLastName: string | undefined;
  
  




  public newNotifications = [
    { id: 0, title: 'New user registered', icon: 'cilUserFollow', color: 'success' },
    { id: 1, title: 'User deleted', icon: 'cilUserUnfollow', color: 'danger' },
    { id: 2, title: 'Sales report is ready', icon: 'cilChartPie', color: 'info' },
    { id: 3, title: 'New client', icon: 'cilBasket', color: 'primary' },
    { id: 4, title: 'Server overloaded', icon: 'cilSpeedometer', color: 'warning' }
  ];
  
  constructor(private userService: UserService) {

  }

  ngOnInit() {
    this.userService.initialize();
    this.userService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      if (isLoggedIn) {
        // Mettre à jour les variables userFirstName et userLastName
        this.userFirstName = this.userService.userFirstName;
        this.userLastName = this.userService.userLastName;
      }
    });
  }



  login() {
    this.userService.login();
  }

  logout() {
    this.userService.logout();
  }



}
