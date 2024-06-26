import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInscriptionDoctorat } from '../inscription-doctorat.model';

@Component({
  standalone: true,
  selector: 'ugb-inscription-doctorat-detail',
  templateUrl: './inscription-doctorat-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InscriptionDoctoratDetailComponent {
  @Input() inscriptionDoctorat: IInscriptionDoctorat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
