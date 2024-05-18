import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { RegionService } from 'app/entities/microserviceGIR/region/service/region.service';
import { ILycee } from '../lycee.model';
import { LyceeService } from '../service/lycee.service';
import { LyceeFormService, LyceeFormGroup } from './lycee-form.service';

@Component({
  standalone: true,
  selector: 'jhi-lycee-update',
  templateUrl: './lycee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LyceeUpdateComponent implements OnInit {
  isSaving = false;
  lycee: ILycee | null = null;

  regionsSharedCollection: IRegion[] = [];

  editForm: LyceeFormGroup = this.lyceeFormService.createLyceeFormGroup();

  constructor(
    protected lyceeService: LyceeService,
    protected lyceeFormService: LyceeFormService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lycee }) => {
      this.lycee = lycee;
      if (lycee) {
        this.updateForm(lycee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lycee = this.lyceeFormService.getLycee(this.editForm);
    if (lycee.id !== null) {
      this.subscribeToSaveResponse(this.lyceeService.update(lycee));
    } else {
      this.subscribeToSaveResponse(this.lyceeService.create(lycee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILycee>>): void {
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

  protected updateForm(lycee: ILycee): void {
    this.lycee = lycee;
    this.lyceeFormService.resetForm(this.editForm, lycee);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(this.regionsSharedCollection, lycee.region);
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.lycee?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }
}
