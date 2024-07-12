import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {CycleService} from '../../services/cycle.service';
import {CycleModel} from '../../models/cycle-model'
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
  selector: 'app-cycle-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],
  templateUrl: './cycle-list.component.html',
  styleUrl: './cycle-list.component.scss'
})
export class CycleListComponent {
  constructor (private route:Router,private cycleService: CycleService){

  }
  
  cycleList : CycleModel[] = [];
  itemDelete!: CycleModel;
  itemUpdate!: CycleModel;
  public liveDemoVisible = false;
  isloading = false;
  
  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.cycleService.getCycleList().subscribe({
      next: (data) => {
        this.cycleList = data;
        this.isloading = false;
      },
      error: (err) => {
        this.isloading = false;
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleCycle',
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

  create(){
    this.route.navigate(['/gir/parametrage/cycle/create'])
  }
  associerFrais(){
    this.route.navigate(['/gir/parametrage/cycle/associer-frais',1])

  }
  
  
}
