import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDomaine } from '../domaine.model';
import { DomaineService } from '../service/domaine.service';

@Component({
  standalone: true,
  templateUrl: './domaine-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DomaineDeleteDialogComponent {
  domaine?: IDomaine;

  constructor(
    protected domaineService: DomaineService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domaineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
