import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeHandicap } from '../type-handicap.model';
import { TypeHandicapService } from '../service/type-handicap.service';

@Component({
  standalone: true,
  templateUrl: './type-handicap-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeHandicapDeleteDialogComponent {
  typeHandicap?: ITypeHandicap;

  constructor(
    protected typeHandicapService: TypeHandicapService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeHandicapService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
