import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRessource } from '../ressource.model';
import { RessourceService } from '../service/ressource.service';

@Component({
  standalone: true,
  templateUrl: './ressource-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RessourceDeleteDialogComponent {
  ressource?: IRessource;

  constructor(
    protected ressourceService: RessourceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ressourceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
