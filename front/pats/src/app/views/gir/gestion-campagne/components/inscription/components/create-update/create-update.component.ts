import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import { InscriptionModel } from '../../models/inscription-model';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { InscriptionService } from '../../../../services/inscription.service';
import { CampagneListComponent } from '../../../campagnes/components/campagne-list/campagne-list.component';
import { Observable, map } from 'rxjs';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { CampagneService } from '../../../campagnes/services/campagne.service';
import { CampagneModel } from '../../../campagnes/models/campagne-model';
import { AnneeAcademiqueService } from '../../../../../parametrage/components/annee-academique/services/annee-academique.service';
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
    libelle: '',
    dateDebut: '',
    dateFin: '',
    dateForclos: '',
    campagne: '',
    formation: '',
    anneeAcademique: ''
  };

  customStylesValidated = false;
  id: number | undefined;
  inscriptionForm: FormGroup;
  campagnes: CampagneModel[] = [];
  formations: any[] = [];
  annees: AnneeAcademiqueModel[] = [];

  constructor(
    private inscriptionService: InscriptionService,
    private anneeAcademiqueService: AnneeAcademiqueService,
    private campagneService: CampagneService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertServiceService
  ) {
    this.inscriptionForm = new FormGroup({
      libelle: new FormControl('', Validators.required),
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
        this.id = Number(id);
        this.loadInscription(this.id);
      }
    });
  }

  loadInscription(id: number) {
    this.inscriptionService.getInscriptionById(id).subscribe({
      next: (data) => {
        this.inscription = data;
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
      libelle: inscription.libelle || '',
      dateDebut: inscription.dateDebut || null,
      dateFin: inscription.dateFin || null,
      dateForclos: inscription.dateForclos || null,
      campagne: inscription.campagne instanceof Object ? inscription.campagne.id : inscription.campagne,
      formation: inscription.formation,
      anneeAcademique: inscription.anneeAcademique instanceof Object ? inscription.anneeAcademique.id : inscription.anneeAcademique,
    });
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
    // Remplacez ceci par un appel à votre service de formations
    this.formations = [
      { id: 1401, nom: 'Formation 1' },
      { id: 2, nom: 'Formation 2' },
    ];
  }

  formatDates(formValue: any): any {
    const formatted = { ...formValue };
    ['dateDebut', 'dateFin', 'dateForclos'].forEach(dateField => {
      if (formValue[dateField]) {
        const date = new Date(formValue[dateField]);
        formatted[dateField] = this.formatDate(date);
      }
    });

    // Conversion des IDs en objets
    ['campagne', 'formation', 'anneeAcademique'].forEach(field => {
      if (formatted[field]) {
        formatted[field] = { id: formatted[field] };
      }
    });

    return formatted;
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

  onSubmit1() {
    if (this.inscriptionForm.valid) {
      this.customStylesValidated = true;

      const formattedData = this.formatDates(this.inscriptionForm.value);
      console.log('Données formatées avant envoi:', JSON.stringify(formattedData));

      const inscription: InscriptionModel = {
        id: this.id ?? undefined,  // Ne pas envoyer d'ID pour une nouvelle inscription
        ...formattedData
      };

      const operation = this.id ? 'update' : 'create';
      const service = this.id 
        ? this.inscriptionService.updateInscription(this.id, inscription)
        : this.inscriptionService.createInscription(inscription);

      service.subscribe({
        next: (data) => {
          const message = operation === 'update' 
            ? "Mise à jour de l'inscription avec succès"
            : "Programmation d'inscription avec succès";
          this.alertService.showToast(operation === 'update' ? "Mise à jour" : "Création", message, "success");
          this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          console.error('Erreur lors de l\'opération:', err);
          this.alertService.showToast("Erreur", "Une erreur est survenue lors de l'opération", "danger");
        }
      });
    } else {
      this.alertService.showToast("Erreur", "Veuillez remplir correctement tous les champs obligatoires", "warning");
    }
  }
}