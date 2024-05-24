import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFormationInvalide } from '../formation-invalide.model';
import { FormationInvalideService } from '../service/formation-invalide.service';

@Component({
  standalone: true,
  templateUrl: './formation-invalide-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FormationInvalideDeleteDialogComponent {
  formationInvalide?: IFormationInvalide;

  constructor(
    protected formationInvalideService: FormationInvalideService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationInvalideService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
