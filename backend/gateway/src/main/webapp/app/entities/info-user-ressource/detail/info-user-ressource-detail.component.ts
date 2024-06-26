import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInfoUserRessource } from '../info-user-ressource.model';

@Component({
  standalone: true,
  selector: 'ugb-info-user-ressource-detail',
  templateUrl: './info-user-ressource-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InfoUserRessourceDetailComponent {
  @Input() infoUserRessource: IInfoUserRessource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
