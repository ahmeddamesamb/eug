import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInformationImage } from '../information-image.model';
import { InformationImageService } from '../service/information-image.service';

@Component({
  standalone: true,
  templateUrl: './information-image-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InformationImageDeleteDialogComponent {
  informationImage?: IInformationImage;

  constructor(
    protected informationImageService: InformationImageService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.informationImageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
