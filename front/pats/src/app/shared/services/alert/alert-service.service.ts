import { Injectable } from '@angular/core';
import { ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { delay } from 'rxjs';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';


@Injectable({
  providedIn: 'root'
})
export class AlertServiceService {

  constructor() { }

  private toaster: ToasterComponent | undefined;

  setToaster(toaster: ToasterComponent) {
    this.toaster = toaster;
  }

  showToast(title: string, texte: string, color: string, placement: ToasterPlacement = ToasterPlacement.TopEnd) {
    if (!this.toaster) {
      return;
    }
    var delay = 5000;
    if(color == "danger"){
      delay = 500000;
    }
    const options = {
      title: title,
      texte: texte,
      delay: delay,
      placement: placement,
      color: color,
      autohide: true,
    };

    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
    });
    componentRef.instance['visibleChange'].emit(true);
  }
}
