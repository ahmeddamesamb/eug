import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { InscriptionService } from '../../../../services/inscription.service';
import { CampagneService } from '../../../campagnes/services/campagne.service';
import { AnneeAcademiqueService } from '../../../../../parametrage/components/annee-academique/services/annee-academique.service';
import { FormationService } from 'src/app/views/gir/parametrage/components/formation/services/formation.service';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import { DatePipe } from '@angular/common';
import { Observable } from 'rxjs';
import { InscriptionModel } from '../../models/inscription-model';
import { CampagneModel } from '../../../campagnes/models/campagne-model';
import { FormationModel } from 'src/app/views/gir/parametrage/components/formation/models/formation-model';
import { AnneeAcademiqueModel } from 'src/app/views/gir/parametrage/components/annee-academique/models/AnneeAcademiqueModel';

@Component({
  selector: 'app-create-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1],
  templateUrl: './create-update.component.html',
  styleUrl: './create-update.component.scss'
})
export class CreateUpdateComponent implements OnInit {

  inscription: InscriptionModel = {
    id: 0,
    libelleProgrammation: '',
    dateDebutProgrammation: '',
    dateFinProgrammation: '',
    ouvertYN: false,
    emailUser: '',
    dateForclosClasse: '',
    forclosClasseYN: false,
    actifYN: true,
    anneeAcademique: {
      id: 0,
     
    },
    formation: {
      id: 0
    },
    campagne: {
      id: 0,
     
    }
  };

  customStylesValidated = false;
  id: number | undefined ;

  inscriptionForm: FormGroup;
  campagnes: CampagneModel[] = [];
  formations: FormationModel[] = [];
  annees: AnneeAcademiqueModel[] = [];

  constructor(
    private inscriptionService: InscriptionService,
    private anneeAcademiqueService: AnneeAcademiqueService,
    private campagneService: CampagneService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertServiceService,
    private formationService: FormationService
  ) {
    this.inscriptionForm = new FormGroup({
      libelleProgrammation: new FormControl('', Validators.required),
      dateDebutProgrammation: new FormControl(null, Validators.required),
      dateFinProgrammation: new FormControl(null, Validators.required),
      dateForclosClasse: new FormControl(null, Validators.required),
      campagne: new FormControl(null, Validators.required),
      formation: new FormControl(null, Validators.required),
      anneeAcademique: new FormControl(null, Validators.required)
    });
  }

  ngOnInit() {
    this.loadCampagnes();
    this.loadFormations();
    this.loadAnneeAcademiques();

    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id && !isNaN(Number(id))) {
        this.id = Number(id);
        this.loadInscription(this.id);
      }
    });
  }

  loadInscription(id: number) {
    this.inscriptionService.getInscriptionById(id).subscribe({
      next: (data) => {
        this.inscription = data;
        console.log("ici", this.inscription);
        this.initializeForm(data);
      },
      error: (err) => {
        console.error(err);
        this.alertService.showToast("Erreur", "Impossible de charger l'inscription", "danger");
      }
    });
  }



  initializeForm(inscription: InscriptionModel) {
    this.inscriptionForm.patchValue({
      libelleProgrammation: inscription.libelleProgrammation || '',
      dateDebutProgrammation: inscription.dateDebutProgrammation || '',
      dateFinProgrammation: inscription.dateFinProgrammation || '',
      dateForclosClasse: inscription.dateForclosClasse || '',
      campagne: inscription.campagne?.id || null,
      formation: inscription.formation?.id || null,
      anneeAcademique: inscription.anneeAcademique?.id || null
    });
    console.log('Form values after initialization:', this.inscriptionForm.value);
  }

  loadCampagnes() {
    this.campagneService.getCampagneList().subscribe({
      next: (data) => {
        this.campagnes = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des campagnes:", err);
      }
    });
  }

  loadAnneeAcademiques() {
    this.anneeAcademiqueService.getAnneeAcademiqueList().subscribe({
      next: (data) => {
        this.annees = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des années académiques:", err);
      }
    });
  }

  loadFormations() {
    this.formationService.getFormationList().subscribe({
      next: (data) => {
        this.formations = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des formations:", err);
      }
    });
  }

  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  annuler() {
    this.router.navigate(['/gir/gestion-campagne/inscription/attente']);
  }

  onReset() {
    this.inscriptionForm.reset();
    this.customStylesValidated = false;
  }



  onSubmit() {
    if (this.inscriptionForm.valid) {
      const formValue = this.inscriptionForm.value;
      
      // Vérification des valeurs sélectionnées
      if (!formValue.anneeAcademique || !formValue.formation || !formValue.campagne) {
        this.alertService.showToast("Erreur", "Veuillez sélectionner tous les champs requis", "warning");
        return;
      }
  
      const inscription: InscriptionModel = {
        id: this.id, // Ajoutez cette ligne pour inclure l'ID lors de la mise à jour
        libelleProgrammation: formValue.libelleProgrammation,
        dateDebutProgrammation: this.formatDate(formValue.dateDebutProgrammation),
        dateFinProgrammation: this.formatDate(formValue.dateFinProgrammation),
        ouvertYN: true,
        emailUser: formValue.emailUser || '',
        dateForclosClasse: this.formatDate(formValue.dateForclosClasse),
        forclosClasseYN: false,
        actifYN: true,
        anneeAcademique: { id: Number(formValue.anneeAcademique) },
        formation: { id: Number(formValue.formation) },
        campagne: { id: Number(formValue.campagne) }
      };
  
      console.log('ID avant envoi:', this.id);
      console.log('Inscription avant envoi:', JSON.stringify(inscription));
  
      let operation: Observable<InscriptionModel>;
      if (this.id) {
        operation = this.inscriptionService.updateInscription(this.id, inscription);
      } else {
        operation = this.inscriptionService.createInscription(inscription);
      }
  
      operation.subscribe({
        next: (data) => {
          const action = this.id ? "Modification" : "Création";
          const message = `${action} de l'inscription réussie`;
          this.alertService.showToast(action, message, "success");
          this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          console.error(`Erreur lors de la ${this.id ? 'modification' : 'création'}:`, err);
          let errorMessage = `Une erreur est survenue lors de la ${this.id ? 'modification' : 'création'}`;
        
          if (err.status === 400 && err.error) {
            switch (err.error.message) {
              case 'error.noFormationCampaignInPast':
                errorMessage = "On ne peut pas programmer une formation/campagne dans le passé.";
                break;
              case 'error.dateDebutNotPosteriorDateFin':
                errorMessage = "La date de début ne doit pas être postérieure à la date de fin.";
                break;
              default:
                errorMessage = err.error.detail || err.error.title || "Une erreur inattendue s'est produite.";
            }
          }
          this.alertService.showToast("Erreur", errorMessage, "danger");
        }
      });
    } else {
      this.alertService.showToast("Erreur", "Veuillez remplir correctement tous les champs obligatoires", "warning");
    }
  }
}