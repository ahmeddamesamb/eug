import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {DomaineService} from '../../services/domaine.service';
import {DomaineModel} from '../../models/domaine-model';
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
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
  selector: 'app-domaine-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent
  ],
  templateUrl: './domaine-list.component.html',
  styleUrl: './domaine-list.component.scss'
})
export class DomaineListComponent {
  constructor (private route:Router,private domaineService: DomaineService,private alertService: AlertServiceService){

  }

  domaineList : DomaineModel[] = [];
  itemDelete!: DomaineModel;
  itemUpdate!: DomaineModel;
  public liveDemoVisible = false;
  isloading = false;


  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.domaineService.getDomaineList().subscribe({
      next: (data) => {
        this.domaineList = data;
        this.isloading = false;
      },
      error: (err) => {
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleDomaine',
      label: 'libelle'
    },
    {
      key: 'ufrs',
      label: 'ufrs'
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
    this.route.navigate(['/gir/parametrage/domaine/view',item])

  }

  create() {
    
    this.route.navigate(['/gir/parametrage/domaine/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/domaine/update',item])
  }


  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.domaineService.deleteDomaine(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression du domaine avec succes","success");
        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression du domaine","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }


  toggleLiveDemo(item : DomaineModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }


}
