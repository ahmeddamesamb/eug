import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {UfrServiceService} from '../../services/ufr-service.service';
import {UfrModel} from '../../models/ufr-model';
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
  selector: 'app-ufr-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],
  templateUrl: './ufr-list.component.html',
  styleUrl: './ufr-list.component.scss'
})
export class UfrListComponent {

  constructor (private route:Router,private ufrService: UfrServiceService ,private alertService: AlertServiceService){

  }

  ufrList : UfrModel[] = [];
  itemDelete!: UfrModel;
  itemUpdate!: UfrModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.ufrService.getUfrList().subscribe({
      next: (data) => {
        this.ufrList = data;
        this.isloading = false;
      },
      error: (err) => {


      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libeleUfr',
      label: 'Nom'
    },
    {
      key: 'sigleUfr',
      label: 'Sigle'
    },
   /*  { key: 'systemeLMDYN', label: 'LMD', _style: { width: '15%' } },
    { key: 'ordreStat', _style: { width: '15%' } }, */
    {
      key: 'universite',
      label: 'Université'
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
    this.route.navigate(['/gir/parametrage/ufr/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/parametrage/ufr/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/ufr/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.ufrService.deleteUfr(this.itemDelete.id).subscribe({
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

  toggleLiveDemo(item : UfrModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }





}
