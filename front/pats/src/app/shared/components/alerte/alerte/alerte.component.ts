import { ChangeDetectorRef, Component, ElementRef, forwardRef, Input, Renderer2 } from '@angular/core';

import { ToastComponent, ToasterService, ToastHeaderComponent, ToastBodyComponent, ToastCloseDirective, ProgressBarDirective, ProgressComponent } from '@coreui/angular-pro';


@Component({
  selector: 'app-alerte',

  templateUrl: './alerte.component.html',
  styleUrl: './alerte.component.scss',
  providers: [{ provide: ToastComponent, useExisting: forwardRef(() => AlerteComponent) }],
  standalone: true,
  imports: [ToastHeaderComponent, ToastBodyComponent, ToastCloseDirective, ProgressBarDirective, ProgressComponent]
})
export class AlerteComponent extends ToastComponent{

  @Input() closeButton = true;
  @Input() title = '';
  @Input() texte = '';

  constructor(
    public override hostElement: ElementRef,
    public override renderer: Renderer2,
    public override toasterService: ToasterService,
    public override changeDetectorRef: ChangeDetectorRef
  ) {
    super(hostElement, renderer, toasterService, changeDetectorRef);
  }

}
