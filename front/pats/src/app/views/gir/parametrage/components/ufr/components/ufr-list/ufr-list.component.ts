import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {UfrServiceService} from '../../services/ufr-service.service';
import {UfrModel} from '../../models/ufr-model';
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
import { delay } from 'rxjs';

@Component({
  selector: 'app-ufr-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent,AlerteComponent
    ],
  templateUrl: './ufr-list.component.html',
  styleUrl: './ufr-list.component.scss'
})
export class UfrListComponent {

  constructor (private route:Router,private ufrService: UfrServiceService){

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
    { key: 'systemeLMDYN', label: 'LMD', _style: { width: '15%' } },
    { key: 'ordreStat', _style: { width: '15%' } },
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
          this.addToast(true);
        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.addToast(false);
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









  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Suppression`,
      texte: `Echec de suppression de l'UFR`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Suppression de l'UFR avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }



}
