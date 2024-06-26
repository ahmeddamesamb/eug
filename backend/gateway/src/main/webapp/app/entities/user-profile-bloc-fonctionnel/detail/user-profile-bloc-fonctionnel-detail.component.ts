import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';

@Component({
  standalone: true,
  selector: 'ugb-user-profile-bloc-fonctionnel-detail',
  templateUrl: './user-profile-bloc-fonctionnel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserProfileBlocFonctionnelDetailComponent {
  @Input() userProfileBlocFonctionnel: IUserProfileBlocFonctionnel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
