import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

@Component({
  standalone: true,
  templateUrl: './annee-academique-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AnneeAcademiqueDeleteDialogComponent {
  anneeAcademique?: IAnneeAcademique;

  constructor(
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.anneeAcademiqueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
