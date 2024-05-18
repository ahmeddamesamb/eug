import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISpecialite } from '../specialite.model';
import { SpecialiteService } from '../service/specialite.service';

@Component({
  standalone: true,
  templateUrl: './specialite-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SpecialiteDeleteDialogComponent {
  specialite?: ISpecialite;

  constructor(
    protected specialiteService: SpecialiteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specialiteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
