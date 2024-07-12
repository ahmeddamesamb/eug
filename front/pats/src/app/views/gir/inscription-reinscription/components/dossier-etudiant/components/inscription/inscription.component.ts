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
import { InscriptionAdministrativeFormModel } from '../../../../models/inscription-administrative-form-model';
import { InscriptionAdministrativeFormService } from '../../../../services/inscription-administrative-form.service';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { Observable } from 'rxjs'; 
import {TypeadmissionService} from '../../../../../parametrage/components/typeadmission/services/typeadmission.service';
import {TypeadmissionModel } from '../../../../../parametrage/components/typeadmission/models/typeadmission-model';
import { SpecialiteModel} from '../../../../../parametrage/components/specialite/models/specialite-model';
import {  SpecialiteServiceService} from '../../../../../parametrage/components/specialite/services/specialite-service.service';


@Component({
  selector: 'app-inscription',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.scss'
})
export class InscriptionComponent {

  inscription: InscriptionAdministrativeFormModel = {
    id: 0,
    inscriptionPrincipaleYN: true,
    inscriptionAnnuleeYN: false,
    exonereYN: true, 
    paiementFraisOblYN: true,
    paiementFraisIntegergYN: true, 
    certificatDelivreYN: true,
    dateChoixFormation: null,
    dateValidationInscription: null,
    inscriptionAdministrative: {
      id: 0
    },
    formation: {
      id: 0
    },
    ufr: {
      id:0
    },
    typeAdmission:{
      id:0
    }
  }

  customStylesValidated = false;
  id: number | undefined ;
  inscriptionForm: FormGroup;
  niveaux: NiveauModel[] = [];
  ufrs : UfrModel[] = [];
  specialite : SpecialiteModel[]= [];

  typesAdmissions : TypeadmissionModel[] = [];

 

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private niveauService: NiveauService,
    private ufrService: UfrServiceService,
    private inscriptionService: InscriptionAdministrativeFormService,
    private alertService: AlertServiceService,
    private typeAdmissionService: TypeadmissionService,
    private specialiteServiceService : SpecialiteServiceService

  
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
    this.loadTypeAdmissions();
    this.loadSpecialite();
    
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id && !isNaN(Number(id))) {
        this.id = Number(id);
        this.loadInscription(this.id);
      }
    });
   
  }

  loadInscription(id: number) {
    this.inscriptionService. getIafById(id).subscribe({
      next: (data) => {
        this.inscription = data;
        console.log("ici", this.inscription);
        this.initializeForm(data);
      },
      error: (err) => {
        console.error(err);
        //this.alertService.showToast("Erreur", "Impossible de charger l'inscription", "danger");
      }
    });
  }

  
  initializeForm(inscription: InscriptionAdministrativeFormModel) {
    this.inscriptionForm.patchValue({
    
    });
    console.log('Form values after initialization:', this.inscriptionForm.value);
  }


  
  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }



  onReset() {
    this.inscriptionForm.reset();
    this.customStylesValidated = false;
  }
  loadNiveaux() {
    this.niveauService.getNiveauList()
    .subscribe({
      next: (data) => {
        this.niveaux = data;
        console.log("Niveaux:  " + this.niveaux);
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
          console.log("UFRs: "+ this.ufrs);
        },
        error: (err) => {
          console.error("Erreur lors du chargement des UFRs:", err);
        }
      });
  }

  loadTypeAdmissions(){
    this.typeAdmissionService.getTypeAdmissionList()
    .subscribe({
      next: (data) => {
        this.typesAdmissions = data;
        console.log("UFRs: "+ this.typesAdmissions);
      },
      error: (err) => {
        console.error("Erreur lors du chargement des types d'admission", err);
      }
    });
  }
  loadSpecialite(){
    this.specialiteServiceService.getSpecialiteList()
    .subscribe({
      next: (data) => {
        this.specialite = data;
        console.log("Specialite: "+ this.specialite);
      },
      error: (err) => {
        console.error("Erreur lors du chargement des spécialités", err);
      }
    });
  }



  onSubmit() {
    if (this.inscriptionForm.valid) {
      const formValue = this.inscriptionForm.value;
      
      // Vérification des valeurs sélectionnées
      if (!formValue.UFR || !formValue.filiere || !formValue.niveau || !formValue.typeAdmission) {
        this.alertService.showToast("Erreur", "Veuillez sélectionner tous les champs requis", "warning");
        return;
      }
  
      const inscription: InscriptionAdministrativeFormModel = {
        ...this.inscription,
        id: this.id || 0,
        inscriptionPrincipaleYN: true,
        inscriptionAnnuleeYN: false,
        exonereYN: true,
        paiementFraisOblYN: true,
        paiementFraisIntegergYN: true,
        certificatDelivreYN: true,
        dateChoixFormation: new Date(),
        dateValidationInscription: new Date(),
        inscriptionAdministrative: {
          id: this.id || 0
        },
        formation: {
          id: Number(formValue.filiere)
        },
        ufr: {
          id: Number(formValue.UFR)
        },
        niveau: {
          id: Number(formValue.niveau)
        },
        typeAdmission: {
          id: Number(formValue.typeAdmission)
        }
      };
  
      console.log('ID avant envoi:', this.id);
      console.log('Inscription avant envoi:', JSON.stringify(inscription));
  
      let operation: Observable<InscriptionAdministrativeFormModel>;
      if (this.id) {
        operation = this.inscriptionService.updateIaf(this.id, inscription);
      } else {
        operation = this.inscriptionService.createIaf(inscription);
      }
  
      operation.subscribe({
        next: (data) => {
          const action = this.id ? "Modification" : "Création";
          const message = `${action} de l'inscription administrative réussie`;
          this.alertService.showToast(action, message, "success");
          //this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          console.error(`Erreur lors de la ${this.id ? 'modification' : 'création'}:`, err);
          let errorMessage = `Une erreur est survenue lors de la ${this.id ? 'modification' : 'création'}`;
        
          if (err.status === 400 && err.error) {
            errorMessage = err.error.detail || err.error.title || "Une erreur inattendue s'est produite.";
          }
          this.alertService.showToast("Erreur", errorMessage, "danger");
        }
      });
    } else {
      this.alertService.showToast("Erreur", "Veuillez remplir correctement tous les champs obligatoires", "warning");
      this.customStylesValidated = true; // Pour afficher les erreurs de validation
    }
  }
}
