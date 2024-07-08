import { Component } from '@angular/core';
import {EtatValidationComponent} from 'src/app/shared/components/etat-validation/etat-validation.component'
@Component({
  selector: 'app-etape-inscription',
  standalone: true,
  imports: [EtatValidationComponent],
  templateUrl: './etape-inscription.component.html',
  styleUrl: './etape-inscription.component.scss'
})
export class EtapeInscriptionComponent {

  panes = [
    { name: 'Déclencher' , etat: true},          
    { name: 'Quitus BU' , etat: false},
    { name: 'Quitus CROUS' , etat: false},
    { name: 'Visite Médicale' , etat: false},
    { name: 'Carte délivrée' , etat: false},
    { name: 'Paiement' , etat: false},
  ]

}
