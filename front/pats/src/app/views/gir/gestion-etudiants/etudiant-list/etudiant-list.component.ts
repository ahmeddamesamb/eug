import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {InformationPersonnellesService} from '../services/information-personnelles.service';
import {InscriptionAdministrativeFormModel} from '../../inscription-reinscription/models/inscription-administrative-form-model';
import {InscriptionAdministrativeFormService} from '../../inscription-reinscription/services/inscription-administrative-form.service';
//import {EtudiantModel} from '../models/etudiant-model';
import { NumberToStringPipe } from '../../../../pipes/number-to-string.pipe'
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import {CreateEtudiantComponent} from '../create-etudiant/create-etudiant.component';
import{
  BadgeComponent,
  ButtonDirective,
  CollapseDirective,
  IColumn,
  SmartTableComponent,
  TemplateIdDirective,
  TextColorDirective,
  ModalBodyComponent,
  ModalComponent,
  ModalFooterComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  ModalToggleDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  PopoverModule,
} from '@coreui/angular-pro';

@Component({
  selector: 'app-etudiant-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,CreateEtudiantComponent
  ],
  templateUrl: './etudiant-list.component.html',
  styleUrl: './etudiant-list.component.scss'
})
export class EtudiantListComponent {

  constructor (private route:Router,private iafService: InscriptionAdministrativeFormService, private alertService: AlertServiceService){

  }

  iafList : InscriptionAdministrativeFormModel[] = [];
  itemDelete!: InscriptionAdministrativeFormModel;
  itemUpdate!: InscriptionAdministrativeFormModel;
  public liveDemoVisible = false;
  public modalCreateUpdate = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.iafService.getIafList().subscribe({
      next: (data) => {
        this.iafList = data;
        this.isloading = false;
      },
      error: (err) => {
        this.isloading = false;
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'inscriptionAdministrative',
      label: 'Code'
    },
    {
      key: 'formation',
      label: 'Niveau'
    },
    {
      key: 'formation',
      label: 'Filière'
    },
    {
      key: 'inscriptionAdministrative',
      label: 'Code BU'
    },
    {
      key: 'inscriptionAdministrative',
      label: 'Année'
    },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];

  view(item: number) {
    this.route.navigate(['/gir/inscription-reinscription/view/',item])

  }

  modalCreate() {
    this.modalCreateUpdate = !this.modalCreateUpdate;
    //this.route.navigate(['/gir/gestion-etudiant/create'])
  }

  update(item:number) {
    this.route.navigate(['/gir/gestion-etudiant/update',item])
  }


  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.iafService.deleteIaf(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppréssion","Suppréssion de l'étudiant avec succes","success");
        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppréssion","Echec de la suppréssion de l'étudiant","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : InscriptionAdministrativeFormModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }

  handleLivemodalCreate(event: boolean) {
    this.modalCreateUpdate = event;
  }


}
