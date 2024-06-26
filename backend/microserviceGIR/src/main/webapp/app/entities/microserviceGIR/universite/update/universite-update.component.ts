import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMinistere } from 'app/entities/microserviceGIR/ministere/ministere.model';
import { MinistereService } from 'app/entities/microserviceGIR/ministere/service/ministere.service';
import { IUniversite } from '../universite.model';
import { UniversiteService } from '../service/universite.service';
import { UniversiteFormService, UniversiteFormGroup } from './universite-form.service';

@Component({
  standalone: true,
  selector: 'ugb-universite-update',
  templateUrl: './universite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UniversiteUpdateComponent implements OnInit {
  isSaving = false;
  universite: IUniversite | null = null;

  ministeresSharedCollection: IMinistere[] = [];

  editForm: UniversiteFormGroup = this.universiteFormService.createUniversiteFormGroup();

  constructor(
    protected universiteService: UniversiteService,
    protected universiteFormService: UniversiteFormService,
    protected ministereService: MinistereService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMinistere = (o1: IMinistere | null, o2: IMinistere | null): boolean => this.ministereService.compareMinistere(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ universite }) => {
      this.universite = universite;
      if (universite) {
        this.updateForm(universite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const universite = this.universiteFormService.getUniversite(this.editForm);
    if (universite.id !== null) {
      this.subscribeToSaveResponse(this.universiteService.update(universite));
    } else {
      this.subscribeToSaveResponse(this.universiteService.create(universite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUniversite>>): void {
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

  protected updateForm(universite: IUniversite): void {
    this.universite = universite;
    this.universiteFormService.resetForm(this.editForm, universite);

    this.ministeresSharedCollection = this.ministereService.addMinistereToCollectionIfMissing<IMinistere>(
      this.ministeresSharedCollection,
      universite.ministere,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ministereService
      .query()
      .pipe(map((res: HttpResponse<IMinistere[]>) => res.body ?? []))
      .pipe(
        map((ministeres: IMinistere[]) =>
          this.ministereService.addMinistereToCollectionIfMissing<IMinistere>(ministeres, this.universite?.ministere),
        ),
      )
      .subscribe((ministeres: IMinistere[]) => (this.ministeresSharedCollection = ministeres));
  }
}
