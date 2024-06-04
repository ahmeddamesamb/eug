import { Component } from '@angular/core';
import { IItem } from '@coreui/angular-pro';
import { Router } from '@angular/router';
import {MinistereServiceService} from '../../services/ministere-service.service'
import {MinistereModel} from '../../models/ministere-model'
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'

import {
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
} from '@coreui/angular-pro';


import { pipe } from 'rxjs';

@Component({
  selector: 'app-ministere-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective,CardBodyComponent,CardComponent,CardHeaderComponent,  ColComponent,
  ],
  templateUrl: './ministere-list.component.html',
  styleUrl: './ministere-list.component.scss'
})
export class MinistereListComponent {
  constructor (private route:Router,private ministereService: MinistereServiceService){

  }

  ministereList : MinistereModel[] = [];


  ngOnInit(): void {
    this.ministereService.getMinistereList().subscribe((data)=>{
      this.ministereList = data;
 
    })
    
  }

  columns: IColumn[] = [
    {
      key: 'nomMinistere'
    },
    {
      key: 'sigleMinistere'
    },
    {
      key: 'dateDebut',
      label: 'Date debut',
      _props: { class: 'text-truncate' }
    },
    {
      key: 'dateFin',
      label: 'Date Fin',
      _props: { class: 'text-truncate' }
    },
    { key: 'enCoursYN', _style: { width: '15%' } },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];
  details_visible = Object.create({});

  getBadge(enCoursYN: number) {
    switch (enCoursYN) {
      case 1:
        return 'success';
      // case 'Inactive':
      //   return 'secondary';
      // case 'Pending':
      //   return 'warning';
      case 0:
        return 'danger';
      default:
        return 'primary';
    }
  }

  view(item: number) {
    this.route.navigate(['/gir/parametrage/ministere/view',item])

  }
  create() {
    this.route.navigate(['/gir/parametrage/ministere/create'])

  }
  delete(item: number){
    this.ministereService.deleteMinistere(item).subscribe((data)=>{
      console.log(data);
 
    })
    this.route.navigate(['/gir/parametrage/ministere/attente'])
  }

  public liveDemoVisible = false;

  toggleLiveDemo() {
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }
}
