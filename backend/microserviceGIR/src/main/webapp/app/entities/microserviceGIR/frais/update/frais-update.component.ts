import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { TypeFraisService } from 'app/entities/microserviceGIR/type-frais/service/type-frais.service';
import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { CycleService } from 'app/entities/microserviceGIR/cycle/service/cycle.service';
import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { FraisService } from '../service/frais.service';
import { IFrais } from '../frais.model';
import { FraisFormService, FraisFormGroup } from './frais-form.service';

@Component({
  standalone: true,
  selector: 'ugb-frais-update',
  templateUrl: './frais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FraisUpdateComponent implements OnInit {
  isSaving = false;
  frais: IFrais | null = null;

  typeFraisSharedCollection: ITypeFrais[] = [];
  cyclesSharedCollection: ICycle[] = [];
  universitesSharedCollection: IUniversite[] = [];

  editForm: FraisFormGroup = this.fraisFormService.createFraisFormGroup();

  constructor(
    protected fraisService: FraisService,
    protected fraisFormService: FraisFormService,
    protected typeFraisService: TypeFraisService,
    protected cycleService: CycleService,
    protected universiteService: UniversiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeFrais = (o1: ITypeFrais | null, o2: ITypeFrais | null): boolean => this.typeFraisService.compareTypeFrais(o1, o2);

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  compareUniversite = (o1: IUniversite | null, o2: IUniversite | null): boolean => this.universiteService.compareUniversite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frais }) => {
      this.frais = frais;
      if (frais) {
        this.updateForm(frais);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frais = this.fraisFormService.getFrais(this.editForm);
    if (frais.id !== null) {
      this.subscribeToSaveResponse(this.fraisService.update(frais));
    } else {
      this.subscribeToSaveResponse(this.fraisService.create(frais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrais>>): void {
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

  protected updateForm(frais: IFrais): void {
    this.frais = frais;
    this.fraisFormService.resetForm(this.editForm, frais);

    this.typeFraisSharedCollection = this.typeFraisService.addTypeFraisToCollectionIfMissing<ITypeFrais>(
      this.typeFraisSharedCollection,
      frais.typeFrais,
    );
    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(this.cyclesSharedCollection, frais.typeCycle);
    this.universitesSharedCollection = this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(
      this.universitesSharedCollection,
      ...(frais.universites ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeFraisService
      .query()
      .pipe(map((res: HttpResponse<ITypeFrais[]>) => res.body ?? []))
      .pipe(
        map((typeFrais: ITypeFrais[]) =>
          this.typeFraisService.addTypeFraisToCollectionIfMissing<ITypeFrais>(typeFrais, this.frais?.typeFrais),
        ),
      )
      .subscribe((typeFrais: ITypeFrais[]) => (this.typeFraisSharedCollection = typeFrais));

    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.frais?.typeCycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));

    this.universiteService
      .query()
      .pipe(map((res: HttpResponse<IUniversite[]>) => res.body ?? []))
      .pipe(
        map((universites: IUniversite[]) =>
          this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(universites, ...(this.frais?.universites ?? [])),
        ),
      )
      .subscribe((universites: IUniversite[]) => (this.universitesSharedCollection = universites));
  }
}
