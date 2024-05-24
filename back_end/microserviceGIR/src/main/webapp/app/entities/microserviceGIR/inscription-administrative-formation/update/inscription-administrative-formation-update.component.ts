import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { InscriptionAdministrativeFormationService } from '../service/inscription-administrative-formation.service';
import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import {
  InscriptionAdministrativeFormationFormService,
  InscriptionAdministrativeFormationFormGroup,
} from './inscription-administrative-formation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inscription-administrative-formation-update',
  templateUrl: './inscription-administrative-formation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InscriptionAdministrativeFormationUpdateComponent implements OnInit {
  isSaving = false;
  inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation | null = null;

  inscriptionAdministrativesSharedCollection: IInscriptionAdministrative[] = [];
  formationsSharedCollection: IFormation[] = [];

  editForm: InscriptionAdministrativeFormationFormGroup =
    this.inscriptionAdministrativeFormationFormService.createInscriptionAdministrativeFormationFormGroup();

  constructor(
    protected inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService,
    protected inscriptionAdministrativeFormationFormService: InscriptionAdministrativeFormationFormService,
    protected inscriptionAdministrativeService: InscriptionAdministrativeService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInscriptionAdministrative = (o1: IInscriptionAdministrative | null, o2: IInscriptionAdministrative | null): boolean =>
    this.inscriptionAdministrativeService.compareInscriptionAdministrative(o1, o2);

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionAdministrativeFormation }) => {
      this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
      if (inscriptionAdministrativeFormation) {
        this.updateForm(inscriptionAdministrativeFormation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionAdministrativeFormation = this.inscriptionAdministrativeFormationFormService.getInscriptionAdministrativeFormation(
      this.editForm,
    );
    if (inscriptionAdministrativeFormation.id !== null) {
      this.subscribeToSaveResponse(this.inscriptionAdministrativeFormationService.update(inscriptionAdministrativeFormation));
    } else {
      this.subscribeToSaveResponse(this.inscriptionAdministrativeFormationService.create(inscriptionAdministrativeFormation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionAdministrativeFormation>>): void {
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

  protected updateForm(inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation): void {
    this.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
    this.inscriptionAdministrativeFormationFormService.resetForm(this.editForm, inscriptionAdministrativeFormation);

    this.inscriptionAdministrativesSharedCollection =
      this.inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing<IInscriptionAdministrative>(
        this.inscriptionAdministrativesSharedCollection,
        inscriptionAdministrativeFormation.inscriptionAdministrative,
      );
    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      inscriptionAdministrativeFormation.formation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionAdministrativeService
      .query()
      .pipe(map((res: HttpResponse<IInscriptionAdministrative[]>) => res.body ?? []))
      .pipe(
        map((inscriptionAdministratives: IInscriptionAdministrative[]) =>
          this.inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing<IInscriptionAdministrative>(
            inscriptionAdministratives,
            this.inscriptionAdministrativeFormation?.inscriptionAdministrative,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministratives: IInscriptionAdministrative[]) =>
          (this.inscriptionAdministrativesSharedCollection = inscriptionAdministratives),
      );

    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(
            formations,
            this.inscriptionAdministrativeFormation?.formation,
          ),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));
  }
}
