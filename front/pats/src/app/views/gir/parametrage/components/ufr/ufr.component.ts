import { Component } from '@angular/core';
import {UfrListComponent} from '../../components/ufr/components/ufr-list/ufr-list.component';

@Component({
  selector: 'app-ufr',
  standalone: true,
  imports: [UfrListComponent],
  templateUrl: './ufr.component.html',
  styleUrl: './ufr.component.scss'
})
export class UfrComponent {

}
