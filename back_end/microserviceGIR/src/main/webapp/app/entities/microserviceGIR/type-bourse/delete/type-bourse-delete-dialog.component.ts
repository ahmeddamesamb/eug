import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeBourse } from '../type-bourse.model';
import { TypeBourseService } from '../service/type-bourse.service';

@Component({
  standalone: true,
  templateUrl: './type-bourse-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeBourseDeleteDialogComponent {
  typeBourse?: ITypeBourse;

  constructor(
    protected typeBourseService: TypeBourseService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeBourseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
