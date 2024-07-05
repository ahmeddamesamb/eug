import { Component } from '@angular/core';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, MultiSelectComponent as MultiSelectComponent_1,MultiSelectOptgroupComponent,MultiSelectOptionComponent, } from '@coreui/angular-pro';
import { FormationModel } from '../../../parametrage/components/formation/models/formation-model';
import { FormControl, FormControlDirective, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormationService } from '../../../parametrage/components/formation/services/formation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';

@Component({
  selector: 'app-formation-add-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective,
    CardComponent, CardHeaderComponent, CardBodyComponent, ReactiveFormsModule,
    FormsModule, FormDirective, FormLabelDirective, FormFeedbackComponent,
    InputGroupComponent, InputGroupTextDirective,
    FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePickerComponent_1,ToasterComponent, MultiSelectComponent_1, MultiSelectOptionComponent, MultiSelectOptgroupComponent],
  templateUrl: './formation-add-update.component.html',
  styleUrl: './formation-add-update.component.scss'
})
export class FormationAddUpdateComponent {

  formation :FormationModel = {
    libelleDiplome: '',
  }; 

  formationForm: FormGroup;
  customStylesValidated = false;
  formationList : FormationModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private formationService: FormationService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.formationForm = new FormGroup({
      formations: new FormControl([], Validators.required),
    });
  }
  annuler(){

  }
  create(){
    
  }

  onSubmit1(){
    
  }
  


 
}
