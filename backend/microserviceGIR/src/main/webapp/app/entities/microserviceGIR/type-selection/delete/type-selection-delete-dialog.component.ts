import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeSelection } from '../type-selection.model';
import { TypeSelectionService } from '../service/type-selection.service';

@Component({
  standalone: true,
  templateUrl: './type-selection-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeSelectionDeleteDialogComponent {
  typeSelection?: ITypeSelection;

  constructor(
    protected typeSelectionService: TypeSelectionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeSelectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
