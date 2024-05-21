import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInformationPersonnelle } from '../information-personnelle.model';

@Component({
  standalone: true,
  selector: 'jhi-information-personnelle-detail',
  templateUrl: './information-personnelle-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InformationPersonnelleDetailComponent {
  @Input() informationPersonnelle: IInformationPersonnelle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
