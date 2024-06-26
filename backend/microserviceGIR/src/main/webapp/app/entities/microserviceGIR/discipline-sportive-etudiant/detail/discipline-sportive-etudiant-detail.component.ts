import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';

@Component({
  standalone: true,
  selector: 'ugb-discipline-sportive-etudiant-detail',
  templateUrl: './discipline-sportive-etudiant-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DisciplineSportiveEtudiantDetailComponent {
  @Input() disciplineSportiveEtudiant: IDisciplineSportiveEtudiant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
