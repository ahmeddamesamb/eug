import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPays } from 'app/entities/microserviceGIR/pays/pays.model';
import { PaysService } from 'app/entities/microserviceGIR/pays/service/pays.service';
import { IRegion } from '../region.model';
import { RegionService } from '../service/region.service';
import { RegionFormService, RegionFormGroup } from './region-form.service';

@Component({
  standalone: true,
  selector: 'ugb-region-update',
  templateUrl: './region-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RegionUpdateComponent implements OnInit {
  isSaving = false;
  region: IRegion | null = null;

  paysSharedCollection: IPays[] = [];

  editForm: RegionFormGroup = this.regionFormService.createRegionFormGroup();

  constructor(
    protected regionService: RegionService,
    protected regionFormService: RegionFormService,
    protected paysService: PaysService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePays = (o1: IPays | null, o2: IPays | null): boolean => this.paysService.comparePays(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.region = region;
      if (region) {
        this.updateForm(region);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const region = this.regionFormService.getRegion(this.editForm);
    if (region.id !== null) {
      this.subscribeToSaveResponse(this.regionService.update(region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(region));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegion>>): void {
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

  protected updateForm(region: IRegion): void {
    this.region = region;
    this.regionFormService.resetForm(this.editForm, region);

    this.paysSharedCollection = this.paysService.addPaysToCollectionIfMissing<IPays>(this.paysSharedCollection, region.pays);
  }

  protected loadRelationshipsOptions(): void {
    this.paysService
      .query()
      .pipe(map((res: HttpResponse<IPays[]>) => res.body ?? []))
      .pipe(map((pays: IPays[]) => this.paysService.addPaysToCollectionIfMissing<IPays>(pays, this.region?.pays)))
      .subscribe((pays: IPays[]) => (this.paysSharedCollection = pays));
  }
}
