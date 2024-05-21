import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISerie } from '../serie.model';
import { SerieService } from '../service/serie.service';
import { SerieFormService, SerieFormGroup } from './serie-form.service';

@Component({
  standalone: true,
  selector: 'jhi-serie-update',
  templateUrl: './serie-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SerieUpdateComponent implements OnInit {
  isSaving = false;
  serie: ISerie | null = null;

  editForm: SerieFormGroup = this.serieFormService.createSerieFormGroup();

  constructor(
    protected serieService: SerieService,
    protected serieFormService: SerieFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serie }) => {
      this.serie = serie;
      if (serie) {
        this.updateForm(serie);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serie = this.serieFormService.getSerie(this.editForm);
    if (serie.id !== null) {
      this.subscribeToSaveResponse(this.serieService.update(serie));
    } else {
      this.subscribeToSaveResponse(this.serieService.create(serie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISerie>>): void {
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

  protected updateForm(serie: ISerie): void {
    this.serie = serie;
    this.serieFormService.resetForm(this.editForm, serie);
  }
}
