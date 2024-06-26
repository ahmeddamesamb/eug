import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';
import { OperateurService } from 'app/entities/microserviceGIR/operateur/service/operateur.service';
import { PaiementFormationPriveeService } from '../service/paiement-formation-privee.service';
import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import { PaiementFormationPriveeFormService, PaiementFormationPriveeFormGroup } from './paiement-formation-privee-form.service';

@Component({
  standalone: true,
  selector: 'ugb-paiement-formation-privee-update',
  templateUrl: './paiement-formation-privee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaiementFormationPriveeUpdateComponent implements OnInit {
  isSaving = false;
  paiementFormationPrivee: IPaiementFormationPrivee | null = null;

  inscriptionAdministrativeFormationsSharedCollection: IInscriptionAdministrativeFormation[] = [];
  operateursSharedCollection: IOperateur[] = [];

  editForm: PaiementFormationPriveeFormGroup = this.paiementFormationPriveeFormService.createPaiementFormationPriveeFormGroup();

  constructor(
    protected paiementFormationPriveeService: PaiementFormationPriveeService,
    protected paiementFormationPriveeFormService: PaiementFormationPriveeFormService,
    protected inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService,
    protected operateurService: OperateurService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInscriptionAdministrativeFormation = (
    o1: IInscriptionAdministrativeFormation | null,
    o2: IInscriptionAdministrativeFormation | null,
  ): boolean => this.inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation(o1, o2);

  compareOperateur = (o1: IOperateur | null, o2: IOperateur | null): boolean => this.operateurService.compareOperateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementFormationPrivee }) => {
      this.paiementFormationPrivee = paiementFormationPrivee;
      if (paiementFormationPrivee) {
        this.updateForm(paiementFormationPrivee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementFormationPrivee = this.paiementFormationPriveeFormService.getPaiementFormationPrivee(this.editForm);
    if (paiementFormationPrivee.id !== null) {
      this.subscribeToSaveResponse(this.paiementFormationPriveeService.update(paiementFormationPrivee));
    } else {
      this.subscribeToSaveResponse(this.paiementFormationPriveeService.create(paiementFormationPrivee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementFormationPrivee>>): void {
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

  protected updateForm(paiementFormationPrivee: IPaiementFormationPrivee): void {
    this.paiementFormationPrivee = paiementFormationPrivee;
    this.paiementFormationPriveeFormService.resetForm(this.editForm, paiementFormationPrivee);

    this.inscriptionAdministrativeFormationsSharedCollection =
      this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
        this.inscriptionAdministrativeFormationsSharedCollection,
        paiementFormationPrivee.inscriptionAdministrativeFormation,
      );
    this.operateursSharedCollection = this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(
      this.operateursSharedCollection,
      paiementFormationPrivee.operateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionAdministrativeFormationService
      .query()
      .pipe(map((res: HttpResponse<IInscriptionAdministrativeFormation[]>) => res.body ?? []))
      .pipe(
        map((inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
            inscriptionAdministrativeFormations,
            this.paiementFormationPrivee?.inscriptionAdministrativeFormation,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          (this.inscriptionAdministrativeFormationsSharedCollection = inscriptionAdministrativeFormations),
      );

    this.operateurService
      .query()
      .pipe(map((res: HttpResponse<IOperateur[]>) => res.body ?? []))
      .pipe(
        map((operateurs: IOperateur[]) =>
          this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(operateurs, this.paiementFormationPrivee?.operateur),
        ),
      )
      .subscribe((operateurs: IOperateur[]) => (this.operateursSharedCollection = operateurs));
  }
}
