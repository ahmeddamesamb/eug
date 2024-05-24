import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPaiementFrais } from '../paiement-frais.model';
import { PaiementFraisService } from '../service/paiement-frais.service';

@Component({
  standalone: true,
  templateUrl: './paiement-frais-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PaiementFraisDeleteDialogComponent {
  paiementFrais?: IPaiementFrais;

  constructor(
    protected paiementFraisService: PaiementFraisService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paiementFraisService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
