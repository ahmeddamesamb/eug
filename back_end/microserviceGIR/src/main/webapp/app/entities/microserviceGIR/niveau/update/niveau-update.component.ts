import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Cycle } from 'app/entities/enumerations/cycle.model';
import { INiveau } from '../niveau.model';
import { NiveauService } from '../service/niveau.service';
import { NiveauFormService, NiveauFormGroup } from './niveau-form.service';

@Component({
  standalone: true,
  selector: 'jhi-niveau-update',
  templateUrl: './niveau-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NiveauUpdateComponent implements OnInit {
  isSaving = false;
  niveau: INiveau | null = null;
  cycleValues = Object.keys(Cycle);

  editForm: NiveauFormGroup = this.niveauFormService.createNiveauFormGroup();

  constructor(
    protected niveauService: NiveauService,
    protected niveauFormService: NiveauFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ niveau }) => {
      this.niveau = niveau;
      if (niveau) {
        this.updateForm(niveau);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const niveau = this.niveauFormService.getNiveau(this.editForm);
    if (niveau.id !== null) {
      this.subscribeToSaveResponse(this.niveauService.update(niveau));
    } else {
      this.subscribeToSaveResponse(this.niveauService.create(niveau));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INiveau>>): void {
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

  protected updateForm(niveau: INiveau): void {
    this.niveau = niveau;
    this.niveauFormService.resetForm(this.editForm, niveau);
  }
}
