import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import { DatePipe } from '@angular/common';
import {NiveauService} from '../../../../../parametrage/components/niveau/services/niveau.service';
import { NiveauModel } from 'src/app/views/gir/parametrage/components/niveau/models/niveau-model';
import { UfrServiceService } from 'src/app/views/gir/parametrage/components/ufr/services/ufr-service.service';
import { UfrModel } from 'src/app/views/gir/parametrage/components/ufr/models/ufr-model';

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
  niveaux: NiveauModel[] = [];
  ufrs : UfrModel[] = [];

 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private niveauService: NiveauService,
    private ufrService: UfrServiceService,
  
  ) {
    this.inscriptionForm = new FormGroup({
      UFR: new FormControl('', Validators.required),
      filiere: new FormControl('', Validators.required),
      niveau: new FormControl('', Validators.required),
      typeAdmission: new FormControl('', Validators.required)
    });
  }

  ngOnInit() {
    this.loadNiveaux();
    this.loadUfrs();
   
  }


  loadNiveaux() {
    this.niveauService.getNiveauList()
    .subscribe({
      next: (data) => {
        this.niveaux = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des niveaux:", err);
      }
    });
  }
    loadUfrs(){
      this.ufrService.getUfrList()
      .subscribe({
        next: (data) => {
          this.ufrs = data;
        },
        error: (err) => {
          console.error("Erreur lors du chargement des niveaux:", err);
        }
      });
  }






  onSubmit() {
  }
}
