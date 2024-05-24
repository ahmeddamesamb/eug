import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMinistere } from '../ministere.model';
import { MinistereService } from '../service/ministere.service';
import { MinistereFormService, MinistereFormGroup } from './ministere-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ministere-update',
  templateUrl: './ministere-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MinistereUpdateComponent implements OnInit {
  isSaving = false;
  ministere: IMinistere | null = null;

  editForm: MinistereFormGroup = this.ministereFormService.createMinistereFormGroup();

  constructor(
    protected ministereService: MinistereService,
    protected ministereFormService: MinistereFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ministere }) => {
      this.ministere = ministere;
      if (ministere) {
        this.updateForm(ministere);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ministere = this.ministereFormService.getMinistere(this.editForm);
    if (ministere.id !== null) {
      this.subscribeToSaveResponse(this.ministereService.update(ministere));
    } else {
      this.subscribeToSaveResponse(this.ministereService.create(ministere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMinistere>>): void {
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

  protected updateForm(ministere: IMinistere): void {
    this.ministere = ministere;
    this.ministereFormService.resetForm(this.editForm, ministere);
  }
}
