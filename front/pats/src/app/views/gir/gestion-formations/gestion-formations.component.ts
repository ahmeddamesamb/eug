import { Component } from '@angular/core';
import {FormationListComponent} from './components/formation-list/formation-list.component'
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
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-gestion-formations',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent,
    NavComponent, NavItemComponent, NavLinkDirective, TabContentRefDirective, TabContentComponent,
     RoundedDirective, TabPaneComponent, FormationListComponent,RouterOutlet],
  templateUrl: './gestion-formations.component.html',
  styleUrl: './gestion-formations.component.scss'
})
export class GestionFormationsComponent {

}
