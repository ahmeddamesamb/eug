import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

@Component({
  standalone: true,
  templateUrl: './utilisateur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UtilisateurDeleteDialogComponent {
  utilisateur?: IUtilisateur;

  constructor(
    protected utilisateurService: UtilisateurService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilisateurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
