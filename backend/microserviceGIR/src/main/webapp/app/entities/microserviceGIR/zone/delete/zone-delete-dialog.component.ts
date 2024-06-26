import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IZone } from '../zone.model';
import { ZoneService } from '../service/zone.service';

@Component({
  standalone: true,
  templateUrl: './zone-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ZoneDeleteDialogComponent {
  zone?: IZone;

  constructor(
    protected zoneService: ZoneService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zoneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
