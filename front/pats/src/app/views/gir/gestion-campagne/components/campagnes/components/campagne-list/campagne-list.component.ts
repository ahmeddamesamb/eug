import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {CampagneService} from '../../services/campagne.service'
import {CampagneModel} from '../../models/campagne-model'
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'

import {AlerteComponent} from 'src/app/shared/components/alerte/alerte/alerte.component'




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
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';

@Component({
  selector: 'app-campagne-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent,AlerteComponent
    ],
  templateUrl: './campagne-list.component.html',
  styleUrl: './campagne-list.component.scss'
})
export class CampagneListComponent {
  constructor (private route:Router,private campagneService: CampagneService ,private alertService: AlertServiceService){

  }

  campagneList : CampagneModel[] = [];
  itemDelete!: CampagneModel;
  itemUpdate!: CampagneModel;
  public liveDemoVisible = false;
  isloading = false;


  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.campagneService.getCampagneList().subscribe({
      next: (data) => {
        this.campagneList = data;
        this.isloading = false;
      },
      error: (err) => {
        this.isloading = false;
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleCampagne',
      label: 'Campagne'
    },
    {
      key: 'dateDebut',
      label: 'Date début',
      _props: { class: 'text-truncate' }
    },
    {
      key: 'dateFin',
      label: 'Date Fin',
      _props: { class: 'text-truncate' }
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
    this.route.navigate(['/gir/gestion-campagne/campagnes/view',item])
  }

  create() { 
    this.route.navigate(['/gir/gestion-campagne/campagnes/create'])
  }
  update(item:number) {
    this.route.navigate(['/gir/gestion-campagne/campagnes/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.campagneService.deleteCampagne(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression de la campagne avec succes","success");

        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression campagne","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : CampagneModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }

}
