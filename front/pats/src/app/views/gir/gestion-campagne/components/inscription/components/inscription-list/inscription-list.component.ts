import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

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
import { delay } from 'rxjs';
import { InscriptionService } from '../../../../services/inscription.service';
import { InscriptionModel } from '../../models/inscription-model';
@Component({
  selector: 'app-inscription-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],  templateUrl: './inscription-list.component.html',
  styleUrl: './inscription-list.component.scss'
})
export class InscriptionListComponent {
  constructor (private route:Router,private inscriptionService: InscriptionService ,private alertService: AlertServiceService){
}

inscriptionList : InscriptionModel[] = [];
  itemDelete!: InscriptionModel;
  itemUpdate!: InscriptionModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.inscriptionService.getInscriptionList().subscribe({
      next: (data) => {
        this.inscriptionList = data;
        this.isloading = false;
      },
      error: (err) => {


      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleFormation',
      label: 'Formation'
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
    this.route.navigate(['/gir/gestion-campagne/inscription/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/gestion-campagne/inscription/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/gestion-campagne/inscription/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.inscriptionService.deleteInscription(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression de l'UFR avec succes","success");

        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression de l'UFR","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : InscriptionModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }


}