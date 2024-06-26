import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBlocFonctionnel } from '../bloc-fonctionnel.model';
import { BlocFonctionnelService } from '../service/bloc-fonctionnel.service';

@Component({
  standalone: true,
  templateUrl: './bloc-fonctionnel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BlocFonctionnelDeleteDialogComponent {
  blocFonctionnel?: IBlocFonctionnel;

  constructor(
    protected blocFonctionnelService: BlocFonctionnelService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blocFonctionnelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
