import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {FormationService} from '../../../parametrage/components/formation/services/formation.service';
import {FormationModel} from '../../../parametrage/components/formation/models/formation-model';
import { NumberToStringPipe } from '../../../../../pipes/number-to-string.pipe';
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
  selector: 'app-formation-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent
  ],
  templateUrl: './formation-list.component.html',
  styleUrl: './formation-list.component.scss'
})
export class FormationListComponent {
  constructor (private route:Router){

  }

  formationList : FormationModel[] = [];
  itemDelete!: FormationModel;
  itemUpdate!: FormationModel;
  isloading = false;

  formationAutoriser(){
    this.route.navigate(['/gir/gestion-formation/gestion-formation/formation-autorise'])
  }

  columns: IColumn[] = [
    {
      key: 'libelleformation',
      label: 'Nom'
    },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];
}
