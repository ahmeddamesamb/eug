import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEnseignant } from '../enseignant.model';
import { EnseignantService } from '../service/enseignant.service';

@Component({
  standalone: true,
  templateUrl: './enseignant-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EnseignantDeleteDialogComponent {
  enseignant?: IEnseignant;

  constructor(
    protected enseignantService: EnseignantService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enseignantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
