import { Component } from '@angular/core';
import {DomaineListComponent} from './components/domaine-list/domaine-list.component'

@Component({
  selector: 'app-domaine',
  standalone: true,
  imports: [DomaineListComponent],
  templateUrl: './domaine.component.html',
  styleUrl: './domaine.component.scss'
})
export class DomaineComponent {

}
