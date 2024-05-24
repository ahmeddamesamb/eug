import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEnseignement } from '../enseignement.model';
import { EnseignementService } from '../service/enseignement.service';
import { EnseignementFormService, EnseignementFormGroup } from './enseignement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-enseignement-update',
  templateUrl: './enseignement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnseignementUpdateComponent implements OnInit {
  isSaving = false;
  enseignement: IEnseignement | null = null;

  editForm: EnseignementFormGroup = this.enseignementFormService.createEnseignementFormGroup();

  constructor(
    protected enseignementService: EnseignementService,
    protected enseignementFormService: EnseignementFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enseignement }) => {
      this.enseignement = enseignement;
      if (enseignement) {
        this.updateForm(enseignement);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enseignement = this.enseignementFormService.getEnseignement(this.editForm);
    if (enseignement.id !== null) {
      this.subscribeToSaveResponse(this.enseignementService.update(enseignement));
    } else {
      this.subscribeToSaveResponse(this.enseignementService.create(enseignement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnseignement>>): void {
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

  protected updateForm(enseignement: IEnseignement): void {
    this.enseignement = enseignement;
    this.enseignementFormService.resetForm(this.editForm, enseignement);
  }
}
