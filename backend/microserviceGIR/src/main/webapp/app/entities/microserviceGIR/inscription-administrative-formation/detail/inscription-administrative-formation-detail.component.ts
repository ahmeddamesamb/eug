import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';

@Component({
  standalone: true,
  selector: 'ugb-inscription-administrative-formation-detail',
  templateUrl: './inscription-administrative-formation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InscriptionAdministrativeFormationDetailComponent {
  @Input() inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
