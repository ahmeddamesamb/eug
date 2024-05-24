import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';

@Component({
  standalone: true,
  selector: 'jhi-paiement-formation-privee-detail',
  templateUrl: './paiement-formation-privee-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaiementFormationPriveeDetailComponent {
  @Input() paiementFormationPrivee: IPaiementFormationPrivee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
