import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInfoUserRessource } from '../info-user-ressource.model';
import { InfoUserRessourceService } from '../service/info-user-ressource.service';

@Component({
  standalone: true,
  templateUrl: './info-user-ressource-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InfoUserRessourceDeleteDialogComponent {
  infoUserRessource?: IInfoUserRessource;

  constructor(
    protected infoUserRessourceService: InfoUserRessourceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infoUserRessourceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
