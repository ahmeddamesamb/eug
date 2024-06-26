import { Component } from '@angular/core';
import {UniversiteListComponent} from './components/universite-list/universite-list.component'

@Component({
  selector: 'app-universite',
  standalone: true,
  imports: [UniversiteListComponent],
  templateUrl: './universite.component.html',
  styleUrl: './universite.component.scss'
})
export class UniversiteComponent {

}
