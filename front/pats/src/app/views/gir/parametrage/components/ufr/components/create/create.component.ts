import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent,DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { UniversiteService } from '../../../universite/services/universite.service';
import {UniversiteModel} from '../../../universite/models/universite-model';
import {UfrModel} from '../../../ufr/models/ufr-model';
import {UfrServiceService} from '../../../ufr/services/ufr-service.service';
import { Router } from '@angular/router';
import {AlerteComponent} from 'src/app/shared/components/alerte/alerte/alerte.component'

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
  ufrForm: FormGroup | undefined;
  customStylesValidated = false;
  universiteList : UniversiteModel[] = []; //Pour le select du formulaire
  constructor( private ufrService: UfrServiceService,private universiteService: UniversiteService ,private route:Router ){

  }
  ngOnInit() {
    this.ufrForm = new FormGroup({
      libeleUfr: new FormControl('', Validators.required),
      sigleUfr: new FormControl('', Validators.required),
      systemeLMDYN: new FormControl(''),
      ordreStat: new FormControl(''),
      universite: new FormControl('', Validators.required)
    });
    this.getListeUniversite();
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

      this.ufrService.createUfr(this.ufr!).subscribe({
        
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.route.navigate(['/gir/parametrage/ufr/view',data.id])
        },
        error: (err) => {
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.ufr);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  onReset1() {
    this.ufrForm!.reset();
    this.customStylesValidated = false;
  }



  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Creation`,
      texte: `Echec de creation de l'Ufr`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Creation Ufr avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }
}
