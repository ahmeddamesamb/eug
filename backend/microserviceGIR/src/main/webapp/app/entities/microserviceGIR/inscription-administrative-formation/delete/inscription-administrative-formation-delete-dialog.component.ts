import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from '../service/inscription-administrative-formation.service';

@Component({
  standalone: true,
  templateUrl: './inscription-administrative-formation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InscriptionAdministrativeFormationDeleteDialogComponent {
  inscriptionAdministrativeFormation?: IInscriptionAdministrativeFormation;

  constructor(
    protected inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionAdministrativeFormationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
