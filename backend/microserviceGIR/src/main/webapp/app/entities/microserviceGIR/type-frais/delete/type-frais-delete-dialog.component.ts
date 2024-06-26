import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeFrais } from '../type-frais.model';
import { TypeFraisService } from '../service/type-frais.service';

@Component({
  standalone: true,
  templateUrl: './type-frais-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeFraisDeleteDialogComponent {
  typeFrais?: ITypeFrais;

  constructor(
    protected typeFraisService: TypeFraisService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeFraisService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
