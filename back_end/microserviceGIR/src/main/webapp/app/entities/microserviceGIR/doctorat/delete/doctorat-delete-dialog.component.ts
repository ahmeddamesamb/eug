import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDoctorat } from '../doctorat.model';
import { DoctoratService } from '../service/doctorat.service';

@Component({
  standalone: true,
  templateUrl: './doctorat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DoctoratDeleteDialogComponent {
  doctorat?: IDoctorat;

  constructor(
    protected doctoratService: DoctoratService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.doctoratService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
