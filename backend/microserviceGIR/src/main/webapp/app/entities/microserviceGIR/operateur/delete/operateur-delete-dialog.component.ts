import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOperateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';

@Component({
  standalone: true,
  templateUrl: './operateur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OperateurDeleteDialogComponent {
  operateur?: IOperateur;

  constructor(
    protected operateurService: OperateurService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operateurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
