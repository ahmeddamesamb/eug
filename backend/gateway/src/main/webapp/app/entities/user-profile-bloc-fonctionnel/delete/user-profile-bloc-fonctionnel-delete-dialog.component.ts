import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';
import { UserProfileBlocFonctionnelService } from '../service/user-profile-bloc-fonctionnel.service';

@Component({
  standalone: true,
  templateUrl: './user-profile-bloc-fonctionnel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserProfileBlocFonctionnelDeleteDialogComponent {
  userProfileBlocFonctionnel?: IUserProfileBlocFonctionnel;

  constructor(
    protected userProfileBlocFonctionnelService: UserProfileBlocFonctionnelService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userProfileBlocFonctionnelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
