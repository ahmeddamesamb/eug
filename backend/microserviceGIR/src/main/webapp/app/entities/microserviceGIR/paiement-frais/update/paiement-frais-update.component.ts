import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';
import { FraisService } from 'app/entities/microserviceGIR/frais/service/frais.service';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';
import { OperateurService } from 'app/entities/microserviceGIR/operateur/service/operateur.service';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { PaiementFraisService } from '../service/paiement-frais.service';
import { IPaiementFrais } from '../paiement-frais.model';
import { PaiementFraisFormService, PaiementFraisFormGroup } from './paiement-frais-form.service';

@Component({
  standalone: true,
  selector: 'ugb-paiement-frais-update',
  templateUrl: './paiement-frais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaiementFraisUpdateComponent implements OnInit {
  isSaving = false;
  paiementFrais: IPaiementFrais | null = null;

  fraisSharedCollection: IFrais[] = [];
  operateursSharedCollection: IOperateur[] = [];
  inscriptionAdministrativeFormationsSharedCollection: IInscriptionAdministrativeFormation[] = [];

  editForm: PaiementFraisFormGroup = this.paiementFraisFormService.createPaiementFraisFormGroup();

  constructor(
    protected paiementFraisService: PaiementFraisService,
    protected paiementFraisFormService: PaiementFraisFormService,
    protected fraisService: FraisService,
    protected operateurService: OperateurService,
    protected inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFrais = (o1: IFrais | null, o2: IFrais | null): boolean => this.fraisService.compareFrais(o1, o2);

  compareOperateur = (o1: IOperateur | null, o2: IOperateur | null): boolean => this.operateurService.compareOperateur(o1, o2);

  compareInscriptionAdministrativeFormation = (
    o1: IInscriptionAdministrativeFormation | null,
    o2: IInscriptionAdministrativeFormation | null,
  ): boolean => this.inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paiementFrais }) => {
      this.paiementFrais = paiementFrais;
      if (paiementFrais) {
        this.updateForm(paiementFrais);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paiementFrais = this.paiementFraisFormService.getPaiementFrais(this.editForm);
    if (paiementFrais.id !== null) {
      this.subscribeToSaveResponse(this.paiementFraisService.update(paiementFrais));
    } else {
      this.subscribeToSaveResponse(this.paiementFraisService.create(paiementFrais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaiementFrais>>): void {
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

  protected updateForm(paiementFrais: IPaiementFrais): void {
    this.paiementFrais = paiementFrais;
    this.paiementFraisFormService.resetForm(this.editForm, paiementFrais);

    this.fraisSharedCollection = this.fraisService.addFraisToCollectionIfMissing<IFrais>(this.fraisSharedCollection, paiementFrais.frais);
    this.operateursSharedCollection = this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(
      this.operateursSharedCollection,
      paiementFrais.operateur,
    );
    this.inscriptionAdministrativeFormationsSharedCollection =
      this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
        this.inscriptionAdministrativeFormationsSharedCollection,
        paiementFrais.inscriptionAdministrativeFormation,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.fraisService
      .query()
      .pipe(map((res: HttpResponse<IFrais[]>) => res.body ?? []))
      .pipe(map((frais: IFrais[]) => this.fraisService.addFraisToCollectionIfMissing<IFrais>(frais, this.paiementFrais?.frais)))
      .subscribe((frais: IFrais[]) => (this.fraisSharedCollection = frais));

    this.operateurService
      .query()
      .pipe(map((res: HttpResponse<IOperateur[]>) => res.body ?? []))
      .pipe(
        map((operateurs: IOperateur[]) =>
          this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(operateurs, this.paiementFrais?.operateur),
        ),
      )
      .subscribe((operateurs: IOperateur[]) => (this.operateursSharedCollection = operateurs));

    this.inscriptionAdministrativeFormationService
      .query()
      .pipe(map((res: HttpResponse<IInscriptionAdministrativeFormation[]>) => res.body ?? []))
      .pipe(
        map((inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
            inscriptionAdministrativeFormations,
            this.paiementFrais?.inscriptionAdministrativeFormation,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          (this.inscriptionAdministrativeFormationsSharedCollection = inscriptionAdministrativeFormations),
      );
  }
}
