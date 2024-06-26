import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFormationInvalide } from '../formation-invalide.model';

@Component({
  standalone: true,
  selector: 'ugb-formation-invalide-detail',
  templateUrl: './formation-invalide-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FormationInvalideDetailComponent {
  @Input() formationInvalide: IFormationInvalide | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
