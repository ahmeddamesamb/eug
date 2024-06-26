import { NgStyle, NgTemplateOutlet } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  AvatarComponent,
  BadgeComponent,
  BreadcrumbRouterComponent,
  ButtonDirective,
  ColorModeService,
  ContainerComponent,
  DropdownComponent,
  DropdownDividerDirective,
  DropdownHeaderDirective,
  DropdownItemDirective,
  DropdownMenuDirective,
  DropdownToggleDirective,
  FormControlDirective,
  FormDirective,
  HeaderComponent,
  HeaderNavComponent,
  HeaderTogglerDirective,
  InputGroupComponent,
  InputGroupTextDirective,
  NavItemComponent,
  NavLinkDirective,
  ProgressBarDirective,
  ProgressComponent,
  SidebarToggleDirective,
  TextColorDirective,
  ThemeDirective
} from '@coreui/angular-pro';

import { IconDirective } from '@coreui/icons-angular';
import { DefaultBreadcrumbComponent } from '.././';
import { UserService } from '../../../services/user.service';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  styleUrls: ['./default-header.component.scss'],
  standalone: true,
  providers: [UserService, KeycloakService],
  imports: [ContainerComponent, HeaderTogglerDirective, SidebarToggleDirective, IconDirective, HeaderNavComponent, NavItemComponent, NavLinkDirective, RouterLink, RouterLinkActive, NgTemplateOutlet, BreadcrumbRouterComponent, ThemeDirective, DropdownComponent, DropdownToggleDirective, TextColorDirective, AvatarComponent, DropdownMenuDirective, DropdownHeaderDirective, DropdownItemDirective, BadgeComponent, DropdownDividerDirective, ProgressBarDirective, ProgressComponent, InputGroupComponent, InputGroupTextDirective, FormControlDirective, ButtonDirective, NgStyle, FormDirective,DefaultBreadcrumbComponent]
})
export class DefaultHeaderComponent extends HeaderComponent {

  readonly #colorModeService = inject(ColorModeService);
  readonly colorMode = this.#colorModeService.colorMode;
  isLoggedIn: boolean = false;
  
  userFirstName: string | undefined;
  userLastName: string | undefined;


  constructor(private userService: UserService) {
    super();
    this.#colorModeService.localStorageItemName.set('coreui-pro-angular-admin-template-theme-modern');
    this.#colorModeService.eventName.set('ColorSchemeChange');
  }

  @Input() sidebarId: string = 'sidebar1';



  

  ngOnInit() {
    const token = this.userService.getToken();
    console.log("Token récupéré depuis le localStorage :", token);
    this.userService.initializeWithToken(token ?? '');
    this.userService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      if (isLoggedIn) {
        // Mettre à jour les variables userFirstName et userLastName
        this.userFirstName = this.userService.getUserFirstName();
        this.userLastName = this.userService.getUserLastName();
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
