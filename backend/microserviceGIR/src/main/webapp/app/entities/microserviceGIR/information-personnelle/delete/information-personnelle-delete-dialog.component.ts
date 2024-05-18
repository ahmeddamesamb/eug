import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInformationPersonnelle } from '../information-personnelle.model';
import { InformationPersonnelleService } from '../service/information-personnelle.service';

@Component({
  standalone: true,
  templateUrl: './information-personnelle-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InformationPersonnelleDeleteDialogComponent {
  informationPersonnelle?: IInformationPersonnelle;

  constructor(
    protected informationPersonnelleService: InformationPersonnelleService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.informationPersonnelleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
