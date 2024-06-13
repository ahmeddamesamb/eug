import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { UfrServiceService } from '../../services/ufr-service.service';
import { UniversiteService } from '../../../universite/services/universite.service';
import { UfrModel } from '../../models/ufr-model';
import { Router, ActivatedRoute } from '@angular/router';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';
import { map, Observable } from 'rxjs';
import { UniversiteModel } from '../../../universite/models/universite-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1,ToasterComponent],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {

  ufr :UfrModel = {
    libeleUfr: '',
    sigleUfr: '',
    systemeLMDYN:0,
    ordreStat:0,
    universite : {
      id: 0
    } 
  }; 
  ufrForm: FormGroup;
  customStylesValidated = false;
  universiteList : UniversiteModel[] = []; //Pour le select du formulaire
  id: number | undefined ;
  msg:any = "teste";
  constructor(private ufrService: UfrServiceService, private universiteService: UniversiteService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.ufrForm = new FormGroup({
      libeleUfr: new FormControl('', Validators.required),
      sigleUfr: new FormControl('', Validators.required),
      universite: new FormControl('', Validators.required),
      systemeLMDYN: new FormControl(0),
      ordreStat: new FormControl(0),

    });
  }


  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{
        this.ufrService.getUfrById(parseInt(id)).subscribe(
          (data) => {
            this.ufr = data;
            this.id = parseInt(id);
            this.initializeForm(this.ufr); 
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }
    
    this.getListeUniversite();
  }
  
  initializeForm(ufr: UfrModel) {
    this.ufrForm.setValue({
      libeleUfr: ufr.libeleUfr || '',
      sigleUfr: ufr.sigleUfr || '',
      universite : ufr.universite?.id || 0,
      systemeLMDYN : ufr.systemeLMDYN || 0,
      ordreStat : ufr.ordreStat || 0,
    });
  }

  //Pour le select du formulaire
  getListeUniversite(){
    this.universiteService.getUniversiteList().subscribe({
      next: (data) => {
        this.universiteList = data;
      },
      error: (err) => {


      }
    });
  }

  onSubmit1() {

    if (this.ufrForm!.valid) {
      this.customStylesValidated = true;
      var idUniversite = this.ufrForm!.get('universite')?.value;
      if (idUniversite) {
          if (this.ufr && this.ufr.universite) {
              this.ufr.libeleUfr = this.ufrForm!.get('libeleUfr')?.value; 
              this.ufr.sigleUfr = this.ufrForm!.get('sigleUfr')?.value;
              this.ufr.systemeLMDYN = this.ufrForm!.get('systemeLMDYN')?.value ? 1 : 0;
              this.ufr.ordreStat = this.ufrForm!.get('ordreStat')?.value ? 1 : 0; 
              this.ufr.universite.id = parseInt(this.ufrForm!.get('universite')?.value) ; 
          }
      }
      if(this.id !=null){
        //Si c'est une mis a jour
        this.ufrService.updateUfr(Number(this.ufr.id), this.ufr).subscribe({
          next: (data) => {
            console.log(data);

            this.alertService.showToast("Mis a jour","Mis a jour de l'Ufr avec succes","success");
            this.router.navigate(['/gir/parametrage/ufr/view', data.id]);
          },
          error: (err) => {
            console.log(err);

            this.alertService.showToast("Mis a jour","Echec de la Mis a jour de l'Ufr","danger");
          }
        });
      }else{
        //Si c'est une creation
        this.ufrService.createUfr(this.ufr!).subscribe({ 
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Creation","Creation Ufr avec succes","success");
            this.router.navigate(['/gir/parametrage/ufr/view',data.id])
          },
          error: (err) => {

            this.alertService.showToast("Creation","Echec de creation de l'Ufr","danger");
          }
        });

      }
      
      console.log('Données du formulaire :', this.ufr);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  annuler(){
    this.router.navigate(['/gir/parametrage/ufr/attente']);
  }

  onReset1() {
    this.ufrForm!.reset();
    this.customStylesValidated = false;
  }

}
