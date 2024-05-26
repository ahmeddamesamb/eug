import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICampagne } from '../campagne.model';
import { CampagneService } from '../service/campagne.service';
import { CampagneFormService, CampagneFormGroup } from './campagne-form.service';

@Component({
  standalone: true,
  selector: 'jhi-campagne-update',
  templateUrl: './campagne-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CampagneUpdateComponent implements OnInit {
  isSaving = false;
  campagne: ICampagne | null = null;

  editForm: CampagneFormGroup = this.campagneFormService.createCampagneFormGroup();

  constructor(
    protected campagneService: CampagneService,
    protected campagneFormService: CampagneFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campagne }) => {
      this.campagne = campagne;
      if (campagne) {
        this.updateForm(campagne);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const campagne = this.campagneFormService.getCampagne(this.editForm);
    if (campagne.id !== null) {
      this.subscribeToSaveResponse(this.campagneService.update(campagne));
    } else {
      this.subscribeToSaveResponse(this.campagneService.create(campagne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampagne>>): void {
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

  protected updateForm(campagne: ICampagne): void {
    this.campagne = campagne;
    this.campagneFormService.resetForm(this.editForm, campagne);
  }
}
