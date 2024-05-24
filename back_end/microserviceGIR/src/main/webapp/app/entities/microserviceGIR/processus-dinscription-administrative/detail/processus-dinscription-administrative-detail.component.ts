import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProcessusDinscriptionAdministrative } from '../processus-dinscription-administrative.model';

@Component({
  standalone: true,
  selector: 'jhi-processus-dinscription-administrative-detail',
  templateUrl: './processus-dinscription-administrative-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProcessusDinscriptionAdministrativeDetailComponent {
  @Input() processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
