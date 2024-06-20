import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, MultiSelectComponent as MultiSelectComponent_1,MultiSelectOptgroupComponent,MultiSelectOptionComponent, } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { DomaineService } from '../../services/domaine.service';
import { DomaineModel } from '../../models/domaine-model';
import { UfrModel } from '../../../ufr/models/ufr-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import { UniversiteModel } from '../../../universite/models/universite-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'
import { UfrServiceService } from '../../../ufr/services/ufr-service.service';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective,DatePipe,DatePickerComponent_1,ToasterComponent, MultiSelectComponent_1, MultiSelectOptionComponent, MultiSelectOptgroupComponent],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {
  domaine :DomaineModel = {
    libelleDomaine: '',
    ufrs : []
  }; 

  domaineForm: FormGroup;
  customStylesValidated = false;
  ufrList : UfrModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private domaineService: DomaineService, private ufrService: UfrServiceService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.domaineForm = new FormGroup({
      libelleDomaine: new FormControl('', Validators.required),
      ufrs: new FormControl(null, Validators.required),

    });
  }


  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{
        this.domaineService.getDomaineById(parseInt(id)).subscribe(
          (data) => {
            this.domaine = {
              libelleDomaine: '',
              ufrs : []
            }; 
            this.domaine = data;
            this.id = parseInt(id);
            this.initializeForm(this.domaine);          
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }
    
    this.getListeUfr();
  }

  initializeForm(domaine: DomaineModel) {
    //this.domaineForm.reset();
    this.domaineForm.setValue({
      libelleDomaine: '',
      ufrs: null,
    });
    console.log("Information du formulaire avant initialisation", this.domaineForm.value);
    var ufrsChoisis = domaine.ufrs!.map(ufr => ufr.id);
  
    // Mettez à jour le formulaire avec les nouvelles données
    this.domaineForm.setValue({
      libelleDomaine: domaine.libelleDomaine || '',
      ufrs: ufrsChoisis,
    });
  
    console.log("Information du formulaire après initialisation", this.domaineForm.value);
  }


  //Pour le select du formulaire
  getListeUfr(){
    this.ufrService.getUfrList().subscribe({
      next: (data) => {
        this.ufrList = data;
      },
      error: (err) => {


      }
    });
  }





  onSubmit1() {

    if (this.domaineForm!.valid) {
      this.customStylesValidated = true;
      //var idUniversite = this.ufrForm!.get('universite')?.value;
      this.domaine.libelleDomaine = this.domaineForm!.get('libelleDomaine')?.value; 
      
      var idUfrChoisi = this.domaineForm!.get('ufrs')?.value;
      this.domaine.ufrs = idUfrChoisi.map((id: number) => ({ id: id }));
              
      

      if(this.id !=null){
        //Si c'est une mis a jour
        this.domaineService.updateDomaine(this.id, this.domaine).subscribe({
          next: (data) => {
            console.log(data);

            this.alertService.showToast("Mis a jour","Mis a jour du domaine avec succes","success");
            this.router.navigate(['/gir/parametrage/domaine/view', data.id]);
          },
          error: (err) => {
            console.log(err);

            this.alertService.showToast("Mis a jour","Echec de la Mis a jour du domaine","danger");
          }
        });
      }else{
        //Si c'est une creation
        this.domaineService.createDomaine(this.domaine!).subscribe({ 
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Creation","Creation du domaine avec succes","success");
            this.router.navigate(['/gir/parametrage/domaine/view',data.id])
          },
          error: (err) => {

            this.alertService.showToast("Creation","Echec de creation du domaine","danger");
          }
        });

      }
      
      console.log('Données du formulaire :', this.domaine);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  annuler(){
    this.router.navigate(['/gir/parametrage/ufr/attente']);
  }

  onReset1() {
    this.domaineForm!.reset();
    this.customStylesValidated = false;
  }








}
