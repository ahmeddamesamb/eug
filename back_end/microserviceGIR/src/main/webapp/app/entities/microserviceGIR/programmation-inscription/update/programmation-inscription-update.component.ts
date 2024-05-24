import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { ICampagne } from 'app/entities/microserviceGIR/campagne/campagne.model';
import { CampagneService } from 'app/entities/microserviceGIR/campagne/service/campagne.service';
import { ProgrammationInscriptionService } from '../service/programmation-inscription.service';
import { IProgrammationInscription } from '../programmation-inscription.model';
import { ProgrammationInscriptionFormService, ProgrammationInscriptionFormGroup } from './programmation-inscription-form.service';

@Component({
  standalone: true,
  selector: 'jhi-programmation-inscription-update',
  templateUrl: './programmation-inscription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProgrammationInscriptionUpdateComponent implements OnInit {
  isSaving = false;
  programmationInscription: IProgrammationInscription | null = null;

  anneeAcademiquesSharedCollection: IAnneeAcademique[] = [];
  formationsSharedCollection: IFormation[] = [];
  campagnesSharedCollection: ICampagne[] = [];

  editForm: ProgrammationInscriptionFormGroup = this.programmationInscriptionFormService.createProgrammationInscriptionFormGroup();

  constructor(
    protected programmationInscriptionService: ProgrammationInscriptionService,
    protected programmationInscriptionFormService: ProgrammationInscriptionFormService,
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected formationService: FormationService,
    protected campagneService: CampagneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAnneeAcademique = (o1: IAnneeAcademique | null, o2: IAnneeAcademique | null): boolean =>
    this.anneeAcademiqueService.compareAnneeAcademique(o1, o2);

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  compareCampagne = (o1: ICampagne | null, o2: ICampagne | null): boolean => this.campagneService.compareCampagne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programmationInscription }) => {
      this.programmationInscription = programmationInscription;
      if (programmationInscription) {
        this.updateForm(programmationInscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const programmationInscription = this.programmationInscriptionFormService.getProgrammationInscription(this.editForm);
    if (programmationInscription.id !== null) {
      this.subscribeToSaveResponse(this.programmationInscriptionService.update(programmationInscription));
    } else {
      this.subscribeToSaveResponse(this.programmationInscriptionService.create(programmationInscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgrammationInscription>>): void {
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

  protected updateForm(programmationInscription: IProgrammationInscription): void {
    this.programmationInscription = programmationInscription;
    this.programmationInscriptionFormService.resetForm(this.editForm, programmationInscription);

    this.anneeAcademiquesSharedCollection = this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
      this.anneeAcademiquesSharedCollection,
      programmationInscription.anneeAcademique,
    );
    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      programmationInscription.formation,
    );
    this.campagnesSharedCollection = this.campagneService.addCampagneToCollectionIfMissing<ICampagne>(
      this.campagnesSharedCollection,
      programmationInscription.campagne,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.anneeAcademiqueService
      .query()
      .pipe(map((res: HttpResponse<IAnneeAcademique[]>) => res.body ?? []))
      .pipe(
        map((anneeAcademiques: IAnneeAcademique[]) =>
          this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
            anneeAcademiques,
            this.programmationInscription?.anneeAcademique,
          ),
        ),
      )
      .subscribe((anneeAcademiques: IAnneeAcademique[]) => (this.anneeAcademiquesSharedCollection = anneeAcademiques));

    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.programmationInscription?.formation),
        ),
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));

    this.campagneService
      .query()
      .pipe(map((res: HttpResponse<ICampagne[]>) => res.body ?? []))
      .pipe(
        map((campagnes: ICampagne[]) =>
          this.campagneService.addCampagneToCollectionIfMissing<ICampagne>(campagnes, this.programmationInscription?.campagne),
        ),
      )
      .subscribe((campagnes: ICampagne[]) => (this.campagnesSharedCollection = campagnes));
  }
}
