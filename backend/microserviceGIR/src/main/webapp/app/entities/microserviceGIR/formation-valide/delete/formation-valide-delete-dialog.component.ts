import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFormationValide } from '../formation-valide.model';
import { FormationValideService } from '../service/formation-valide.service';

@Component({
  standalone: true,
  templateUrl: './formation-valide-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FormationValideDeleteDialogComponent {
  formationValide?: IFormationValide;

  constructor(
    protected formationValideService: FormationValideService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationValideService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
