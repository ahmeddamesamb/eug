import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISerie } from '../serie.model';
import { SerieService } from '../service/serie.service';

@Component({
  standalone: true,
  templateUrl: './serie-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SerieDeleteDialogComponent {
  serie?: ISerie;

  constructor(
    protected serieService: SerieService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
