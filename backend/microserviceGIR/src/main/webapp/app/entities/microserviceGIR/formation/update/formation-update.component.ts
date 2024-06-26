import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeFormation } from 'app/entities/microserviceGIR/type-formation/type-formation.model';
import { TypeFormationService } from 'app/entities/microserviceGIR/type-formation/service/type-formation.service';
import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { NiveauService } from 'app/entities/microserviceGIR/niveau/service/niveau.service';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { SpecialiteService } from 'app/entities/microserviceGIR/specialite/service/specialite.service';
import { IDepartement } from 'app/entities/microserviceGIR/departement/departement.model';
import { DepartementService } from 'app/entities/microserviceGIR/departement/service/departement.service';
import { FormationService } from '../service/formation.service';
import { IFormation } from '../formation.model';
import { FormationFormService, FormationFormGroup } from './formation-form.service';

@Component({
  standalone: true,
  selector: 'ugb-formation-update',
  templateUrl: './formation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationUpdateComponent implements OnInit {
  isSaving = false;
  formation: IFormation | null = null;

  typeFormationsSharedCollection: ITypeFormation[] = [];
  niveausSharedCollection: INiveau[] = [];
  specialitesSharedCollection: ISpecialite[] = [];
  departementsSharedCollection: IDepartement[] = [];

  editForm: FormationFormGroup = this.formationFormService.createFormationFormGroup();

  constructor(
    protected formationService: FormationService,
    protected formationFormService: FormationFormService,
    protected typeFormationService: TypeFormationService,
    protected niveauService: NiveauService,
    protected specialiteService: SpecialiteService,
    protected departementService: DepartementService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeFormation = (o1: ITypeFormation | null, o2: ITypeFormation | null): boolean =>
    this.typeFormationService.compareTypeFormation(o1, o2);

  compareNiveau = (o1: INiveau | null, o2: INiveau | null): boolean => this.niveauService.compareNiveau(o1, o2);

  compareSpecialite = (o1: ISpecialite | null, o2: ISpecialite | null): boolean => this.specialiteService.compareSpecialite(o1, o2);

  compareDepartement = (o1: IDepartement | null, o2: IDepartement | null): boolean => this.departementService.compareDepartement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formation }) => {
      this.formation = formation;
      if (formation) {
        this.updateForm(formation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formation = this.formationFormService.getFormation(this.editForm);
    if (formation.id !== null) {
      this.subscribeToSaveResponse(this.formationService.update(formation));
    } else {
      this.subscribeToSaveResponse(this.formationService.create(formation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormation>>): void {
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

  protected updateForm(formation: IFormation): void {
    this.formation = formation;
    this.formationFormService.resetForm(this.editForm, formation);

    this.typeFormationsSharedCollection = this.typeFormationService.addTypeFormationToCollectionIfMissing<ITypeFormation>(
      this.typeFormationsSharedCollection,
      formation.typeFormation,
    );
    this.niveausSharedCollection = this.niveauService.addNiveauToCollectionIfMissing<INiveau>(
      this.niveausSharedCollection,
      formation.niveau,
    );
    this.specialitesSharedCollection = this.specialiteService.addSpecialiteToCollectionIfMissing<ISpecialite>(
      this.specialitesSharedCollection,
      formation.specialite,
    );
    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing<IDepartement>(
      this.departementsSharedCollection,
      formation.departement,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeFormationService
      .query()
      .pipe(map((res: HttpResponse<ITypeFormation[]>) => res.body ?? []))
      .pipe(
        map((typeFormations: ITypeFormation[]) =>
          this.typeFormationService.addTypeFormationToCollectionIfMissing<ITypeFormation>(typeFormations, this.formation?.typeFormation),
        ),
      )
      .subscribe((typeFormations: ITypeFormation[]) => (this.typeFormationsSharedCollection = typeFormations));

    this.niveauService
      .query()
      .pipe(map((res: HttpResponse<INiveau[]>) => res.body ?? []))
      .pipe(map((niveaus: INiveau[]) => this.niveauService.addNiveauToCollectionIfMissing<INiveau>(niveaus, this.formation?.niveau)))
      .subscribe((niveaus: INiveau[]) => (this.niveausSharedCollection = niveaus));

    this.specialiteService
      .query()
      .pipe(map((res: HttpResponse<ISpecialite[]>) => res.body ?? []))
      .pipe(
        map((specialites: ISpecialite[]) =>
          this.specialiteService.addSpecialiteToCollectionIfMissing<ISpecialite>(specialites, this.formation?.specialite),
        ),
      )
      .subscribe((specialites: ISpecialite[]) => (this.specialitesSharedCollection = specialites));

    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing<IDepartement>(departements, this.formation?.departement),
        ),
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }
}
