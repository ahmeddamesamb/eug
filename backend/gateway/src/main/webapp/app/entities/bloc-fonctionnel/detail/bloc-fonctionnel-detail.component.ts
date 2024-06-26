import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBlocFonctionnel } from '../bloc-fonctionnel.model';

@Component({
  standalone: true,
  selector: 'ugb-bloc-fonctionnel-detail',
  templateUrl: './bloc-fonctionnel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BlocFonctionnelDetailComponent {
  @Input() blocFonctionnel: IBlocFonctionnel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
