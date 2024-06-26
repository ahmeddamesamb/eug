import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEnseignement } from '../enseignement.model';
import { EnseignementService } from '../service/enseignement.service';

@Component({
  standalone: true,
  templateUrl: './enseignement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EnseignementDeleteDialogComponent {
  enseignement?: IEnseignement;

  constructor(
    protected enseignementService: EnseignementService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enseignementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
