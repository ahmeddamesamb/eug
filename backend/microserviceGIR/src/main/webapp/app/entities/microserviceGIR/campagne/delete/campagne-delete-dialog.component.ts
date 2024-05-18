import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICampagne } from '../campagne.model';
import { CampagneService } from '../service/campagne.service';

@Component({
  standalone: true,
  templateUrl: './campagne-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CampagneDeleteDialogComponent {
  campagne?: ICampagne;

  constructor(
    protected campagneService: CampagneService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.campagneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
