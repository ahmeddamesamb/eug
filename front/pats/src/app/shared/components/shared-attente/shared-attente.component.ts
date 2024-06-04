import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-shared-attente',
  standalone: true,
  imports: [],
  templateUrl: './shared-attente.component.html',
  styleUrl: './shared-attente.component.scss'
})
export class SharedAttenteComponent {

  @Input() public nomImage: string | undefined ="";

}
