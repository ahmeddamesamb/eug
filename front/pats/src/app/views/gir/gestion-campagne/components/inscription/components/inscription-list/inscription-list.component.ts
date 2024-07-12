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
import {FormationService  } from '../../../../../parametrage/components/formation/services/formation.service';
import { FormationModel } from '../../../../../parametrage/components/formation/models/formation-model';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';
@Component({
  selector: 'app-inscription-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, BadgeComponent, SmartTableComponent, TemplateIdDirective, TextColorDirective, CardHeaderComponent,
    PopoverModule, ColComponent,ToasterComponent,AlerteComponent
    ],
     templateUrl: './inscription-list.component.html',
  styleUrl: './inscription-list.component.scss'
})
export class InscriptionListComponent {
  constructor (private route:Router,private formationService: FormationService ,private alertService: AlertServiceService){
}

formationList : FormationModel[] = [];
  itemDelete!: FormationModel;
  itemUpdate!: FormationModel;
  public liveDemoVisible = false;
  isloading = false;
  selectedItems = [];

  ngOnInit(): void {
    this.getListe();
 
  }
  checkSelected = (selectedItems: any) => {
    this.selectedItems = selectedItems.map((item: { id: any; }) => item.id);
  };

  getListe(){
    this.isloading = true;
    this.formationService.getFormationList().subscribe({
      next: (data) => {
        this.formationList = data;
        console.log(this.formationList);
        this.isloading = false;
      },
      error: (err) => {
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'niveau',
      label: 'Niveau'
    },
    {
      key: 'specialite',
      label: 'Spécialité'
    },
   
    {
      key: 'show',
      label: 'Actions',
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
      this.formationService.deleteFormation(this.itemDelete.id).subscribe({
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

  toggleLiveDemo(item : FormationModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }


}