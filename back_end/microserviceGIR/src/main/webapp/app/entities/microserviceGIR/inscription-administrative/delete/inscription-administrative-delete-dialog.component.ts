import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInscriptionAdministrative } from '../inscription-administrative.model';
import { InscriptionAdministrativeService } from '../service/inscription-administrative.service';

@Component({
  standalone: true,
  templateUrl: './inscription-administrative-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InscriptionAdministrativeDeleteDialogComponent {
  inscriptionAdministrative?: IInscriptionAdministrative;

  constructor(
    protected inscriptionAdministrativeService: InscriptionAdministrativeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionAdministrativeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
