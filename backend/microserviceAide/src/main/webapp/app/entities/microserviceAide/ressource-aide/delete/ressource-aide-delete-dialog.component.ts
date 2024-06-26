import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRessourceAide } from '../ressource-aide.model';
import { RessourceAideService } from '../service/ressource-aide.service';

@Component({
  standalone: true,
  templateUrl: './ressource-aide-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RessourceAideDeleteDialogComponent {
  ressourceAide?: IRessourceAide;

  constructor(
    protected ressourceAideService: RessourceAideService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ressourceAideService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
