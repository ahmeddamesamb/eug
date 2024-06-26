import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IFormationAutorisee } from '../formation-autorisee.model';
import { FormationAutoriseeService } from '../service/formation-autorisee.service';
import { FormationAutoriseeFormService, FormationAutoriseeFormGroup } from './formation-autorisee-form.service';

@Component({
  standalone: true,
  selector: 'ugb-formation-autorisee-update',
  templateUrl: './formation-autorisee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationAutoriseeUpdateComponent implements OnInit {
  isSaving = false;
  formationAutorisee: IFormationAutorisee | null = null;

  formationsSharedCollection: IFormation[] = [];

  editForm: FormationAutoriseeFormGroup = this.formationAutoriseeFormService.createFormationAutoriseeFormGroup();

  constructor(
    protected formationAutoriseeService: FormationAutoriseeService,
    protected formationAutoriseeFormService: FormationAutoriseeFormService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationAutorisee }) => {
      this.formationAutorisee = formationAutorisee;
      if (formationAutorisee) {
        this.updateForm(formationAutorisee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formationAutorisee = this.formationAutoriseeFormService.getFormationAutorisee(this.editForm);
    if (formationAutorisee.id !== null) {
      this.subscribeToSaveResponse(this.formationAutoriseeService.update(formationAutorisee));
    } else {
      this.subscribeToSaveResponse(this.formationAutoriseeService.create(formationAutorisee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationAutorisee>>): void {
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

  protected updateForm(formationAutorisee: IFormationAutorisee): void {
    this.formationAutorisee = formationAutorisee;
    this.formationAutoriseeFormService.resetForm(this.editForm, formationAutorisee);

    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      ...(formationAutorisee.formations ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, ...(this.formationAutorisee?.formations ?? [])),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));
  }
}
