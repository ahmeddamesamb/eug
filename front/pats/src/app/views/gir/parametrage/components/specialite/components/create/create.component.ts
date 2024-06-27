import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { SpecialiteServiceService } from '../../services/specialite-service.service';
import { MentionServiceService } from '../../../mention/services/mention-service.service';
import { SpecialiteModel } from '../../models/specialite-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import { MentionModel } from '../../../mention/models/mention-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent,
     DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective,
      FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent,
       FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,
       DatePipe,DatePickerComponent_1,ToasterComponent],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {

  specialite :SpecialiteModel = {
    nomSpecialites: '',
    sigleSpecialites: '',
    specialiteParticulierYN:0,
    specialitesPayanteYN:0,
    mention : {
      id: 0
    } 
  }; 
  specialiteForm: FormGroup;
  customStylesValidated = false;
  mentionList : MentionModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private specialiteService: SpecialiteServiceService, private mentionService: MentionServiceService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.specialiteForm = new FormGroup({
      nomSpecialites: new FormControl('', Validators.required),
      sigleSpecialites: new FormControl('', Validators.required),
      mention: new FormControl('', Validators.required),
      specialiteParticulierYN: new FormControl(0),
      specialitesPayanteYN: new FormControl(0),

    });
  }

  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{
        this.specialiteService.getSpecialiteById(parseInt(id)).subscribe(
          (data) => {
            this.specialite = data;
            this.id = parseInt(id);
            this.initializeForm(this.specialite); 
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }
    
    this.getListeMention();
  }

  initializeForm(specialite: SpecialiteModel) {
    this.specialiteForm.setValue({
      nomSpecialites: specialite.nomSpecialites || '',
      sigleSpecialites: specialite.sigleSpecialites || '',
      mention : specialite.mention?.id || 0,
      specialiteParticulierYN : specialite.specialiteParticulierYN || 0,
      specialitesPayanteYN : specialite.specialitesPayanteYN || 0,
    });
  }

  //Pour le select du formulaire
  getListeMention(){
    this.mentionService.getMentionList().subscribe({
      next: (data) => {
        this.mentionList = data;
      },
      error: (err) => {
      }
    });
  }

  onSubmit1() {

    if (this.specialiteForm!.valid) {
      this.customStylesValidated = true;
      var idMention = this.specialiteForm!.get('mention')?.value;
      if (idMention) {
          if (this.specialite && this.specialite.mention) {
              this.specialite.nomSpecialites = this.specialiteForm!.get('nomSpecialites')?.value; 
              this.specialite.sigleSpecialites = this.specialiteForm!.get('sigleSpecialites')?.value;
              this.specialite.specialiteParticulierYN = this.specialiteForm!.get('specialiteParticulierYN')?.value ? 1 : 0;
              this.specialite.specialitesPayanteYN = this.specialiteForm!.get('specialitesPayanteYN')?.value ? 1 : 0; 
              this.specialite.mention.id = parseInt(this.specialiteForm!.get('mention')?.value) ; 
          }
      }
      if(this.id !=null){
        //Si c'est une mis a jour
        this.specialiteService.updateSpecialite(Number(this.specialite.id), this.specialite).subscribe({
          next: (data) => {
            console.log(data);

            this.alertService.showToast("Mis a jour","Mis a jour de l'spécialité avec succes","success");
            this.router.navigate(['/gir/parametrage/specialite/view', data.id]);
          },
          error: (err) => {
            console.log(err);

            this.alertService.showToast("Mis a jour","Echec de la Mis a jour de l'spécialité","danger");
          }
        });
      }else{
        //Si c'est une creation
        this.specialiteService.createSpecialite(this.specialite!).subscribe({ 
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Creation","Creation de l'spécialité avec succes","success");
            this.router.navigate(['/gir/parametrage/specialite/view',data.id])
          },
          error: (err) => {

            this.alertService.showToast("Creation","Echec de creation de l'spécialité","danger");
          }
        });

      }
      
      //console.log('Données du formulaire :', this.specialite);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

}
