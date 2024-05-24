import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeAdmission } from '../type-admission.model';
import { TypeAdmissionService } from '../service/type-admission.service';

@Component({
  standalone: true,
  templateUrl: './type-admission-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeAdmissionDeleteDialogComponent {
  typeAdmission?: ITypeAdmission;

  constructor(
    protected typeAdmissionService: TypeAdmissionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeAdmissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
