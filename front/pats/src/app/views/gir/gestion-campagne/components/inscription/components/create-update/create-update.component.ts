import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RowComponent, ColComponent, TextColorDirective, CardComponent,DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import {InscriptionModel} from '../../models/inscription-model';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import {InscriptionService} from '../../../../services/inscription.service';
import { CampagneListComponent } from '../../../campagnes/components/campagne-list/campagne-list.component';
import { Observable, map } from 'rxjs';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { CampagneService } from '../../../campagnes/services/campagne.service';
import { CampagneModel } from '../../../campagnes/models/campagne-model';
import { AnneeAcademiqueService} from '../../../../../parametrage/components/annee-academique/services/annee-academique.service';
import { AnneeAcademiqueModel } from 'src/app/views/gir/parametrage/components/annee-academique/models/AnneeAcademiqueModel';
@Component({
  selector: 'app-create-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1],
  templateUrl: './create-update.component.html',
  styleUrl: './create-update.component.scss'
})
export class CreateUpdateComponent {

  inscription: InscriptionModel = {
    id: 0,
    libelle:'',
    dateDebut: '',
    dateFin: '',
    dateForclos: '',
    campagne: '',
    formation: '',
    anneeAcademique:''
  };

  customStylesValidated = false;
  id: number | undefined;
  inscriptionForm: FormGroup;
  campagnes: CampagneModel[] = []; 
  formations: any[] = [];
  annees: AnneeAcademiqueModel[] = [];




  constructor( private inscriptionService: InscriptionService, private anneeAcademiqueService: AnneeAcademiqueService, private campagneService: CampagneService, private route: ActivatedRoute, private router:Router , private alertService:AlertServiceService){
    this.inscriptionForm = new FormGroup({
      libelle: new FormControl(null, Validators.required),
      dateDebut: new FormControl(null, Validators.required),
      dateFin: new FormControl(null, Validators.required),
      dateForclos: new FormControl(null, Validators.required),
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
        this.inscriptionService.getInscriptionById(Number(id)).subscribe(
          (data) => {
            this.inscription = data;
            console.log(this.inscription);
            this.id = Number(id);
            this.initializeForm(data);
          },
          (err) => {
            console.log(err);
            this.alertService.showToast("Erreur", "Impossible de charger l'inscription", "danger");
          }
        );
      }
    });
  }

initializeForm(inscription: InscriptionModel) {
  this.inscriptionForm.setValue({
    dateDebut: inscription.dateDebut || null,
    dateFin: inscription.dateFin || null,
    dateForclos: inscription.dateForclos || null,
    campagne: inscription.campagne,
    formation: inscription.formation,
    anneeAcademique: inscription.anneeAcademique || null,


  });
}

loadCampagnes() {
  console.log("GET CAMPAGNES");
  this.campagneService.getCampagneList().subscribe({
    next: (data) => {
      this.campagnes = data;
      console.log("CAMPAGNES:", this.campagnes.length);
      
      if (this.campagnes && this.campagnes.length > 0) {
        console.log("Libellés des campagnes:");
        this.campagnes.forEach(campagne => {
          console.log(campagne.libelleCampagne); 
        });
      } else {
        console.log("Aucune campagne trouvée");
      }
    },
    error: (err) => {
      console.error("Erreur lors du chargement des campagnes:", err);
    }
  });
}

loadAnneeAcademiques() {
  console.log("getAnneesAcamiques");
  this.anneeAcademiqueService.getAnneeAcademiqueList()
  .subscribe({
    next: (data) => {
      this.annees = data;
      console.log("AnneesAcademiques:", this.annees.length);
      
      if (this.annees && this.annees.length > 0) {
        console.log("Libellés des annees:");
        this.annees.forEach(annee => {
          console.log(annee.libelleAnneeAcademique); 
        });
      } else {
        console.log("Aucune année académique trouvée");
      }
    },
    error: (err) => {
      console.error("Erreur lors du chargement des années academiques:", err);
    }
  });
}


formatDates(formValue: any): any {
  const formatted = { ...formValue };
  ['dateDebut', 'dateFin', 'dateForclos'].forEach(dateField => {
    if (formValue[dateField]) {
      const date = new Date(formValue[dateField]);
      formatted[dateField] = this.formatDate(date);
    }
  });
  return formatted;
}

formatDate(date: Date): string {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}

loadFormations() {
  this.formations = [
    { id: 1, nom: 'Formation 1' },
    { id: 2, nom: 'Formation 2' },
  ];
}

onSubmit1() {
  if (this.inscriptionForm!.valid) {
    this.customStylesValidated = true;

    // Formater les dates
    const formattedData = this.formatDates(this.inscriptionForm!.value);

    if (this.id != null) {
      // Si c'est une mise à jour
      this.inscription = { ...formattedData, id: Number(this.id) };
      this.inscriptionService.updateInscription(this.id, this.inscription).subscribe({
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Mise à jour", "Mise à jour de l'inscription avec succès", "success");
          this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          console.error(err);
          this.alertService.showToast("Mise à jour", "Échec de la mise à jour de l'inscription", "danger");
        }
      });
    } else {
      // Si c'est une création
      this.inscription = formattedData;
      this.inscriptionService.createInscription(this.inscription!).subscribe({
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Création", "Programmation d'inscription avec succès", "success");
          this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          console.error(err);
          this.alertService.showToast("Création", "Échec de la programmation d'inscription", "danger");
        }
      });
    }

    console.log('Données du formulaire formatées :', formattedData);
  } else {
    console.log('Formulaire invalide');
    // Optionnel : Afficher un message d'erreur à l'utilisateur
    this.alertService.showToast("Erreur", "Veuillez remplir correctement tous les champs obligatoires", "warning");
  }
}

annuler(){
  this.router.navigate(['/gir/gestion-campagne/inscription/attente']);
}

onReset1() {
  this.inscriptionForm!.reset();
  this.customStylesValidated = false;
}


}
