import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { FormationInvalideService } from '../service/formation-invalide.service';
import { IFormationInvalide } from '../formation-invalide.model';
import { FormationInvalideFormService, FormationInvalideFormGroup } from './formation-invalide-form.service';

@Component({
  standalone: true,
  selector: 'ugb-formation-invalide-update',
  templateUrl: './formation-invalide-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationInvalideUpdateComponent implements OnInit {
  isSaving = false;
  formationInvalide: IFormationInvalide | null = null;

  formationsSharedCollection: IFormation[] = [];
  anneeAcademiquesSharedCollection: IAnneeAcademique[] = [];

  editForm: FormationInvalideFormGroup = this.formationInvalideFormService.createFormationInvalideFormGroup();

  constructor(
    protected formationInvalideService: FormationInvalideService,
    protected formationInvalideFormService: FormationInvalideFormService,
    protected formationService: FormationService,
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  compareAnneeAcademique = (o1: IAnneeAcademique | null, o2: IAnneeAcademique | null): boolean =>
    this.anneeAcademiqueService.compareAnneeAcademique(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationInvalide }) => {
      this.formationInvalide = formationInvalide;
      if (formationInvalide) {
        this.updateForm(formationInvalide);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formationInvalide = this.formationInvalideFormService.getFormationInvalide(this.editForm);
    if (formationInvalide.id !== null) {
      this.subscribeToSaveResponse(this.formationInvalideService.update(formationInvalide));
    } else {
      this.subscribeToSaveResponse(this.formationInvalideService.create(formationInvalide));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationInvalide>>): void {
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

  protected updateForm(formationInvalide: IFormationInvalide): void {
    this.formationInvalide = formationInvalide;
    this.formationInvalideFormService.resetForm(this.editForm, formationInvalide);

    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      formationInvalide.formation,
    );
    this.anneeAcademiquesSharedCollection = this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
      this.anneeAcademiquesSharedCollection,
      formationInvalide.anneeAcademique,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.formationInvalide?.formation),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));

    this.anneeAcademiqueService
      .query()
      .pipe(map((res: HttpResponse<IAnneeAcademique[]>) => res.body ?? []))
      .pipe(
        map((anneeAcademiques: IAnneeAcademique[]) =>
          this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
            anneeAcademiques,
            this.formationInvalide?.anneeAcademique,
          ),
        ),
      )
      .subscribe((anneeAcademiques: IAnneeAcademique[]) => (this.anneeAcademiquesSharedCollection = anneeAcademiques));
  }
}
