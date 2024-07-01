import { Component } from '@angular/core';
import { MultiSelectComponent, MultiSelectOptgroupComponent, MultiSelectOptionComponent,CardComponent, CardHeaderComponent,CardBodyComponent } from '@coreui/angular-pro';

@Component({
  selector: 'app-formation-add-update',
  standalone: true,
  imports: [MultiSelectComponent,MultiSelectOptgroupComponent,MultiSelectOptionComponent,CardComponent, CardHeaderComponent,CardBodyComponent],
  templateUrl: './formation-add-update.component.html',
  styleUrl: './formation-add-update.component.scss'
})
export class FormationAddUpdateComponent {

  annuler(){

  }
  create(){
    
  }
  frontend = [
    {
      value: '1ère Année MPI',
      selected: true
    },
    {
      value: '1ère Année Sciences politique'
    },
    {
      value: '1ère Année SEG'
    },
    {
      value: '1ère Année Sciences juridiques'
    }
  ];

  backend = [
    {
      value: 'b1',
      label: 'Django'
    },
    {
      value: 'b2',
      label: 'Laravel',
      selected: true
    },
    {
      value: 'b3',
      label: 'Node.js'
    }
  ];
}
