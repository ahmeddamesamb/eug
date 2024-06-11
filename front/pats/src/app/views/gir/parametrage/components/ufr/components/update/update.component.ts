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

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1, ToasterComponent],
  templateUrl: './update.component.html',
  styleUrl: './update.component.scss'
})
export class UpdateComponent {
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
  id: string = "";

  constructor(private ufrService: UfrServiceService, private universiteService: UniversiteService, private route: ActivatedRoute, private router: Router) {

    this.ufrForm = new FormGroup({
      libeleUfr: new FormControl('', Validators.required),
      sigleUfr: new FormControl('', Validators.required),
      universite: new FormControl('', Validators.required),
      systemeLMDYN: new FormControl('', Validators.required),
      ordreStat: new FormControl('', Validators.required),
    });
  }


  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    id.subscribe((id)=>{
      this.ufrService.getUfrById(parseInt(id)).subscribe(
        (data) => {
          this.ufr = data;
          this.initializeForm(this.ufr);
        },
        (err) => {
          console.log(err);
        }
      );
    })
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

  universiteList : UniversiteModel[] = []; //Pour le select du formulaire
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
    this.customStylesValidated = true;
    if (this.ufrForm.valid) {
      

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

      console.log("MISE A JOUR:", this.ufr);
      console.log("ID Universite: ", this.id);
      this.ufrService.updateUfr(Number(this.ufr.id), this.ufr).subscribe({
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.router.navigate(['/gir/parametrage/ufr/view', data.id]);
        },
        error: (err) => {
          console.log(err);
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.ufr);
    } else {
      console.log('Formulaire invalide');
    }
  }
  annuler(){
    this.router.navigate(['/gir/parametrage/ufr/attente']);
  }




  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Mis a jour`,
      texte: `Echec de la mis a jour de l'ufr`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Mis a jour de l'ufr avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }

}
