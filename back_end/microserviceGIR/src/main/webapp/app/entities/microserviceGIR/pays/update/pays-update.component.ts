import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IZone } from 'app/entities/microserviceGIR/zone/zone.model';
import { ZoneService } from 'app/entities/microserviceGIR/zone/service/zone.service';
import { IPays } from '../pays.model';
import { PaysService } from '../service/pays.service';
import { PaysFormService, PaysFormGroup } from './pays-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pays-update',
  templateUrl: './pays-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaysUpdateComponent implements OnInit {
  isSaving = false;
  pays: IPays | null = null;

  zonesSharedCollection: IZone[] = [];

  editForm: PaysFormGroup = this.paysFormService.createPaysFormGroup();

  constructor(
    protected paysService: PaysService,
    protected paysFormService: PaysFormService,
    protected zoneService: ZoneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareZone = (o1: IZone | null, o2: IZone | null): boolean => this.zoneService.compareZone(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pays }) => {
      this.pays = pays;
      if (pays) {
        this.updateForm(pays);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pays = this.paysFormService.getPays(this.editForm);
    if (pays.id !== null) {
      this.subscribeToSaveResponse(this.paysService.update(pays));
    } else {
      this.subscribeToSaveResponse(this.paysService.create(pays));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPays>>): void {
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

  protected updateForm(pays: IPays): void {
    this.pays = pays;
    this.paysFormService.resetForm(this.editForm, pays);

    this.zonesSharedCollection = this.zoneService.addZoneToCollectionIfMissing<IZone>(this.zonesSharedCollection, ...(pays.zones ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.zoneService
      .query()
      .pipe(map((res: HttpResponse<IZone[]>) => res.body ?? []))
      .pipe(map((zones: IZone[]) => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, ...(this.pays?.zones ?? []))))
      .subscribe((zones: IZone[]) => (this.zonesSharedCollection = zones));
  }
}
