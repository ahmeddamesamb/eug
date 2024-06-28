import { Component } from '@angular/core';
import { IconDirective } from '@coreui/icons-angular';
import { RouterLink, RouterOutlet } from '@angular/router';

import {
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  NavComponent,
  NavItemComponent,
  NavLinkDirective,
  RoundedDirective,
  RowComponent,
  TabContentComponent,
  TabContentRefDirective,
  TabPaneComponent,
  TextColorDirective
} from '@coreui/angular-pro';

@Component({
  selector: 'app-gestion-campagne',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective,
    CardComponent, CardHeaderComponent, CardBodyComponent,
    NavComponent, NavItemComponent, NavLinkDirective,
    TabContentRefDirective, RouterLink, IconDirective,
    TabContentComponent, RoundedDirective, TabPaneComponent,
    RouterOutlet],
  templateUrl: './gestion-campagne.component.html',
  styleUrl: './gestion-campagne.component.scss'
})
export class GestionCampagneComponent {

  public panes = [
    { name: 'En cours', id: 'tab-01' , route: 'en-cours'},
    { name: 'Terminées', id: 'tab-02', route: 'terminees' },
    { name: 'Inscription', id: 'tab-03' , route: 'inscription'},
    { name: 'Campagnes', id: 'tab-04', route: 'campagnes' },

  ];

}
