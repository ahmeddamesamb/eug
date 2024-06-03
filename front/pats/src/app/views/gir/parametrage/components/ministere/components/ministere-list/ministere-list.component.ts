import { Component } from '@angular/core';
import { IItem } from '@coreui/angular-pro';
import { Router } from '@angular/router';

import {
  BadgeComponent,
  ButtonDirective,
  CollapseDirective,
  IColumn,
  SmartTableComponent,
  TemplateIdDirective,
  TextColorDirective
} from '@coreui/angular-pro';

@Component({
  selector: 'app-ministere-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective],
  templateUrl: './ministere-list.component.html',
  styleUrl: './ministere-list.component.scss'
})
export class MinistereListComponent {
  constructor (private route:Router){

  }

  usersData: IItem[] = [
   
    {id: 0, libelle: "Ministére de la Santé et de l'Action Social", sigle: 'MSAS', dateDebut: '2018/01/01' , dateFin: '2019/01/01', status: 'Inactive' },
    {id: 1, libelle: 'Ministere de Zoulaykha', sigle: 'MZ', dateDebut: '2018/01/01', dateFin: '2019/01/01', status: 'Active'},
  ]

  columns: IColumn[] = [
    {
      key: 'libelle'
    },
    {
      key: 'sigle'
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
    { key: 'status', _style: { width: '15%' } },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];
  details_visible = Object.create({});

  getBadge(status: string) {
    switch (status) {
      case 'Active':
        return 'success';
      case 'Inactive':
        return 'secondary';
      case 'Pending':
        return 'warning';
      case 'Banned':
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
}
