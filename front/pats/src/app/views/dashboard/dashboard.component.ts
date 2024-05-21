import { DOCUMENT, NgStyle } from '@angular/common';
import { Component, DestroyRef, effect, inject, OnInit, Renderer2, signal, WritableSignal } from '@angular/core';
import  {InfoServiceComponent } from './info-service/info-service.component'



import {
  AvatarComponent,
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardFooterComponent,
  CardHeaderComponent,
  CardSubtitleDirective,
  CardTitleDirective,
  ColComponent,
  ProgressComponent,
  RowComponent,
  TableDirective
} from '@coreui/angular-pro';
import { ChartjsComponent } from '@coreui/angular-chartjs';
import { IconDirective } from '@coreui/icons-angular';
import { getStyle } from '@coreui/utils';
import {
  WidgetsDropdownVerticalComponent
} from '../widgets/widgets-dropdown-vertical/widgets-dropdown-vertical.component';
import { DashboardChartsData, IChartProps } from './dashboard-charts-data';
import { CardsComponent } from "../base/cards/cards.component";

const avatar2 = './assets/images/avatars/2.jpg';
const avatar3 = './assets/images/avatars/3.jpg';
const avatar1 = './assets/images/avatars/1.jpg';
const avatar4 = './assets/images/avatars/4.jpg';
const avatar5 = './assets/images/avatars/5.jpg';
const avatar6 = './assets/images/avatars/6.jpg';

@Component({
    templateUrl: 'dashboard.component.html',
    styleUrls: ['dashboard.component.scss'],
    standalone: true,
    imports: [
        NgStyle,
        AvatarComponent,
        ButtonDirective,
        CardComponent,
        CardBodyComponent,
        CardFooterComponent,
        CardHeaderComponent,
        CardSubtitleDirective,
        CardTitleDirective,
        ChartjsComponent,
        ColComponent,
        IconDirective,
        ProgressComponent,
        RowComponent,
        TableDirective,
        WidgetsDropdownVerticalComponent,
        CardsComponent,
        InfoServiceComponent
    ]
})
export class DashboardComponent implements OnInit {

  service_items = [1, 2, 3, 4, 5, 6];


  
  ngOnInit(): void {
    
  }



 

  
 
}
