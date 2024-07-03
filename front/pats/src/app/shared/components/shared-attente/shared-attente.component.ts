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
  @Input() public view: boolean | undefined =false;
  @Input() public update: boolean | undefined =false;
  @Input() public delete: boolean | undefined =false;
  @Input() public formationAutorise: boolean | undefined =false;
  @Input() public invalideFormation: boolean | undefined =false;

}
