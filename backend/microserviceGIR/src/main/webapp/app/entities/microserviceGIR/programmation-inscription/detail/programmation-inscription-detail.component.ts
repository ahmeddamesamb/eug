import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProgrammationInscription } from '../programmation-inscription.model';

@Component({
  standalone: true,
  selector: 'jhi-programmation-inscription-detail',
  templateUrl: './programmation-inscription-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProgrammationInscriptionDetailComponent {
  @Input() programmationInscription: IProgrammationInscription | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
