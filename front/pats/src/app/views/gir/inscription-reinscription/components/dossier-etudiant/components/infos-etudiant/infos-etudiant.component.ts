import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import {
  AccordionButtonDirective,
  AccordionComponent,
  AccordionItemComponent,
  TemplateIdDirective
} from '@coreui/angular-pro';


@Component({
  selector: 'app-infos-etudiant',
  standalone: true,
  imports: [
    AccordionComponent,
    AccordionItemComponent,
    TemplateIdDirective,
    AccordionButtonDirective
  ],
  templateUrl: './infos-etudiant.component.html',
  styleUrl: './infos-etudiant.component.scss'
})
export class InfosEtudiantComponent {


  constructor(

  
  ) { }

  

}
