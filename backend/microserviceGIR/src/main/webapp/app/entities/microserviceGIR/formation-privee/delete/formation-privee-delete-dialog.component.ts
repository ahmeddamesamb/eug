import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFormationPrivee } from '../formation-privee.model';
import { FormationPriveeService } from '../service/formation-privee.service';

@Component({
  standalone: true,
  templateUrl: './formation-privee-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FormationPriveeDeleteDialogComponent {
  formationPrivee?: IFormationPrivee;

  constructor(
    protected formationPriveeService: FormationPriveeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationPriveeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
