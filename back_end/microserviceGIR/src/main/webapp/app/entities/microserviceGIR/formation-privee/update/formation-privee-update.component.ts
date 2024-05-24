import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IFormationPrivee } from '../formation-privee.model';
import { FormationPriveeService } from '../service/formation-privee.service';
import { FormationPriveeFormService, FormationPriveeFormGroup } from './formation-privee-form.service';

@Component({
  standalone: true,
  selector: 'jhi-formation-privee-update',
  templateUrl: './formation-privee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationPriveeUpdateComponent implements OnInit {
  isSaving = false;
  formationPrivee: IFormationPrivee | null = null;

  formationsCollection: IFormation[] = [];

  editForm: FormationPriveeFormGroup = this.formationPriveeFormService.createFormationPriveeFormGroup();

  constructor(
    protected formationPriveeService: FormationPriveeService,
    protected formationPriveeFormService: FormationPriveeFormService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationPrivee }) => {
      this.formationPrivee = formationPrivee;
      if (formationPrivee) {
        this.updateForm(formationPrivee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formationPrivee = this.formationPriveeFormService.getFormationPrivee(this.editForm);
    if (formationPrivee.id !== null) {
      this.subscribeToSaveResponse(this.formationPriveeService.update(formationPrivee));
    } else {
      this.subscribeToSaveResponse(this.formationPriveeService.create(formationPrivee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationPrivee>>): void {
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

  protected updateForm(formationPrivee: IFormationPrivee): void {
    this.formationPrivee = formationPrivee;
    this.formationPriveeFormService.resetForm(this.editForm, formationPrivee);

    this.formationsCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsCollection,
      formationPrivee.formation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query({ filter: 'formationprivee-is-null' })
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.formationPrivee?.formation),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsCollection = formations));
  }
}
