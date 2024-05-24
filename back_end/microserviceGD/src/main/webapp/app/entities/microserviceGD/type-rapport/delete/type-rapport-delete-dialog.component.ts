import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeRapport } from '../type-rapport.model';
import { TypeRapportService } from '../service/type-rapport.service';

@Component({
  standalone: true,
  templateUrl: './type-rapport-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeRapportDeleteDialogComponent {
  typeRapport?: ITypeRapport;

  constructor(
    protected typeRapportService: TypeRapportService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeRapportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
