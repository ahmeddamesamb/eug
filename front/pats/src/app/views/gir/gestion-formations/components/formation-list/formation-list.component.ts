import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {FormationService} from '../../../parametrage/components/formation/services/formation.service';
import {FormationModel} from '../../../parametrage/components/formation/models/formation-model';
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
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent
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
      key: 'specialite',
      label: 'Spécialité'
    },
    {
      key: 'niveau',
      label: 'Niveau'
    },
    {
      key: 'departement',
      label: 'UFR'
    },
    {
      key: 'lmdYN',
      label: 'LMD'
    },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];

  formationAutoriser(item:number){
    this.route.navigate(['/gir/gestion-formation/gestion-formation/formation-autorise',item])
  }

}
