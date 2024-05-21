import { Component } from '@angular/core';
import {
  CardBodyComponent,
  CardComponent,
} from '@coreui/angular-pro';

@Component({
  selector: 'app-info-service',
  standalone: true,
  imports: [
    CardComponent,
    CardBodyComponent,
],
  templateUrl: './info-service.component.html',
  styleUrl: './info-service.component.scss'
  
})
export class InfoServiceComponent {

}
