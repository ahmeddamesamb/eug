import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDisciplineSportive } from '../discipline-sportive.model';

@Component({
  standalone: true,
  selector: 'jhi-discipline-sportive-detail',
  templateUrl: './discipline-sportive-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DisciplineSportiveDetailComponent {
  @Input() disciplineSportive: IDisciplineSportive | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
