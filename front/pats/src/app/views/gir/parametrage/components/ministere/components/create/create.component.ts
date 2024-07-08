import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent,DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { MinistereServiceService } from '../../services/ministere-service.service';
import {MinistereModel} from '../../models/ministere-model';
import { ActivatedRoute, Router } from '@angular/router';
import {  AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'
import { map, Observable } from 'rxjs';



@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {
  ministere: MinistereModel = {
    id: 0,
    nomMinistere: '',
    sigleMinistere: '',
    dateDebut: '',
    dateFin: '',
    enCoursYN: false,
  };
  ministereForm: FormGroup;
  customStylesValidated = false;
  id: number | undefined ;

  constructor( private ministereService: MinistereServiceService, private route: ActivatedRoute, private router:Router , private alertService:AlertServiceService){
    this.ministereForm = new FormGroup({
      nomMinistere: new FormControl('', Validators.required),
      sigleMinistere: new FormControl('', Validators.required),
      dateDebut: new FormControl(null, Validators.required),
      dateFin: new FormControl(null),
      enCoursYN: new FormControl(1)
    });
  }

  ngOnInit() {

    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{

        this.ministereService.getMinistereById(parseInt(id)).subscribe(
          (data) => {
            this.ministere = data;
            console.log(this.ministere);
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

  initializeForm(ministere: MinistereModel) {
    this.ministereForm.setValue({
      nomMinistere: ministere.nomMinistere || '',
      sigleMinistere: ministere.sigleMinistere || '',
      dateDebut: ministere.dateDebut || null,
      dateFin: ministere.dateFin || null,
      enCoursYN: ministere.enCoursYN || 0
    });
  }

  onSubmit1() {

    if (this.ministereForm!.valid) {
      this.customStylesValidated = true;
      


      if(this.id !=null){
        //Si c'est une mis a jour
        const enCoursYN = this.ministereForm.value.enCoursYN ? 1 : 0;

        this.ministere = { ...this.ministereForm.value, id: Number(this.id), enCoursYN };      // Convertir la valeur booléenne en entier
        this.ministereService.updateMinistere(this.id, this.ministere).subscribe({
          next: (data) => {
            console.log(data);
            //this.addToast(true);
            this.alertService.showToast("Création","Mise à jour du ministère avec succès","success");
            this.router.navigate(['/gir/parametrage/ministere/view', data.id]);
          },
          error: (err) => {
            //console.log(err);
            //this.addToast(false);
            this.alertService.showToast("Création","Échec de la mise à jour du ministère","danger");
          }
        });

      }else{
        //Si c'est une creation
        this.ministere = this.ministereForm!.value;
        this.ministereService.createMinistere(this.ministere!).subscribe({
          
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Création","Creation du ministère avec succes","success");
            //this.addToast(true);
            this.router.navigate(['/gir/parametrage/ministere/view',data.id])
          },
          error: (err) => {
            this.alertService.showToast("Création","Echec de création du ministère","danger");
          }
        });


      }



      console.log('Données du formulaire :', this.ministereForm!.value);
    } else {
      console.log('Formulaire invalide');
    }
  }

  annuler(){
    this.router.navigate(['/gir/parametrage/ministere/attente']);
  }

  onReset1() {
    this.ministereForm!.reset();
    this.customStylesValidated = false;
  }

  

}
