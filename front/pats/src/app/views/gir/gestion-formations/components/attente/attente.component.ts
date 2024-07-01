import { Component } from '@angular/core';
import {SharedAttenteComponent} from "../../../../../shared/components/shared-attente/shared-attente.component";

@Component({
  selector: 'app-attente',
  standalone: true,
  imports: [SharedAttenteComponent],
  templateUrl: './attente.component.html',
  styleUrl: './attente.component.scss'
})
export class AttenteComponent {

}
