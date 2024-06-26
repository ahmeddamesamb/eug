import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { CycleService } from 'app/entities/microserviceGIR/cycle/service/cycle.service';
import { INiveau } from '../niveau.model';
import { NiveauService } from '../service/niveau.service';
import { NiveauFormService, NiveauFormGroup } from './niveau-form.service';

@Component({
  standalone: true,
  selector: 'ugb-niveau-update',
  templateUrl: './niveau-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NiveauUpdateComponent implements OnInit {
  isSaving = false;
  niveau: INiveau | null = null;

  cyclesSharedCollection: ICycle[] = [];

  editForm: NiveauFormGroup = this.niveauFormService.createNiveauFormGroup();

  constructor(
    protected niveauService: NiveauService,
    protected niveauFormService: NiveauFormService,
    protected cycleService: CycleService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ niveau }) => {
      this.niveau = niveau;
      if (niveau) {
        this.updateForm(niveau);
      }

      this.loadRelationshipsOptions();
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

    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(this.cyclesSharedCollection, niveau.typeCycle);
  }

  protected loadRelationshipsOptions(): void {
    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.niveau?.typeCycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));
  }
}
