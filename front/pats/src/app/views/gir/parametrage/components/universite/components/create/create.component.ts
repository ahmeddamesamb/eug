import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { UniversiteService } from '../../services/universite.service';
import { MinistereServiceService } from '../../../ministere/services/ministere-service.service';
import { UniversiteModel } from '../../models/universite-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import { MinistereModel } from '../../../ministere/models/ministere-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {
  universite :UniversiteModel = {
    nomUniversite: '',
    sigleUniversite: '',
    ministere : {
      id: 0
    } 
  }; 
  universiteForm: FormGroup;
  customStylesValidated = false;
  ministereList : MinistereModel[] = []; //Pour le select du formulaire
  id: number | undefined ;


  constructor( private universiteService: UniversiteService,private ministereService: MinistereServiceService , private router: ActivatedRoute ,private route:Router, private alertService: AlertServiceService ){
    this.universiteForm = new FormGroup({
      nomUniversite: new FormControl('', Validators.required),
      sigleUniversite: new FormControl('', Validators.required),
      ministere: new FormControl('', Validators.required),
    });
  }

  ngOnInit() {
    const id: Observable<string> = this.router.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{
        this.universiteService.getUniversiteById(parseInt(id)).subscribe(
          (data) => {
            this.universite = data;
            this.id = parseInt(id);
            this.initializeForm(this.universite);
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }
    this.getListeMinistere();
  }

  initializeForm(universite: UniversiteModel) {
    this.universiteForm.setValue({
      nomUniversite: universite.nomUniversite || '',
      sigleUniversite: universite.sigleUniversite || '',
      ministere : universite.ministere?.id || 0 
    });
  }

  //Pour le select du formulaire
  getListeMinistere(){
    this.ministereService.getMinistereList().subscribe({
      next: (data) => {
        this.ministereList = data;
      },
      error: (err) => {
      }
    });
  }

  onSubmit1() {

    if (this.universiteForm!.valid) {
      this.customStylesValidated = true;
      //this.universite = this.universiteForm!.value; 
      var idMinistere = this.universiteForm!.get('ministere')?.value;
      if (idMinistere) {
          if (this.universite && this.universite.ministere) {
              this.universite.nomUniversite = this.universiteForm!.get('nomUniversite')?.value; 
              this.universite.sigleUniversite = this.universiteForm!.get('sigleUniversite')?.value; 
              this.universite.ministere.id = parseInt(this.universiteForm!.get('ministere')?.value) ; 
          }
      }

      if(this.id !=null){
        //Si c'est une mis a jour
        


        this.universiteService.updateUniversite(Number(this.id), this.universite).subscribe({
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Mis a jour","Mis a jour de l'université avec success","success");
            this.route.navigate(['/gir/parametrage/universite/view', data.id]);
          },
          error: (err) => {
            this.alertService.showToast("Mis a jour","Erreur sur la mis a jour de l'université","danger");
          }
        });
        
      }else{
        //Si c'est une creation

        this.universiteService.createUniversite(this.universite!).subscribe({
        
          next: (data) => {
            console.log(data);
            //this.addToast(true);
            this.alertService.showToast("Creation","Creation université avec succes","success");
            this.route.navigate(['/gir/parametrage/universite/view',data.id])
          },
          error: (err) => {
            //this.addToast(false);
            this.alertService.showToast("Creation","Echec de creation de l'université","danger");
          }
        });
        
        

      }


      
      
    } else {
      console.log('Formulaire invalide');
    }
  }

  annuler(){
    this.route.navigate(['/gir/parametrage/universite/attente']);
  }

  onReset1() {
    this.universiteForm!.reset();
    this.customStylesValidated = false;
  }



}
