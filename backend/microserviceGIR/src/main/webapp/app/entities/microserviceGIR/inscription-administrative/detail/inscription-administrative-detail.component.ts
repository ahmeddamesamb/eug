import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInscriptionAdministrative } from '../inscription-administrative.model';

@Component({
  standalone: true,
  selector: 'jhi-inscription-administrative-detail',
  templateUrl: './inscription-administrative-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InscriptionAdministrativeDetailComponent {
  @Input() inscriptionAdministrative: IInscriptionAdministrative | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
