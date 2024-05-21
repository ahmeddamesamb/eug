import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IFormationValide } from '../formation-valide.model';
import { FormationValideService } from '../service/formation-valide.service';
import { FormationValideFormService, FormationValideFormGroup } from './formation-valide-form.service';

@Component({
  standalone: true,
  selector: 'jhi-formation-valide-update',
  templateUrl: './formation-valide-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationValideUpdateComponent implements OnInit {
  isSaving = false;
  formationValide: IFormationValide | null = null;

  formationsSharedCollection: IFormation[] = [];

  editForm: FormationValideFormGroup = this.formationValideFormService.createFormationValideFormGroup();

  constructor(
    protected formationValideService: FormationValideService,
    protected formationValideFormService: FormationValideFormService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationValide }) => {
      this.formationValide = formationValide;
      if (formationValide) {
        this.updateForm(formationValide);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formationValide = this.formationValideFormService.getFormationValide(this.editForm);
    if (formationValide.id !== null) {
      this.subscribeToSaveResponse(this.formationValideService.update(formationValide));
    } else {
      this.subscribeToSaveResponse(this.formationValideService.create(formationValide));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationValide>>): void {
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

  protected updateForm(formationValide: IFormationValide): void {
    this.formationValide = formationValide;
    this.formationValideFormService.resetForm(this.editForm, formationValide);

    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      formationValide.formation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.formationValide?.formation),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));
  }
}
