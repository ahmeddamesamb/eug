import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { IProcessusDinscriptionAdministrative } from '../processus-dinscription-administrative.model';
import { ProcessusDinscriptionAdministrativeService } from '../service/processus-dinscription-administrative.service';
import {
  ProcessusDinscriptionAdministrativeFormService,
  ProcessusDinscriptionAdministrativeFormGroup,
} from './processus-dinscription-administrative-form.service';

@Component({
  standalone: true,
  selector: 'jhi-processus-dinscription-administrative-update',
  templateUrl: './processus-dinscription-administrative-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcessusDinscriptionAdministrativeUpdateComponent implements OnInit {
  isSaving = false;
  processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative | null = null;

  inscriptionAdministrativesCollection: IInscriptionAdministrative[] = [];

  editForm: ProcessusDinscriptionAdministrativeFormGroup =
    this.processusDinscriptionAdministrativeFormService.createProcessusDinscriptionAdministrativeFormGroup();

  constructor(
    protected processusDinscriptionAdministrativeService: ProcessusDinscriptionAdministrativeService,
    protected processusDinscriptionAdministrativeFormService: ProcessusDinscriptionAdministrativeFormService,
    protected inscriptionAdministrativeService: InscriptionAdministrativeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInscriptionAdministrative = (o1: IInscriptionAdministrative | null, o2: IInscriptionAdministrative | null): boolean =>
    this.inscriptionAdministrativeService.compareInscriptionAdministrative(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processusDinscriptionAdministrative }) => {
      this.processusDinscriptionAdministrative = processusDinscriptionAdministrative;
      if (processusDinscriptionAdministrative) {
        this.updateForm(processusDinscriptionAdministrative);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const processusDinscriptionAdministrative = this.processusDinscriptionAdministrativeFormService.getProcessusDinscriptionAdministrative(
      this.editForm,
    );
    if (processusDinscriptionAdministrative.id !== null) {
      this.subscribeToSaveResponse(this.processusDinscriptionAdministrativeService.update(processusDinscriptionAdministrative));
    } else {
      this.subscribeToSaveResponse(this.processusDinscriptionAdministrativeService.create(processusDinscriptionAdministrative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcessusDinscriptionAdministrative>>): void {
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

  protected updateForm(processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative): void {
    this.processusDinscriptionAdministrative = processusDinscriptionAdministrative;
    this.processusDinscriptionAdministrativeFormService.resetForm(this.editForm, processusDinscriptionAdministrative);

    this.inscriptionAdministrativesCollection =
      this.inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing<IInscriptionAdministrative>(
        this.inscriptionAdministrativesCollection,
        processusDinscriptionAdministrative.inscriptionAdministrative,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionAdministrativeService
      .query({ filter: 'processusdinscriptionadministrative-is-null' })
      .pipe(map((res: HttpResponse<IInscriptionAdministrative[]>) => res.body ?? []))
      .pipe(
        map((inscriptionAdministratives: IInscriptionAdministrative[]) =>
          this.inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing<IInscriptionAdministrative>(
            inscriptionAdministratives,
            this.processusDinscriptionAdministrative?.inscriptionAdministrative,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministratives: IInscriptionAdministrative[]) =>
          (this.inscriptionAdministrativesCollection = inscriptionAdministratives),
      );
  }
}
