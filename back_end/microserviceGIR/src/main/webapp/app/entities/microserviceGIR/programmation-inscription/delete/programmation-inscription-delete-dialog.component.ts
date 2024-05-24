import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProgrammationInscription } from '../programmation-inscription.model';
import { ProgrammationInscriptionService } from '../service/programmation-inscription.service';

@Component({
  standalone: true,
  templateUrl: './programmation-inscription-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProgrammationInscriptionDeleteDialogComponent {
  programmationInscription?: IProgrammationInscription;

  constructor(
    protected programmationInscriptionService: ProgrammationInscriptionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.programmationInscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
