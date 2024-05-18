import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { TypeFraisService } from 'app/entities/microserviceGIR/type-frais/service/type-frais.service';
import { CYCLE } from 'app/entities/enumerations/cycle.model';
import { FraisService } from '../service/frais.service';
import { IFrais } from '../frais.model';
import { FraisFormService, FraisFormGroup } from './frais-form.service';

@Component({
  standalone: true,
  selector: 'jhi-frais-update',
  templateUrl: './frais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FraisUpdateComponent implements OnInit {
  isSaving = false;
  frais: IFrais | null = null;
  cYCLEValues = Object.keys(CYCLE);

  typeFraisSharedCollection: ITypeFrais[] = [];

  editForm: FraisFormGroup = this.fraisFormService.createFraisFormGroup();

  constructor(
    protected fraisService: FraisService,
    protected fraisFormService: FraisFormService,
    protected typeFraisService: TypeFraisService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeFrais = (o1: ITypeFrais | null, o2: ITypeFrais | null): boolean => this.typeFraisService.compareTypeFrais(o1, o2);

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
  }
}
