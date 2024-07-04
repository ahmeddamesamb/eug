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
    formation: ''
  };

  customStylesValidated = false;
  id: number | undefined;
  inscriptionForm: FormGroup;
  campagnes: CampagneModel[] = []; 
  formations: any[] = [];



  constructor( private inscriptionService: InscriptionService, private campagneService: CampagneService, private route: ActivatedRoute, private router:Router , private alertService:AlertServiceService){
    this.inscriptionForm = new FormGroup({
      libelle: new FormControl(null, Validators.required),
      dateDebut: new FormControl(null, Validators.required),
      dateFin: new FormControl(null, Validators.required),
      dateForclos: new FormControl(null, Validators.required),
      campagne: new FormControl(null, Validators.required),
      formation: new FormControl(null, Validators.required)
      
    });
  }
  ngOnInit() {
    this.loadCampagnes();
    this.loadFormations();

    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{

        this.inscriptionService.getInscriptionById(parseInt(id)).subscribe(
          (data) => {
            this.inscription = data;
            console.log(this.inscription);
            this.id = parseInt(id);
            this.initializeForm(data);
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }

}
initializeForm(inscription: InscriptionModel) {
  this.inscriptionForm.setValue({
    dateDebut: inscription.dateDebut || null,
    dateFin: inscription.dateFin || null,
    dateForclos: inscription.dateForclos || null,
    campagne: inscription.campagne,
    formation: inscription.formation
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

loadFormations() {
  this.formations = [
    { id: 1, nom: 'Formation 1' },
    { id: 2, nom: 'Formation 2' },
  ];
}

onSubmit1() {

  if (this.inscriptionForm!.valid) {
    this.customStylesValidated = true;
    


    if(this.id !=null){
      //Si c'est une mis a jour

      this.inscription = { ...this.inscriptionForm.value, id: Number(this.id) };      // Convertir la valeur booléenne en entier
      this.inscriptionService.updateInscription(this.id, this.inscription).subscribe({
        next: (data) => {
          console.log(data);
          //this.addToast(true);
          this.alertService.showToast("Création","Mise à jour du ministère avec succès","success");
          this.router.navigate(['/gir/gestion-campagne/inscription/view', data.id]);
        },
        error: (err) => {
          //console.log(err);
          //this.addToast(false);
          this.alertService.showToast("Création","Échec de la mise à jour du ministère","danger");
        }
      });

    }else{
      //Si c'est une creation
      this.inscription = this.inscriptionForm!.value;
      this.inscriptionService.createInscription(this.inscription!).subscribe({
        
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Création","Programmation d'inscription  avec succes","success");
          //this.addToast(true);
          this.router.navigate(['/gir/gestion-campagne/inscription/view',data.id])
        },
        error: (err) => {
          this.alertService.showToast("Création","Echec de la programmation d'inscription","danger");
        }
      });


    }



    console.log('Données du formulaire :', this.inscriptionForm!.value);
  } else {
    console.log('Formulaire invalide');
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
