import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFrais } from '../frais.model';
import { FraisService } from '../service/frais.service';

@Component({
  standalone: true,
  templateUrl: './frais-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FraisDeleteDialogComponent {
  frais?: IFrais;

  constructor(
    protected fraisService: FraisService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fraisService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
