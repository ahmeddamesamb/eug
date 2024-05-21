import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUFR } from '../ufr.model';
import { UFRService } from '../service/ufr.service';

@Component({
  standalone: true,
  templateUrl: './ufr-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UFRDeleteDialogComponent {
  uFR?: IUFR;

  constructor(
    protected uFRService: UFRService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uFRService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
