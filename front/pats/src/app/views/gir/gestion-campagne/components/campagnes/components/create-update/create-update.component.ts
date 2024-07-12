import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CampagneService } from '../../../campagnes/services/campagne.service';
import { AnneeAcademiqueService } from '../../../../../parametrage/components/annee-academique/services/annee-academique.service';
import { FormationService } from 'src/app/views/gir/parametrage/components/formation/services/formation.service';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DocsExampleComponent } from '@docs-components/public-api';
import { DatePipe } from '@angular/common';
import { Observable } from 'rxjs';
import { CampagneModel } from '../../../campagnes/models/campagne-model';
import { AnneeAcademiqueModel } from 'src/app/views/gir/parametrage/components/annee-academique/models/AnneeAcademiqueModel';

@Component({
  selector: 'app-create-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, 
    CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, 
    ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1],
  templateUrl: './create-update.component.html',
  styleUrl: './create-update.component.scss'
})
export class CreateUpdateComponent {

  campagne: CampagneModel = {
    id: 0,
    libelleCampagne: '',
    dateDebut: '',
    dateFin: '',
    libelleAbrege: '',
    actifYN: true
    };

    customStylesValidated = false;
  id: number | undefined ;

  campagneForm: FormGroup;

  constructor(
    private campagneService: CampagneService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertServiceService,
  ) {
    this.campagneForm = new FormGroup({
      libelleCampagne: new FormControl('', Validators.required),
      dateDebut: new FormControl(null, Validators.required),
      dateFin: new FormControl(null, Validators.required),
      libelleAbrege: new FormControl('', Validators.required)
    });
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id && !isNaN(Number(id))) {
        this.id = Number(id);
        this.loadCampagne(this.id);
      }
    });
  }

  loadCampagne(id: number) {
    this.campagneService.getCampagneById(id).subscribe({
      next: (data) => {
        this.campagne = data;
        console.log("ici", this.campagne);
        this.initializeForm(data);
      },
      error: (err) => {
        console.error(err);
        this.alertService.showToast("Erreur", "Impossible de charger la campagne", "danger");
      }
    });
  }
  initializeForm(campagne: CampagneModel) {
    this.campagneForm.patchValue({
      libelleCampagne: campagne.libelleCampagne || '',
      dateDebut: campagne.dateDebut || '',
      dateFin: campagne.dateFin || '',
      libelleAbrege: campagne.libelleAbrege || '',
    });
    console.log('Form values after initialization:', this.campagneForm.value);
  }

  onSubmit1() {

    if (this.campagneForm!.valid) {
      this.customStylesValidated = true;
      


      if(this.id !=null){

        this.campagne = { ...this.campagneForm.value, id: Number(this.id)};      // Convertir la valeur booléenne en entier
        this.campagneService.updateCampagne(this.id, this.campagne).subscribe({
          next: (data) => {
            console.log(data);
            //this.addToast(true);
            this.alertService.showToast("Création","Mise à jour d'une campagne' avec succès","success");
            this.router.navigate(['/gir/gestion-campagne/campagnes/view', data.id]);
          },
          error: (err) => {
            //console.log(err);
            //this.addToast(false);
            this.alertService.showToast("Création","Échec de la mise à jour d'une campagne'","danger");
          }
        });

      }else{
        //Si c'est une creation
        this.campagne = this.campagneForm!.value;
        this.campagneService.createCampagne(this.campagne!).subscribe({
          
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Création","Creation d'une campagne avec succes","success");
            //this.addToast(true);
            this.router.navigate(['/gir/gestion-campagne/campagnes/view',data.id])
          },
          error: (err) => {
            this.alertService.showToast("Création","Echec de création d'une campagne'","danger");
          }
        });


      }



      console.log('Données du formulaire :', this.campagneForm!.value);
    } else {
      console.log('Formulaire invalide');
    }
  }
  annuler() {
    this.router.navigate(['/gir/gestion-campagne/campagnes/attente']);
  }

  onReset() {
    this.campagneForm.reset();
    this.customStylesValidated = false;
  }


}
