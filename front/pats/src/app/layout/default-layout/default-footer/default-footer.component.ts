import { Component } from '@angular/core';
import { FooterComponent } from '@coreui/angular-pro';

@Component({
    selector: 'app-default-footer',
    templateUrl: './default-footer.component.html',
    styleUrls: ['./default-footer.component.scss'],
    standalone: true,
})
export class DefaultFooterComponent extends FooterComponent {
  annee: number | undefined;
  
  constructor() {
    super();
    const dateActuelle = new Date();
    this.annee = dateActuelle.getFullYear();
  }

}
