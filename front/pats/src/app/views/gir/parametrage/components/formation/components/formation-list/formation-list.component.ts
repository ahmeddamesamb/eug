import { Component} from '@angular/core';
import { Router } from '@angular/router';

import {FormationService} from '../../services/formation.service';
import {FormationModel} from '../../models/formation-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service';

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
  ToasterPlacement,
  ToasterComponent,
  PopoverModule,
} from '@coreui/angular-pro';


@Component({
  selector: 'app-formation-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],
  templateUrl: './formation-list.component.html',
  styleUrl: './formation-list.component.scss'
})
export class FormationListComponent {
  constructor (private route:Router,private formationService: FormationService,private alertService: AlertServiceService){

  }

  formationList : FormationModel[] = [];
  itemDelete!: FormationModel;
  itemUpdate!: FormationModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }
  getListe(){
    this.isloading = true;
    this.formationService.getFormationList().subscribe({
      next: (data) => {
        this.formationList = data;
        this.isloading = false;
      },
      error: (err) => {


      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleDiplome',
      label: 'Diplôme'
    },
    
    {
      key: 'codeFormation',
      label: 'Code formation'
    },
    {
      key: 'typeFormation',
      label: 'Type Forma.'
    },
    {
      key: 'niveau',
      label: 'Niveau'
    },
    {
      key: 'specialite',
      label: 'Spécialité'
    },
    {
      key: 'departement',
      label: 'Département'
    },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];

  create(){

  }
  view(){
    
  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/mention/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.formationService.deleteFormation(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression d'une formation avec succes","success");

        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression de la formation","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : FormationModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }

  
}
