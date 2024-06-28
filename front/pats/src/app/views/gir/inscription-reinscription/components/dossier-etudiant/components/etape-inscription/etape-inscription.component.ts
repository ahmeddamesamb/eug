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

}
