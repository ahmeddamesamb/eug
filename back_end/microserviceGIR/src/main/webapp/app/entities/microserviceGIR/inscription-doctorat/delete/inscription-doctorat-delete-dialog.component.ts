import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { InscriptionDoctoratService } from '../service/inscription-doctorat.service';

@Component({
  standalone: true,
  templateUrl: './inscription-doctorat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InscriptionDoctoratDeleteDialogComponent {
  inscriptionDoctorat?: IInscriptionDoctorat;

  constructor(
    protected inscriptionDoctoratService: InscriptionDoctoratService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionDoctoratService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
