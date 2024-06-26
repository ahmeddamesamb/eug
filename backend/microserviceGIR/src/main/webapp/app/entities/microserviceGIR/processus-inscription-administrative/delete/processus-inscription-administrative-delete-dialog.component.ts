import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import { ProcessusInscriptionAdministrativeService } from '../service/processus-inscription-administrative.service';

@Component({
  standalone: true,
  templateUrl: './processus-inscription-administrative-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProcessusInscriptionAdministrativeDeleteDialogComponent {
  processusInscriptionAdministrative?: IProcessusInscriptionAdministrative;

  constructor(
    protected processusInscriptionAdministrativeService: ProcessusInscriptionAdministrativeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.processusInscriptionAdministrativeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
