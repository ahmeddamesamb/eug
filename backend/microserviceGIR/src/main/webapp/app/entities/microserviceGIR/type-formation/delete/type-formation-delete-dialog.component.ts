import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeFormation } from '../type-formation.model';
import { TypeFormationService } from '../service/type-formation.service';

@Component({
  standalone: true,
  templateUrl: './type-formation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeFormationDeleteDialogComponent {
  typeFormation?: ITypeFormation;

  constructor(
    protected typeFormationService: TypeFormationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeFormationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
