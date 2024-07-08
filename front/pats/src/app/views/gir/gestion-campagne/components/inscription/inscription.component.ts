import { Component } from '@angular/core';
import { InscriptionListComponent} from '../inscription/components/inscription-list/inscription-list.component';

@Component({
  selector: 'app-inscription',
  standalone: true,
  imports: [InscriptionListComponent],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.scss'
})
export class InscriptionComponent {

}
