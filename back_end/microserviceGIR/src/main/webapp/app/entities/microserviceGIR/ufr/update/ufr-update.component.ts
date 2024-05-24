import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { IUfr } from '../ufr.model';
import { UfrService } from '../service/ufr.service';
import { UfrFormService, UfrFormGroup } from './ufr-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ufr-update',
  templateUrl: './ufr-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UfrUpdateComponent implements OnInit {
  isSaving = false;
  ufr: IUfr | null = null;

  universitesSharedCollection: IUniversite[] = [];

  editForm: UfrFormGroup = this.ufrFormService.createUfrFormGroup();

  constructor(
    protected ufrService: UfrService,
    protected ufrFormService: UfrFormService,
    protected universiteService: UniversiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUniversite = (o1: IUniversite | null, o2: IUniversite | null): boolean => this.universiteService.compareUniversite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ufr }) => {
      this.ufr = ufr;
      if (ufr) {
        this.updateForm(ufr);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ufr = this.ufrFormService.getUfr(this.editForm);
    if (ufr.id !== null) {
      this.subscribeToSaveResponse(this.ufrService.update(ufr));
    } else {
      this.subscribeToSaveResponse(this.ufrService.create(ufr));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUfr>>): void {
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

  protected updateForm(ufr: IUfr): void {
    this.ufr = ufr;
    this.ufrFormService.resetForm(this.editForm, ufr);

    this.universitesSharedCollection = this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(
      this.universitesSharedCollection,
      ufr.universite,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.universiteService
      .query()
      .pipe(map((res: HttpResponse<IUniversite[]>) => res.body ?? []))
      .pipe(
        map((universites: IUniversite[]) =>
          this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(universites, this.ufr?.universite),
        ),
      )
      .subscribe((universites: IUniversite[]) => (this.universitesSharedCollection = universites));
  }
}
