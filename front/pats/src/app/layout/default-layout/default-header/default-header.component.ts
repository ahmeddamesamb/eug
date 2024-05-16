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


@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  standalone: true,
  imports: [ContainerComponent, HeaderTogglerDirective, SidebarToggleDirective, IconDirective, HeaderNavComponent, NavItemComponent, NavLinkDirective, RouterLink, RouterLinkActive, NgTemplateOutlet, BreadcrumbRouterComponent, ThemeDirective, DropdownComponent, DropdownToggleDirective, TextColorDirective, AvatarComponent, DropdownMenuDirective, DropdownHeaderDirective, DropdownItemDirective, BadgeComponent, DropdownDividerDirective, ProgressBarDirective, ProgressComponent, InputGroupComponent, InputGroupTextDirective, FormControlDirective, ButtonDirective, NgStyle, FormDirective,DefaultBreadcrumbComponent]
})
export class DefaultHeaderComponent extends HeaderComponent {

  readonly #colorModeService = inject(ColorModeService);
  readonly colorMode = this.#colorModeService.colorMode;

  constructor() {
    super();
    this.#colorModeService.localStorageItemName.set('coreui-pro-angular-admin-template-theme-modern');
    this.#colorModeService.eventName.set('ColorSchemeChange');
  }

  @Input() sidebarId: string = 'sidebar1';



  public newNotifications = [
    { id: 0, title: 'New user registered', icon: 'cilUserFollow', color: 'success' },
    { id: 1, title: 'User deleted', icon: 'cilUserUnfollow', color: 'danger' },
    { id: 2, title: 'Sales report is ready', icon: 'cilChartPie', color: 'info' },
    { id: 3, title: 'New client', icon: 'cilBasket', color: 'primary' },
    { id: 4, title: 'Server overloaded', icon: 'cilSpeedometer', color: 'warning' }
  ];


}
