import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBaccalaureat } from '../baccalaureat.model';

@Component({
  standalone: true,
  selector: 'jhi-baccalaureat-detail',
  templateUrl: './baccalaureat-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BaccalaureatDetailComponent {
  @Input() baccalaureat: IBaccalaureat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
