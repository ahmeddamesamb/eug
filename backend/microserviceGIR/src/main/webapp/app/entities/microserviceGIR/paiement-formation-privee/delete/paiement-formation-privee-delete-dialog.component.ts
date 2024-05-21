import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import { PaiementFormationPriveeService } from '../service/paiement-formation-privee.service';

@Component({
  standalone: true,
  templateUrl: './paiement-formation-privee-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PaiementFormationPriveeDeleteDialogComponent {
  paiementFormationPrivee?: IPaiementFormationPrivee;

  constructor(
    protected paiementFormationPriveeService: PaiementFormationPriveeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementFormationPriveeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
