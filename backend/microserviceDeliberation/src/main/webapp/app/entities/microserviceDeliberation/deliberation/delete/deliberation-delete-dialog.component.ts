import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDeliberation } from '../deliberation.model';
import { DeliberationService } from '../service/deliberation.service';

@Component({
  standalone: true,
  templateUrl: './deliberation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DeliberationDeleteDialogComponent {
  deliberation?: IDeliberation;

  constructor(
    protected deliberationService: DeliberationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliberationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
