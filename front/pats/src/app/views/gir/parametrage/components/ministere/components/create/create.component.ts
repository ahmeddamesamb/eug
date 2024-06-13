import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent,DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { MinistereServiceService } from '../../services/ministere-service.service';
import {MinistereModel} from '../../models/ministere-model';
import { Router } from '@angular/router';
import {  AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'



@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {
  ministere? :MinistereModel ;
  ministereForm: FormGroup | undefined;
  customStylesValidated = false;

  constructor( private ministereService: MinistereServiceService,private route:Router , private alertService:AlertServiceService){

  }

  ngOnInit() {
    this.ministereForm = new FormGroup({
      nomMinistere: new FormControl('', Validators.required),
      sigleMinistere: new FormControl('', Validators.required),
      dateDebut: new FormControl(null, Validators.required)
    });
  }

  onSubmit1() {

    if (this.ministereForm!.valid) {
      this.customStylesValidated = true;
      this.ministere = this.ministereForm!.value;
      /* if (this.ministere && this.ministere.dateDebut instanceof Date) {
        const transformedDate = this.formatDate(this.ministere.dateDebut);
        if (transformedDate !== null) {
          this.ministere.dateDebut = transformedDate;
        }
      } */
      this.ministereService.createMinistere(this.ministere!).subscribe({
        
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Creation","Creation du ministere avec succes","success");
          //this.addToast(true);
          this.route.navigate(['/gir/parametrage/ministere/view',data.id])
        },
        error: (err) => {
          this.alertService.showToast("Creation","Echec de creation du ministere","danger");
        }
      });
      console.log('Données du formulaire :', this.ministereForm!.value);
    } else {
      console.log('Formulaire invalide');
    }
  }

  onReset1() {
    this.ministereForm!.reset();
    this.customStylesValidated = false;
  }

  

}
