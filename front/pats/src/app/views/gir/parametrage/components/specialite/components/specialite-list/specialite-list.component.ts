import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {SpecialiteServiceService} from '../../services/specialite-service.service';
import {SpecialiteModel} from '../../models/specialite-model';
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

@Component({
  selector: 'app-specialite-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],
  templateUrl: './specialite-list.component.html',
  styleUrl: './specialite-list.component.scss'
})
export class SpecialiteListComponent {
  constructor (private route:Router,private specialiteService: SpecialiteServiceService ,private alertService: AlertServiceService){

  }

  specialiteList : SpecialiteModel[] = [];
  itemDelete!: SpecialiteModel;
  itemUpdate!: SpecialiteModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
  }

  getListe(){
    this.isloading = true;
    this.specialiteService.getSpecialiteList().subscribe({
      next: (data) => {
        this.specialiteList = data;
        this.isloading = false;
      },
      error: (err) => {
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'nomSpecialites',
      label: 'Nom'
    },
    {
      key: 'sigleSpecialites',
      label: 'Sigle'
    },
    {
      key: 'mention',
      label: 'Mention'
    },
    { key: 'specialiteParticulierYN', label: 'Particulier', _style: { width: '15%' } },
    { key: 'specialitesPayanteYN', label: 'Payante', _style: { width: '15%' } },
    
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];

  view(item: number) {
    this.route.navigate(['/gir/parametrage/specialite/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/parametrage/specialite/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/specialite/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.specialiteService.deleteSpecialite(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression de l'spépialité avec succes","success");

        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression de l'spépialité","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      //console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : SpecialiteModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }

}
