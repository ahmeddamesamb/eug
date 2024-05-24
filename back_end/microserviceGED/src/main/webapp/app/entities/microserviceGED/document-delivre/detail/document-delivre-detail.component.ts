import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDocumentDelivre } from '../document-delivre.model';

@Component({
  standalone: true,
  selector: 'jhi-document-delivre-detail',
  templateUrl: './document-delivre-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DocumentDelivreDetailComponent {
  @Input() documentDelivre: IDocumentDelivre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
