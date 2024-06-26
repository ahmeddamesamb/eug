import { Component, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';

import { IconSetService } from '@coreui/icons-angular';
import { iconSubset } from './shared/icons/icon-subset';
import {AlertServiceService} from './shared/services/alert/alert-service.service'

@Component({
  selector: 'app-root',
  template: '<router-outlet /> <c-toaster #toaster position="fixed" [placement]="placement"></c-toaster>',
  standalone: true,
  imports: [RouterOutlet,ToasterComponent]
})
export class AppComponent implements OnInit {
  title = 'e-UGB';
  @ViewChild('toaster') toaster: ToasterComponent | undefined;
  placement = ToasterPlacement.TopEnd;

  constructor(
    private router: Router,
    private titleService: Title,
    private iconSetService: IconSetService,
    private toastService: AlertServiceService
  ) {
    this.titleService.setTitle(this.title);
    // iconSet singleton
    this.iconSetService.icons = { ...iconSubset };
  }

  ngOnInit(): void {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
    });
  }

  ngAfterViewInit() {
    if(this.toaster){
      this.toastService.setToaster(this.toaster);
    }
    
  }
}
