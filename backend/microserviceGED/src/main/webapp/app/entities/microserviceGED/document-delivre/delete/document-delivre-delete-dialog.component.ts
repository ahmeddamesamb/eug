import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocumentDelivre } from '../document-delivre.model';
import { DocumentDelivreService } from '../service/document-delivre.service';

@Component({
  standalone: true,
  templateUrl: './document-delivre-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentDelivreDeleteDialogComponent {
  documentDelivre?: IDocumentDelivre;

  constructor(
    protected documentDelivreService: DocumentDelivreService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentDelivreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
