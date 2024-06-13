import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { UniversiteService } from '../../services/universite.service';
import { MinistereServiceService } from '../../../ministere/services/ministere-service.service';
import { UniversiteModel } from '../../models/universite-model';
import { Router, ActivatedRoute } from '@angular/router';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';
import { map, Observable } from 'rxjs';
import { MinistereModel } from '../../../ministere/models/ministere-model';

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1, ToasterComponent],
  templateUrl: './update.component.html',
  styleUrl: './update.component.scss'
})
export class UpdateComponent {
  universite :UniversiteModel = {
    id:0,
    nomUniversite: '',
    sigleUniversite: '',
    ministere : {
      id: 0
    } 
  }; 
  universiteForm: FormGroup;
  customStylesValidated = false;
  id: string = "";

  constructor(private universiteService: UniversiteService, private ministereService: MinistereServiceService, private route: ActivatedRoute, private router: Router) {

    this.universiteForm = new FormGroup({
      nomUniversite: new FormControl('', Validators.required),
      sigleUniversite: new FormControl('', Validators.required),
      ministere: new FormControl('', Validators.required),
    });
  }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    id.subscribe((id)=>{
      this.universiteService.getUniversiteById(parseInt(id)).subscribe(
        (data) => {
          this.universite = data;
          this.initializeForm(this.universite);
        },
        (err) => {
          console.log(err);
        }
      );
    })
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
  ministereList : MinistereModel[] = []; //Pour le select du formulaire
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
    this.customStylesValidated = true;
    if (this.universiteForm.valid) {
      

      var idMinistere = this.universiteForm!.get('ministere')?.value;
      if (idMinistere) {
          if (this.universite && this.universite.ministere) {
              this.universite.nomUniversite = this.universiteForm!.get('nomUniversite')?.value; 
              this.universite.sigleUniversite = this.universiteForm!.get('sigleUniversite')?.value; 
              this.universite.ministere.id = parseInt(this.universiteForm!.get('ministere')?.value) ; 
          }
      }      

      console.log("MISE A JOUR:", this.universite);
      console.log("ID Universite: ", this.id);
      this.universiteService.updateUniversite(Number(this.universite.id), this.universite).subscribe({
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.router.navigate(['/gir/parametrage/universite/view', data.id]);
        },
        error: (err) => {
          console.log(err);
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.universite);
    } else {
      console.log('Formulaire invalide');
    }
  }
  annuler(){
    this.router.navigate(['/gir/parametrage/universite/attente']);
  }



  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Creation`,
      texte: `Echec de la mis a jour de l'université`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Mis a jour de l'université avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }
}
