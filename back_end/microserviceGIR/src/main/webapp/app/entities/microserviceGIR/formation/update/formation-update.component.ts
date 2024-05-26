import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { NiveauService } from 'app/entities/microserviceGIR/niveau/service/niveau.service';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { SpecialiteService } from 'app/entities/microserviceGIR/specialite/service/specialite.service';
import { TypeFormation } from 'app/entities/enumerations/type-formation.model';
import { FormationService } from '../service/formation.service';
import { IFormation } from '../formation.model';
import { FormationFormService, FormationFormGroup } from './formation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-formation-update',
  templateUrl: './formation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormationUpdateComponent implements OnInit {
  isSaving = false;
  formation: IFormation | null = null;
  typeFormationValues = Object.keys(TypeFormation);

  niveausSharedCollection: INiveau[] = [];
  specialitesSharedCollection: ISpecialite[] = [];

  editForm: FormationFormGroup = this.formationFormService.createFormationFormGroup();

  constructor(
    protected formationService: FormationService,
    protected formationFormService: FormationFormService,
    protected niveauService: NiveauService,
    protected specialiteService: SpecialiteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareNiveau = (o1: INiveau | null, o2: INiveau | null): boolean => this.niveauService.compareNiveau(o1, o2);

  compareSpecialite = (o1: ISpecialite | null, o2: ISpecialite | null): boolean => this.specialiteService.compareSpecialite(o1, o2);

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

    this.niveausSharedCollection = this.niveauService.addNiveauToCollectionIfMissing<INiveau>(
      this.niveausSharedCollection,
      formation.niveau,
    );
    this.specialitesSharedCollection = this.specialiteService.addSpecialiteToCollectionIfMissing<ISpecialite>(
      this.specialitesSharedCollection,
      formation.specialite,
    );
  }

  protected loadRelationshipsOptions(): void {
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
  }
}
