import { Component } from '@angular/core';
import {CampagneListComponent} from './components/campagne-list/campagne-list.component'

@Component({
  selector: 'app-campagnes',
  standalone: true,
  imports: [CampagneListComponent],
  templateUrl: './campagnes.component.html',
  styleUrl: './campagnes.component.scss'
})
export class CampagnesComponent {

}
