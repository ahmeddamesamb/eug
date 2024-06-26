import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceUser } from '../service-user.model';
import { ServiceUserService } from '../service/service-user.service';

@Component({
  standalone: true,
  templateUrl: './service-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceUserDeleteDialogComponent {
  serviceUser?: IServiceUser;

  constructor(
    protected serviceUserService: ServiceUserService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
