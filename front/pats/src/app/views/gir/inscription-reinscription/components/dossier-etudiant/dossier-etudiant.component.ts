import { Component } from '@angular/core';
import {ProfilEtudiantComponent} from './components/profil-etudiant/profil-etudiant.component'
import {EtapeInscriptionComponent} from './components/etape-inscription/etape-inscription.component'
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
import { RouterLink, RouterOutlet } from '@angular/router';
import { IconDirective } from '@coreui/icons-angular';

@Component({
  selector: 'app-dossier-etudiant',
  standalone: true,
  imports: [ProfilEtudiantComponent,EtapeInscriptionComponent,RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, NavComponent, NavItemComponent, NavLinkDirective, TabContentRefDirective, RouterLink, IconDirective, TabContentComponent, RoundedDirective, TabPaneComponent,RouterOutlet],
  templateUrl: './dossier-etudiant.component.html',
  styleUrl: './dossier-etudiant.component.scss'
})
export class DossierEtudiantComponent {


  public panes = [
    { name: 'Inscription-Reinscription', id: 'tab-01' , route: 'inscription-reinscription'},
    { name: 'Bibliothéque', id: 'tab-02', route: 'bibliotheque' },
    { name: 'ACP', id: 'tab-03' , route: 'acp'},
    { name: 'Crous', id: 'tab-04', route: 'crous' },
    { name: 'Infos Etudiant', id: 'tab-05', route: 'infos-etudiant'},
  ];

}
