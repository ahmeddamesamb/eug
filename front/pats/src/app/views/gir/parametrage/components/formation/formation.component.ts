import { Component } from '@angular/core';

import {FormationListComponent} from './components/formation-list/formation-list.component'

@Component({
  selector: 'app-formation',
  standalone: true,
  imports: [FormationListComponent],
  templateUrl: './formation.component.html',
  styleUrl: './formation.component.scss'
})
export class FormationComponent {

}
