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
  selector: 'app-parametrage',
  standalone: true,
  templateUrl: './parametrage.component.html',
  styleUrl: './parametrage.component.scss',
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, NavComponent, NavItemComponent, NavLinkDirective, TabContentRefDirective, RouterLink, IconDirective, TabContentComponent, RoundedDirective, TabPaneComponent,RouterOutlet]
})
export class ParametrageComponent {


  public panes = [
    { name: 'Formation', id: 'tab-01' , route: 'formation'},
    { name: 'Spécialité', id: 'tab-02', route: 'specialite' },
    { name: 'Mention', id: 'tab-03' , route: 'mention'},
    { name: 'Domaine', id: 'tab-04', route: 'domaine' },
    { name: 'UFR', id: 'tab-05', route: 'ufr'},
    { name: 'Université', id: 'tab-06' , route: 'universite'},
    { name: 'Ministère', id: 'tab-07' , route: 'ministere'},
    { name: 'Cycle', id: 'tab-08' , route: 'cycle'},
   /* { name: 'Pays', id: 'tab-09' , route: 'pays'},
    { name: 'Zone', id: 'tab-10' , route: 'zone'},*/
  ];
}
