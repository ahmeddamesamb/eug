import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';
import { AnneeAcademiqueFormService, AnneeAcademiqueFormGroup } from './annee-academique-form.service';

@Component({
  standalone: true,
  selector: 'ugb-annee-academique-update',
  templateUrl: './annee-academique-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnneeAcademiqueUpdateComponent implements OnInit {
  isSaving = false;
  anneeAcademique: IAnneeAcademique | null = null;

  editForm: AnneeAcademiqueFormGroup = this.anneeAcademiqueFormService.createAnneeAcademiqueFormGroup();

  constructor(
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected anneeAcademiqueFormService: AnneeAcademiqueFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anneeAcademique }) => {
      this.anneeAcademique = anneeAcademique;
      if (anneeAcademique) {
        this.updateForm(anneeAcademique);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const anneeAcademique = this.anneeAcademiqueFormService.getAnneeAcademique(this.editForm);
    if (anneeAcademique.id !== null) {
      this.subscribeToSaveResponse(this.anneeAcademiqueService.update(anneeAcademique));
    } else {
      this.subscribeToSaveResponse(this.anneeAcademiqueService.create(anneeAcademique));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnneeAcademique>>): void {
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

  protected updateForm(anneeAcademique: IAnneeAcademique): void {
    this.anneeAcademique = anneeAcademique;
    this.anneeAcademiqueFormService.resetForm(this.editForm, anneeAcademique);
  }
}
