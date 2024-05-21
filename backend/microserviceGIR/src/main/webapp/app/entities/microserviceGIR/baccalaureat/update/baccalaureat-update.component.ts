import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { ISerie } from 'app/entities/microserviceGIR/serie/serie.model';
import { SerieService } from 'app/entities/microserviceGIR/serie/service/serie.service';
import { BaccalaureatService } from '../service/baccalaureat.service';
import { IBaccalaureat } from '../baccalaureat.model';
import { BaccalaureatFormService, BaccalaureatFormGroup } from './baccalaureat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-baccalaureat-update',
  templateUrl: './baccalaureat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BaccalaureatUpdateComponent implements OnInit {
  isSaving = false;
  baccalaureat: IBaccalaureat | null = null;

  etudiantsCollection: IEtudiant[] = [];
  seriesSharedCollection: ISerie[] = [];

  editForm: BaccalaureatFormGroup = this.baccalaureatFormService.createBaccalaureatFormGroup();

  constructor(
    protected baccalaureatService: BaccalaureatService,
    protected baccalaureatFormService: BaccalaureatFormService,
    protected etudiantService: EtudiantService,
    protected serieService: SerieService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  compareSerie = (o1: ISerie | null, o2: ISerie | null): boolean => this.serieService.compareSerie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ baccalaureat }) => {
      this.baccalaureat = baccalaureat;
      if (baccalaureat) {
        this.updateForm(baccalaureat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const baccalaureat = this.baccalaureatFormService.getBaccalaureat(this.editForm);
    if (baccalaureat.id !== null) {
      this.subscribeToSaveResponse(this.baccalaureatService.update(baccalaureat));
    } else {
      this.subscribeToSaveResponse(this.baccalaureatService.create(baccalaureat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBaccalaureat>>): void {
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

  protected updateForm(baccalaureat: IBaccalaureat): void {
    this.baccalaureat = baccalaureat;
    this.baccalaureatFormService.resetForm(this.editForm, baccalaureat);

    this.etudiantsCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsCollection,
      baccalaureat.etudiant,
    );
    this.seriesSharedCollection = this.serieService.addSerieToCollectionIfMissing<ISerie>(this.seriesSharedCollection, baccalaureat.serie);
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query({ filter: 'baccalaureat-is-null' })
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.baccalaureat?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsCollection = etudiants));

    this.serieService
      .query()
      .pipe(map((res: HttpResponse<ISerie[]>) => res.body ?? []))
      .pipe(map((series: ISerie[]) => this.serieService.addSerieToCollectionIfMissing<ISerie>(series, this.baccalaureat?.serie)))
      .subscribe((series: ISerie[]) => (this.seriesSharedCollection = series));
  }
}
