import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBaccalaureat } from '../baccalaureat.model';
import { BaccalaureatService } from '../service/baccalaureat.service';

@Component({
  standalone: true,
  templateUrl: './baccalaureat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BaccalaureatDeleteDialogComponent {
  baccalaureat?: IBaccalaureat;

  constructor(
    protected baccalaureatService: BaccalaureatService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.baccalaureatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
