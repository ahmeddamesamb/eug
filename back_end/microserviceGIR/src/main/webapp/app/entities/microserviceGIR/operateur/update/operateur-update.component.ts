import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOperateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';
import { OperateurFormService, OperateurFormGroup } from './operateur-form.service';

@Component({
  standalone: true,
  selector: 'jhi-operateur-update',
  templateUrl: './operateur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OperateurUpdateComponent implements OnInit {
  isSaving = false;
  operateur: IOperateur | null = null;

  editForm: OperateurFormGroup = this.operateurFormService.createOperateurFormGroup();

  constructor(
    protected operateurService: OperateurService,
    protected operateurFormService: OperateurFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operateur }) => {
      this.operateur = operateur;
      if (operateur) {
        this.updateForm(operateur);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operateur = this.operateurFormService.getOperateur(this.editForm);
    if (operateur.id !== null) {
      this.subscribeToSaveResponse(this.operateurService.update(operateur));
    } else {
      this.subscribeToSaveResponse(this.operateurService.create(operateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperateur>>): void {
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

  protected updateForm(operateur: IOperateur): void {
    this.operateur = operateur;
    this.operateurFormService.resetForm(this.editForm, operateur);
  }
}
