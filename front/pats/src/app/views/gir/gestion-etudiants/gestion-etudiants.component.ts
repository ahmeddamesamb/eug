import { Component } from '@angular/core';
import {EtudiantListComponent} from './etudiant-list/etudiant-list.component';
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
  selector: 'app-gestion-etudiants',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent,
     NavComponent, NavItemComponent, NavLinkDirective, TabContentRefDirective, TabContentComponent,
      RoundedDirective, TabPaneComponent, EtudiantListComponent,RouterOutlet],
  templateUrl: './gestion-etudiants.component.html',
  styleUrl: './gestion-etudiants.component.scss'
})
export class GestionEtudiantsComponent {

}
