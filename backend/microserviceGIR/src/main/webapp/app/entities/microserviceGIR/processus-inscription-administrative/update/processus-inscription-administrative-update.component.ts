import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import { ProcessusInscriptionAdministrativeService } from '../service/processus-inscription-administrative.service';
import {
  ProcessusInscriptionAdministrativeFormService,
  ProcessusInscriptionAdministrativeFormGroup,
} from './processus-inscription-administrative-form.service';

@Component({
  standalone: true,
  selector: 'ugb-processus-inscription-administrative-update',
  templateUrl: './processus-inscription-administrative-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcessusInscriptionAdministrativeUpdateComponent implements OnInit {
  isSaving = false;
  processusInscriptionAdministrative: IProcessusInscriptionAdministrative | null = null;

  inscriptionAdministrativesCollection: IInscriptionAdministrative[] = [];

  editForm: ProcessusInscriptionAdministrativeFormGroup =
    this.processusInscriptionAdministrativeFormService.createProcessusInscriptionAdministrativeFormGroup();

  constructor(
    protected processusInscriptionAdministrativeService: ProcessusInscriptionAdministrativeService,
    protected processusInscriptionAdministrativeFormService: ProcessusInscriptionAdministrativeFormService,
    protected inscriptionAdministrativeService: InscriptionAdministrativeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInscriptionAdministrative = (o1: IInscriptionAdministrative | null, o2: IInscriptionAdministrative | null): boolean =>
    this.inscriptionAdministrativeService.compareInscriptionAdministrative(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processusInscriptionAdministrative }) => {
      this.processusInscriptionAdministrative = processusInscriptionAdministrative;
      if (processusInscriptionAdministrative) {
        this.updateForm(processusInscriptionAdministrative);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const processusInscriptionAdministrative = this.processusInscriptionAdministrativeFormService.getProcessusInscriptionAdministrative(
      this.editForm,
    );
    if (processusInscriptionAdministrative.id !== null) {
      this.subscribeToSaveResponse(this.processusInscriptionAdministrativeService.update(processusInscriptionAdministrative));
    } else {
      this.subscribeToSaveResponse(this.processusInscriptionAdministrativeService.create(processusInscriptionAdministrative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcessusInscriptionAdministrative>>): void {
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

  protected updateForm(processusInscriptionAdministrative: IProcessusInscriptionAdministrative): void {
    this.processusInscriptionAdministrative = processusInscriptionAdministrative;
    this.processusInscriptionAdministrativeFormService.resetForm(this.editForm, processusInscriptionAdministrative);

    this.inscriptionAdministrativesCollection =
      this.inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing<IInscriptionAdministrative>(
        this.inscriptionAdministrativesCollection,
        processusInscriptionAdministrative.inscriptionAdministrative,
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
            this.processusInscriptionAdministrative?.inscriptionAdministrative,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministratives: IInscriptionAdministrative[]) =>
          (this.inscriptionAdministrativesCollection = inscriptionAdministratives),
      );
  }
}
