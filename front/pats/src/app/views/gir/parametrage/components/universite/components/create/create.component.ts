import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent,DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { UniversiteService } from '../../services/universite.service';
import {UniversiteModel} from '../../models/universite-model';
import {MinistereModel} from '../../../ministere/models/ministere-model';
import {MinistereServiceService} from '../../../ministere/services/ministere-service.service';
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
  universite :UniversiteModel = {
    nomUniversite: '',
    sigleUniversite: '',
    ministere : {
      id: 0
    } 
  }; 
  universiteForm: FormGroup | undefined;
  customStylesValidated = false;
  ministereList : MinistereModel[] = []; //Pour le select du formulaire
  constructor( private universiteService: UniversiteService,private ministereService: MinistereServiceService ,private route:Router ){

  }

  ngOnInit() {
    this.universiteForm = new FormGroup({
      nomUniversite: new FormControl('', Validators.required),
      sigleUniversite: new FormControl('', Validators.required),
      ministere: new FormControl('', Validators.required)
    });
    this.getListeMinistere();
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

      
      console.log('Données du formulaire :', this.universite);

      this.universiteService.createUniversite(this.universite!).subscribe({
        
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.route.navigate(['/gir/parametrage/universite/view',data.id])
        },
        error: (err) => {
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.universite);
    } else {
      console.log('Formulaire invalide');
    }
  }

  onReset1() {
    this.universiteForm!.reset();
    this.customStylesValidated = false;
  }



  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Creation`,
      texte: `Echec de creation de l'université`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Creation université avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }

}
