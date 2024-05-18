import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDisciplineSportive } from '../discipline-sportive.model';
import { DisciplineSportiveService } from '../service/discipline-sportive.service';

@Component({
  standalone: true,
  templateUrl: './discipline-sportive-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DisciplineSportiveDeleteDialogComponent {
  disciplineSportive?: IDisciplineSportive;

  constructor(
    protected disciplineSportiveService: DisciplineSportiveService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disciplineSportiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
