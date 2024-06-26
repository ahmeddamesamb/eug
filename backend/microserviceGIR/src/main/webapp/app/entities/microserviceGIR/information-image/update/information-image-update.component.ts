import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInformationImage } from '../information-image.model';
import { InformationImageService } from '../service/information-image.service';
import { InformationImageFormService, InformationImageFormGroup } from './information-image-form.service';

@Component({
  standalone: true,
  selector: 'ugb-information-image-update',
  templateUrl: './information-image-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InformationImageUpdateComponent implements OnInit {
  isSaving = false;
  informationImage: IInformationImage | null = null;

  editForm: InformationImageFormGroup = this.informationImageFormService.createInformationImageFormGroup();

  constructor(
    protected informationImageService: InformationImageService,
    protected informationImageFormService: InformationImageFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ informationImage }) => {
      this.informationImage = informationImage;
      if (informationImage) {
        this.updateForm(informationImage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const informationImage = this.informationImageFormService.getInformationImage(this.editForm);
    if (informationImage.id !== null) {
      this.subscribeToSaveResponse(this.informationImageService.update(informationImage));
    } else {
      this.subscribeToSaveResponse(this.informationImageService.create(informationImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInformationImage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(informationImage: IInformationImage): void {
    this.informationImage = informationImage;
    this.informationImageFormService.resetForm(this.editForm, informationImage);
  }
}
