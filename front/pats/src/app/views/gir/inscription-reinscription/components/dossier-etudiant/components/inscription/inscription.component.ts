import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AnneeAcademiqueService } from '../../../../../parametrage/components/annee-academique/services/annee-academique.service';
import { FormationService } from 'src/app/views/gir/parametrage/components/formation/services/formation.service';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-inscription',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.scss'
})
export class InscriptionComponent {



  customStylesValidated = false;
  id: number | undefined ;
  inscriptionForm: FormGroup;

 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertServiceService,
    private formationService: FormationService
  ) {
    this.inscriptionForm = new FormGroup({
      UFR: new FormControl('', Validators.required),
      filiere: new FormControl('', Validators.required),
      niveau: new FormControl('', Validators.required),
      typeAdmission: new FormControl('', Validators.required)
    });
  }

  ngOnInit() {
   
  }







  onSubmit() {
  }
}
