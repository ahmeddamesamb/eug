import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { MentionServiceService } from '../../services/mention-service.service';
import { DomaineService } from '../../../domaine/services/domaine.service';
import { MentionModel } from '../../models/mention-model';
import { Router, ActivatedRoute } from '@angular/router';
import { map, Observable } from 'rxjs';
import { DomaineModel } from '../../../domaine/models/domaine-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, 
    CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, 
    FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, 
    FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, 
    ListGroupItemDirective,DatePipe,ToasterComponent],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {

  mention :MentionModel = {
    libelleMention: '',
    domaine : {
      id: 0
    } 
  }; 
  mentionForm: FormGroup;
  customStylesValidated = false;
  domaineList : DomaineModel[] = []; //Pour le select du formulaire
  id: number | undefined ;

  constructor(private mentionService: MentionServiceService, private domaineService: DomaineService, private route: ActivatedRoute, private router: Router,private alertService:AlertServiceService) {

    this.mentionForm = new FormGroup({
      libelleMention: new FormControl('', Validators.required),
      domaine: new FormControl('', Validators.required),
    });
  }

  ngOnInit() {
    //this.id = this.route.snapshot.params['id'];
    
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));
    if(id){
      
      id.subscribe((id)=>{
        this.mentionService.getMentionById(parseInt(id)).subscribe(
          (data) => {
            this.mention = data;
            this.id = parseInt(id);
            this.initializeForm(this.mention); 
          },
          (err) => {
            console.log(err);
          }
        );
      })

    }
    
    this.getListeDomaine();
  }

  initializeForm(mention: MentionModel) {
    this.mentionForm.setValue({
      libelleMention: mention.libelleMention || '',
      domaine :mention.domaine?.id|| 0,
    });
  }

  //Pour le select du formulaire
  getListeDomaine(){
    this.domaineService.getDomaineList().subscribe({
      next: (data) => {
        this.domaineList = data;
      },
      error: (err) => {


      }
    });
  }

  onSubmit1() {

    if (this.mentionForm!.valid) {
      this.customStylesValidated = true;
      var idDomaine = this.mentionForm!.get('domaine')?.value;
      if (idDomaine) {
          if (this.mention && this.mention.domaine) {
              this.mention.libelleMention = this.mentionForm!.get('libelleMention')?.value; 
              this.mention.domaine.id = parseInt(this.mentionForm!.get('domaine')?.value) ; 
          }
      }
      if(this.id !=null){
        //Si c'est une mis a jour
        this.mentionService.updateMention(Number(this.mention.id), this.mention).subscribe({
          next: (data) => {
            console.log(data);

            this.alertService.showToast("Mis a jour","Mis a jour de la mention avec succes","success");
            this.router.navigate(['/gir/parametrage/mention/view', data.id]);
          },
          error: (err) => {
            console.log(err);

            this.alertService.showToast("Mis a jour","Echec de la Mis a jour de la mention","danger");
          }
        });
      }else{
        //Si c'est une creation
        this.mentionService.createMention(this.mention!).subscribe({ 
          next: (data) => {
            console.log(data);
            this.alertService.showToast("Creation","Creation mention avec succes","success");
            this.router.navigate(['/gir/parametrage/mention/view',data.id])
          },
          error: (err) => {

            this.alertService.showToast("Creation","Echec de creation de la mention","danger");
          }
        });

      }
      
      console.log('Données du formulaire :', this.mention);
    } else {
      console.log('Formulaire invalide');
    }
    
  }

  annuler(){
    this.router.navigate(['/gir/parametrage/mention/attente']);
  }

  onReset1() {
    this.mentionForm!.reset();
    this.customStylesValidated = false;
  }

}
