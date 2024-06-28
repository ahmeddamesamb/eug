import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-etat-validation',
  standalone: true,
  imports: [],
  templateUrl: './etat-validation.component.html',
  styleUrl: './etat-validation.component.scss'
})
export class EtatValidationComponent {
  @Input() public etat: boolean | undefined = false;
  
  @Input() public label: string | undefined = '';


}
