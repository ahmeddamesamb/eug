import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFormationAutorisee } from '../formation-autorisee.model';

@Component({
  standalone: true,
  selector: 'ugb-formation-autorisee-detail',
  templateUrl: './formation-autorisee-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FormationAutoriseeDetailComponent {
  @Input() formationAutorisee: IFormationAutorisee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
