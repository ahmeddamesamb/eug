import { Component } from '@angular/core';

import {MinistereListComponent} from './components/ministere-list/ministere-list.component'

@Component({
  selector: 'app-ministere',
  standalone: true,
  imports: [MinistereListComponent],
  templateUrl: './ministere.component.html',
  styleUrl: './ministere.component.scss'
})
export class MinistereComponent {
  
}
