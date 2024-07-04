import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { EtudiantService } from '../services/etudiant.service';
import { InformationPersonnellesService } from '../services/information-personnelles.service';
import { EtudiantModel } from '../models/etudiant-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'
import { InformationPersonellesModel } from '../models/information-personelles-model';
import { TypeHandicapModel } from '../../parametrage/components/type-handicap/models/type-handicap-model';
import { TypeBourseModel } from '../../parametrage/components/type-bourse/models/type-bourse-model';
import { RegionModel } from '../../parametrage/components/region/models/region-model';
import { TypeselectionModel } from '../../parametrage/components/typeselection/models/typeselection-model';
import { BaccalaureatModel } from '../../parametrage/components/baccalaureat/models/baccalaureat-model';
import { LyceeModel } from '../../parametrage/components/lycee/models/lycee-model';
import { SerieModel } from '../../parametrage/components/serie/models/serie-model';

import { TypeHandicapService } from '../../parametrage/components/type-handicap/services/type-handicap.service';
import { TypeBourseService } from '../../parametrage/components/type-bourse/services/type-bourse.service';
import { RegionService } from '../../parametrage/components/region/services/region.service';
import { TypeselectionService } from '../../parametrage/components/typeselection/services/typeselection.service';
import {  LyceeService } from '../../parametrage/components/lycee/services/lycee.service';
import {  SerieService } from '../../parametrage/components/serie/services/serie.service';

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
      numDocIdentite: '',
      assimileYN: false,
      exonereYN: false,
      region: {id:0},
      typeSelection: {id:0},
      lycee: {id:0},
      baccalaureat: {
        origineScolaire: '',
        anneeBac: null,
        natureBac: '',
        mentionBac: '',
        moyenneSelectionBac: 0,
        moyenneBac: 0,
        serie: {id:0},
      },
    },
    
  }
  typeHandiqueListe: TypeHandicapModel[] = [];
  typeBourseListe: TypeBourseModel[] = [];
  regionListe: RegionModel[] = [];
  typeSelectionListe: TypeselectionModel[] = [];
  lyceeListe: LyceeModel[] = [];
  serieListe: SerieModel[] = [];

  



  informationPersonelleForm: FormGroup;
  customStylesValidated = false;
  //mentionList : MentionModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private infoPersoService: InformationPersonnellesService,
              private route: ActivatedRoute, private router: Router,
              private alertService: AlertServiceService,
              private typeHandiqueService:TypeHandicapService,
              private typeBourseService:TypeBourseService,
              private regionService:RegionService,
              private typeSelectionService:TypeselectionService,
              private lyceeService:LyceeService,
              private serieService:SerieService) {
    
    this.informationPersonelleForm = new FormGroup({
      nomEtu: new FormControl('', Validators.required),
      nomJeuneFilleEtu: new FormControl('', Validators.required),
      prenomEtu: new FormControl('', Validators.required),
      statutMarital: new FormControl('', Validators.required),
      regime: new FormControl(0, Validators.required),
      profession: new FormControl('', Validators.required),
      adresseEtu: new FormControl('', Validators.required),
      telEtu: new FormControl('', Validators.required),
      emailEtu: new FormControl('', Validators.required),
      adresseParent: new FormControl('', Validators.required),
      telParent: new FormControl('', Validators.required),
      emailParent: new FormControl('', Validators.required),
      nomParent: new FormControl('', Validators.required),
      prenomParent: new FormControl('', Validators.required),
      derniereModification: new FormControl('', Validators.required),
      emailUser: new FormControl('', Validators.required),
      typeHandique: new FormControl('', Validators.required),
      typeBourse: new FormControl('', Validators.required),
      handicapYN: new FormControl(0, Validators.required),
      ordiPersoYN: new FormControl(0, Validators.required),
      /*------------------------------------------------------------*/
      etudiant: new FormGroup({
        ine: new FormControl('', Validators.required),
        dateNaissEtu: new FormControl(null, Validators.required),
        lieuNaissEtu: new FormControl('', Validators.required),
        sexe: new FormControl('', Validators.required),
        numDocIdentite: new FormControl('', Validators.required),
        region: new FormControl('', Validators.required),
        typeSelection: new FormControl('', Validators.required),
        lycee: new FormControl('', Validators.required),
        /*------------------------------------------------------------*/
        baccalaureat: new FormGroup({
          origineScolaire: new FormControl('', Validators.required),
          anneeBac: new FormControl(null, Validators.required),
          natureBac: new FormControl('', Validators.required),
          mentionBac: new FormControl('', Validators.required),
          moyenneSelectionBac: new FormControl(0, Validators.required),
          moyenneBac: new FormControl(0, Validators.required),
          serie: new FormControl('', Validators.required)
        }),
      }),
      
    });

  }

  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    //const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    /* if(id){
      
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

    } */

      this.getListeTypeHandique();
    this.getListeTypeBourse();
    this.getListeRegion();
    this.getListeTypeSelection();
    this.getListeLycee();
    this.getListeSerie();
    
    
  }
  getListeLycee() {
    this.lyceeService.getLyceeList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.lyceeListe = data;
      },
      error: (err) => {
  
      }
    });
  }
  getListeSerie() {
    this.serieService.getSerieList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.serieListe = data;
      },
      error: (err) => {
  
      }
    });
  }
  getListeTypeSelection() {
    this.typeSelectionService.getTypeSelectionList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.typeSelectionListe = data;
      },
      error: (err) => {
  
      }
    });
  }
  getListeRegion() {
    this.regionService.getRegionList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.regionListe = data;
      },
      error: (err) => {
  
      }
    });
  }
  getListeTypeBourse() {
    this.typeBourseService.getTypeBourseList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.typeBourseListe = data;
      },
      error: (err) => {
  
      }
    });
  }
  getListeTypeHandique() {
    this.typeHandiqueService.getTypeHandicapList().subscribe({ 
      next: (data) => {
        //console.log(data);
        this.typeHandiqueListe = data;
      },
      error: (err) => {
  
      }
    });
  }

  mapFormToInformationPersonelle() {
    const formValues = this.informationPersonelleForm.value;
  
    this.informationPersonelle.nomEtu = formValues.nomEtu ?? '';
    this.informationPersonelle.nomJeuneFilleEtu = formValues.nomJeuneFilleEtu ?? '';
    this.informationPersonelle.prenomEtu = formValues.prenomEtu ?? '';
    this.informationPersonelle.statutMarital = formValues.statutMarital ?? '';
    this.informationPersonelle.regime = formValues.regime ?? 0;
    this.informationPersonelle.profession = formValues.profession ?? '';
    this.informationPersonelle.adresseEtu = formValues.adresseEtu ?? '';
    this.informationPersonelle.telEtu = formValues.telEtu ?? '';
    this.informationPersonelle.emailEtu = formValues.emailEtu ?? '';
    this.informationPersonelle.adresseParent = formValues.adresseParent ?? '';
    this.informationPersonelle.telParent = formValues.telParent ?? '';
    this.informationPersonelle.emailParent = formValues.emailParent ?? '';
    this.informationPersonelle.nomParent = formValues.nomParent ?? '';
    this.informationPersonelle.prenomParent = formValues.prenomParent ?? '';
    this.informationPersonelle.handicapYN = formValues.handicapYN ?? 0;
    this.informationPersonelle.ordiPersoYN = formValues.ordiPersoYN ?? 0;
    this.informationPersonelle.derniereModification = formValues.derniereModification ?? '';
    this.informationPersonelle.emailUser = formValues.emailUser ?? '';
    this.informationPersonelle.typeHandique = { id: formValues.typeHandique ?? 0 };
    this.informationPersonelle.typeBourse = { id: formValues.typeBourse ?? 0 };
  
    const etudiantFormValues = formValues.etudiant ?? {};
    this.informationPersonelle.etudiant = {
      ine: etudiantFormValues.ine ?? '',
      dateNaissEtu: etudiantFormValues.dateNaissEtu ?? null,
      lieuNaissEtu: etudiantFormValues.lieuNaissEtu ?? '',
      sexe: etudiantFormValues.sexe ?? '',
      numDocIdentite: etudiantFormValues.numDocIdentite ?? '',
      region: { id: etudiantFormValues.region ?? 0 },
      typeSelection: { id: etudiantFormValues.typeSelection ?? 0 },
      lycee: { id: etudiantFormValues.lycee ?? 0 },
      baccalaureat: {
        origineScolaire: etudiantFormValues.baccalaureat?.origineScolaire ?? '',
        anneeBac: etudiantFormValues.baccalaureat?.anneeBac ?? null,
        natureBac: etudiantFormValues.baccalaureat?.natureBac ?? '',
        mentionBac: etudiantFormValues.baccalaureat?.mentionBac ?? '',
        moyenneSelectionBac: etudiantFormValues.baccalaureat?.moyenneSelectionBac ?? 0,
        moyenneBac: etudiantFormValues.baccalaureat?.moyenneBac ?? 0,
        serie: { id: etudiantFormValues.baccalaureat?.serie ?? 0 }
      }
    };
  }
  



  onSubmit1() {

    if (this.informationPersonelleForm!.valid) {
      this.customStylesValidated = true;
      this.mapFormToInformationPersonelle();
      
      
      //Si c'est une creation
      this.infoPersoService.createInformationPersonnelle(this.informationPersonelle!).subscribe({ 
        next: (data) => {
          console.log(data);
          this.alertService.showToast("Création","Création de l'étudiant avec succes","success");
          this.router.navigate(['/gir/inscription-reinscription/view',data.etudiant?.id])
        },
        error: (err) => {
          this.alertService.showToast("Création","Echec de creation de l'étudiant","danger");
        }
      });
      //console.log('Données du formulaire :', this.specialite);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  onReset1() {
    this.informationPersonelleForm!.reset();
    this.customStylesValidated = false;
  }

}
