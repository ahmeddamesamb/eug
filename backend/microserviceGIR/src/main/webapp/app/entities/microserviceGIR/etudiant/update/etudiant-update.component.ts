import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { RegionService } from 'app/entities/microserviceGIR/region/service/region.service';
import { ITypeSelection } from 'app/entities/microserviceGIR/type-selection/type-selection.model';
import { TypeSelectionService } from 'app/entities/microserviceGIR/type-selection/service/type-selection.service';
import { ILycee } from 'app/entities/microserviceGIR/lycee/lycee.model';
import { LyceeService } from 'app/entities/microserviceGIR/lycee/service/lycee.service';
import { EtudiantService } from '../service/etudiant.service';
import { IEtudiant } from '../etudiant.model';
import { EtudiantFormService, EtudiantFormGroup } from './etudiant-form.service';

@Component({
  standalone: true,
  selector: 'ugb-etudiant-update',
  templateUrl: './etudiant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EtudiantUpdateComponent implements OnInit {
  isSaving = false;
  etudiant: IEtudiant | null = null;

  regionsSharedCollection: IRegion[] = [];
  typeSelectionsSharedCollection: ITypeSelection[] = [];
  lyceesSharedCollection: ILycee[] = [];

  editForm: EtudiantFormGroup = this.etudiantFormService.createEtudiantFormGroup();

  constructor(
    protected etudiantService: EtudiantService,
    protected etudiantFormService: EtudiantFormService,
    protected regionService: RegionService,
    protected typeSelectionService: TypeSelectionService,
    protected lyceeService: LyceeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  compareTypeSelection = (o1: ITypeSelection | null, o2: ITypeSelection | null): boolean =>
    this.typeSelectionService.compareTypeSelection(o1, o2);

  compareLycee = (o1: ILycee | null, o2: ILycee | null): boolean => this.lyceeService.compareLycee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etudiant }) => {
      this.etudiant = etudiant;
      if (etudiant) {
        this.updateForm(etudiant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etudiant = this.etudiantFormService.getEtudiant(this.editForm);
    if (etudiant.id !== null) {
      this.subscribeToSaveResponse(this.etudiantService.update(etudiant));
    } else {
      this.subscribeToSaveResponse(this.etudiantService.create(etudiant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtudiant>>): void {
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

  protected updateForm(etudiant: IEtudiant): void {
    this.etudiant = etudiant;
    this.etudiantFormService.resetForm(this.editForm, etudiant);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(
      this.regionsSharedCollection,
      etudiant.region,
    );
    this.typeSelectionsSharedCollection = this.typeSelectionService.addTypeSelectionToCollectionIfMissing<ITypeSelection>(
      this.typeSelectionsSharedCollection,
      etudiant.typeSelection,
    );
    this.lyceesSharedCollection = this.lyceeService.addLyceeToCollectionIfMissing<ILycee>(this.lyceesSharedCollection, etudiant.lycee);
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.etudiant?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));

    this.typeSelectionService
      .query()
      .pipe(map((res: HttpResponse<ITypeSelection[]>) => res.body ?? []))
      .pipe(
        map((typeSelections: ITypeSelection[]) =>
          this.typeSelectionService.addTypeSelectionToCollectionIfMissing<ITypeSelection>(typeSelections, this.etudiant?.typeSelection),
        ),
      )
      .subscribe((typeSelections: ITypeSelection[]) => (this.typeSelectionsSharedCollection = typeSelections));

    this.lyceeService
      .query()
      .pipe(map((res: HttpResponse<ILycee[]>) => res.body ?? []))
      .pipe(map((lycees: ILycee[]) => this.lyceeService.addLyceeToCollectionIfMissing<ILycee>(lycees, this.etudiant?.lycee)))
      .subscribe((lycees: ILycee[]) => (this.lyceesSharedCollection = lycees));
  }
}
