import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMention } from '../mention.model';
import { MentionService } from '../service/mention.service';

@Component({
  standalone: true,
  templateUrl: './mention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MentionDeleteDialogComponent {
  mention?: IMention;

  constructor(
    protected mentionService: MentionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mentionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
