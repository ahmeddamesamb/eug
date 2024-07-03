import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { EtudiantService } from '../services/etudiant.service';
import { EtudiantModel } from '../models/etudiant-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'
import { InformationPersonellesModel } from '../models/information-personelles-model';

@Component({
  selector: 'app-create-etudiant',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent,
    DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective,
     FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent,
      FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,
      DatePipe,DatePickerComponent_1,ToasterComponent],
  templateUrl: './create-etudiant.component.html',
  styleUrl: './create-etudiant.component.scss'
})
export class CreateEtudiantComponent {
  informationPersonelle : InformationPersonellesModel= {
    nomEtu: '',
    nomJeuneFilleEtu: '',
    prenomEtu: '',
    statutMarital: '',
    regime:0 ,
    profession: '',
    adresseEtu: '',
    telEtu: '',
    emailEtu: '',
    adresseParent: '',
    telParent: '',
    emailParent: '',
    nomParent: '',
    prenomParent: '',
    handicapYN: 0,
    ordiPersoYN: 0,
    derniereModification:'',
    emailUser: '',
    typeHandique: {id:0},
    typeBourse: {id:0},
    etudiant : {
      ine: '',
      dateNaissEtu: null,
      lieuNaissEtu: '',
      sexe: '',
      codeEtu: '',
      codeBU: 0,
      emailUGB: '',
      numDocIdentite: '',
      assimileYN: false,
      exonereYN: false,
      region: {id:0},
      typeSelection: {id:0},
      lycee: {id:0},
      baccalaureat: {id:0},
    },
    
  }


  etudiantForm: FormGroup;
  customStylesValidated = false;
  //mentionList : MentionModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private etudiantService: EtudiantService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.etudiantForm = new FormGroup({
      nomEtu: new FormControl('', Validators.required),
      nomJeuneFilleEtu: new FormControl('', Validators.required),
      prenomEtu: new FormControl('', Validators.required),
      statutMarital: new FormControl('', Validators.required),
      regime:new FormControl(0, Validators.required),
      profession: new FormControl('', Validators.required),
      adresseEtu: new FormControl('', Validators.required),
      telEtu: new FormControl('', Validators.required),
      emailEtu: new FormControl('', Validators.required),
      adresseParent: new FormControl('', Validators.required),
      telParent: new FormControl('', Validators.required),
      emailParent: new FormControl('', Validators.required),
      nomParent: new FormControl('', Validators.required),
      prenomParent: new FormControl('', Validators.required),
      derniereModification:new FormControl('', Validators.required),
      emailUser: new FormControl('', Validators.required),
      typeHandique: new FormControl('', Validators.required),
      typeBourse: new FormControl('', Validators.required),
      /*------------------------------------------------------------*/
      /*------------------------------------------------------------*/
      ine: new FormControl('', Validators.required),
      dateNaissEtu: new FormControl(null, Validators.required),
      lieuNaissEtu: new FormControl('', Validators.required),
      sexe: new FormControl('', Validators.required),
      codeEtu: new FormControl('', Validators.required),
      codeBU: new FormControl(0, Validators.required),
      emailUGB: new FormControl('', Validators.required),
      numDocIdentite: new FormControl('', Validators.required),
      region: new FormControl('', Validators.required),
      typeSelection: new FormControl('', Validators.required),
      lycee: new FormControl('', Validators.required),
      baccalaureat: new FormControl('', Validators.required),

    });
  }

  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    /* const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
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
    
    this.getListeMention(); */
  }


  onSubmit1() {

    if (this.etudiantForm!.valid) {
      this.customStylesValidated = true;
      
      
      //Si c'est une creation
      /* this.etudiantService.createEtudiant(this.etudiant!).subscribe({ 
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Création","Création de l'étudiant avec succes","success");
          this.router.navigate(['/gir/inscription-reinscription/view',data.id])
        },
        error: (err) => {
          this.alertService.showToast("Création","Echec de creation de l'étudiant","danger");
        }
      }); */

      
      
      //console.log('Données du formulaire :', this.specialite);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  onReset1() {
    this.etudiantForm!.reset();
    this.customStylesValidated = false;
  }

}
