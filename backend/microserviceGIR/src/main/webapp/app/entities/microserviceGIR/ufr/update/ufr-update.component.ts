import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { IUFR } from '../ufr.model';
import { UFRService } from '../service/ufr.service';
import { UFRFormService, UFRFormGroup } from './ufr-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ufr-update',
  templateUrl: './ufr-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UFRUpdateComponent implements OnInit {
  isSaving = false;
  uFR: IUFR | null = null;

  universitesSharedCollection: IUniversite[] = [];

  editForm: UFRFormGroup = this.uFRFormService.createUFRFormGroup();

  constructor(
    protected uFRService: UFRService,
    protected uFRFormService: UFRFormService,
    protected universiteService: UniversiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUniversite = (o1: IUniversite | null, o2: IUniversite | null): boolean => this.universiteService.compareUniversite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uFR }) => {
      this.uFR = uFR;
      if (uFR) {
        this.updateForm(uFR);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uFR = this.uFRFormService.getUFR(this.editForm);
    if (uFR.id !== null) {
      this.subscribeToSaveResponse(this.uFRService.update(uFR));
    } else {
      this.subscribeToSaveResponse(this.uFRService.create(uFR));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUFR>>): void {
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

  protected updateForm(uFR: IUFR): void {
    this.uFR = uFR;
    this.uFRFormService.resetForm(this.editForm, uFR);

    this.universitesSharedCollection = this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(
      this.universitesSharedCollection,
      uFR.universite,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.universiteService
      .query()
      .pipe(map((res: HttpResponse<IUniversite[]>) => res.body ?? []))
      .pipe(
        map((universites: IUniversite[]) =>
          this.universiteService.addUniversiteToCollectionIfMissing<IUniversite>(universites, this.uFR?.universite),
        ),
      )
      .subscribe((universites: IUniversite[]) => (this.universitesSharedCollection = universites));
  }
}
