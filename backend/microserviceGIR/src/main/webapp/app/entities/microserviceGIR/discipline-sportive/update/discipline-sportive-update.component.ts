import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDisciplineSportive } from '../discipline-sportive.model';
import { DisciplineSportiveService } from '../service/discipline-sportive.service';
import { DisciplineSportiveFormService, DisciplineSportiveFormGroup } from './discipline-sportive-form.service';

@Component({
  standalone: true,
  selector: 'ugb-discipline-sportive-update',
  templateUrl: './discipline-sportive-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisciplineSportiveUpdateComponent implements OnInit {
  isSaving = false;
  disciplineSportive: IDisciplineSportive | null = null;

  editForm: DisciplineSportiveFormGroup = this.disciplineSportiveFormService.createDisciplineSportiveFormGroup();

  constructor(
    protected disciplineSportiveService: DisciplineSportiveService,
    protected disciplineSportiveFormService: DisciplineSportiveFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disciplineSportive }) => {
      this.disciplineSportive = disciplineSportive;
      if (disciplineSportive) {
        this.updateForm(disciplineSportive);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disciplineSportive = this.disciplineSportiveFormService.getDisciplineSportive(this.editForm);
    if (disciplineSportive.id !== null) {
      this.subscribeToSaveResponse(this.disciplineSportiveService.update(disciplineSportive));
    } else {
      this.subscribeToSaveResponse(this.disciplineSportiveService.create(disciplineSportive));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisciplineSportive>>): void {
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

  protected updateForm(disciplineSportive: IDisciplineSportive): void {
    this.disciplineSportive = disciplineSportive;
    this.disciplineSportiveFormService.resetForm(this.editForm, disciplineSportive);
  }
}
