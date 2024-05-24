import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlanning } from '../planning.model';
import { PlanningService } from '../service/planning.service';
import { PlanningFormService, PlanningFormGroup } from './planning-form.service';

@Component({
  standalone: true,
  selector: 'jhi-planning-update',
  templateUrl: './planning-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlanningUpdateComponent implements OnInit {
  isSaving = false;
  planning: IPlanning | null = null;

  editForm: PlanningFormGroup = this.planningFormService.createPlanningFormGroup();

  constructor(
    protected planningService: PlanningService,
    protected planningFormService: PlanningFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planning }) => {
      this.planning = planning;
      if (planning) {
        this.updateForm(planning);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planning = this.planningFormService.getPlanning(this.editForm);
    if (planning.id !== null) {
      this.subscribeToSaveResponse(this.planningService.update(planning));
    } else {
      this.subscribeToSaveResponse(this.planningService.create(planning));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanning>>): void {
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

  protected updateForm(planning: IPlanning): void {
    this.planning = planning;
    this.planningFormService.resetForm(this.editForm, planning);
  }
}
