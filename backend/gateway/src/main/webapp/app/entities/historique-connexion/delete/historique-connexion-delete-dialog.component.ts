import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHistoriqueConnexion } from '../historique-connexion.model';
import { HistoriqueConnexionService } from '../service/historique-connexion.service';

@Component({
  standalone: true,
  templateUrl: './historique-connexion-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HistoriqueConnexionDeleteDialogComponent {
  historiqueConnexion?: IHistoriqueConnexion;

  constructor(
    protected historiqueConnexionService: HistoriqueConnexionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historiqueConnexionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
