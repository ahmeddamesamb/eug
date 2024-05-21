import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';
import { DisciplineSportiveEtudiantService } from '../service/discipline-sportive-etudiant.service';

@Component({
  standalone: true,
  templateUrl: './discipline-sportive-etudiant-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DisciplineSportiveEtudiantDeleteDialogComponent {
  disciplineSportiveEtudiant?: IDisciplineSportiveEtudiant;

  constructor(
    protected disciplineSportiveEtudiantService: DisciplineSportiveEtudiantService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disciplineSportiveEtudiantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
