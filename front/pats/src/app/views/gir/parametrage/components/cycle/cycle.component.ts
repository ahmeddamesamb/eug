import { Component } from '@angular/core';
import {CycleListComponent} from '../cycle/components/cycle-list/cycle-list.component'

@Component({
  selector: 'app-cycle',
  standalone: true,
  imports: [CycleListComponent],
  templateUrl: './cycle.component.html',
  styleUrl: './cycle.component.scss'
})
export class CycleComponent {

}
