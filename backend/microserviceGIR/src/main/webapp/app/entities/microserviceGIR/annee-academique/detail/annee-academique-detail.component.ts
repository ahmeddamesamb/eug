import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAnneeAcademique } from '../annee-academique.model';

@Component({
  standalone: true,
  selector: 'ugb-annee-academique-detail',
  templateUrl: './annee-academique-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AnneeAcademiqueDetailComponent {
  @Input() anneeAcademique: IAnneeAcademique | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
