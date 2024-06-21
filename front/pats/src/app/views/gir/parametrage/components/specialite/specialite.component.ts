import { Component } from '@angular/core';
import {SpecialiteListComponent} from './components/specialite-list/specialite-list.component';


@Component({
  selector: 'app-specialite',
  standalone: true,
  imports: [SpecialiteListComponent],
  templateUrl: './specialite.component.html',
  styleUrl: './specialite.component.scss'
})
export class SpecialiteComponent {

}
