import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';

@Component({
  standalone: true,
  selector: 'ugb-processus-inscription-administrative-detail',
  templateUrl: './processus-inscription-administrative-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProcessusInscriptionAdministrativeDetailComponent {
  @Input() processusInscriptionAdministrative: IProcessusInscriptionAdministrative | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
