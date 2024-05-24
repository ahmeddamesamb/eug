import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaiementFrais } from '../paiement-frais.model';

@Component({
  standalone: true,
  selector: 'jhi-paiement-frais-detail',
  templateUrl: './paiement-frais-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaiementFraisDetailComponent {
  @Input() paiementFrais: IPaiementFrais | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
