import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDoctorat } from 'app/entities/microserviceGIR/doctorat/doctorat.model';
import { DoctoratService } from 'app/entities/microserviceGIR/doctorat/service/doctorat.service';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { InscriptionDoctoratService } from '../service/inscription-doctorat.service';
import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { InscriptionDoctoratFormService, InscriptionDoctoratFormGroup } from './inscription-doctorat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inscription-doctorat-update',
  templateUrl: './inscription-doctorat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InscriptionDoctoratUpdateComponent implements OnInit {
  isSaving = false;
  inscriptionDoctorat: IInscriptionDoctorat | null = null;

  doctoratsSharedCollection: IDoctorat[] = [];
  inscriptionAdministrativeFormationsSharedCollection: IInscriptionAdministrativeFormation[] = [];

  editForm: InscriptionDoctoratFormGroup = this.inscriptionDoctoratFormService.createInscriptionDoctoratFormGroup();

  constructor(
    protected inscriptionDoctoratService: InscriptionDoctoratService,
    protected inscriptionDoctoratFormService: InscriptionDoctoratFormService,
    protected doctoratService: DoctoratService,
    protected inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDoctorat = (o1: IDoctorat | null, o2: IDoctorat | null): boolean => this.doctoratService.compareDoctorat(o1, o2);

  compareInscriptionAdministrativeFormation = (
    o1: IInscriptionAdministrativeFormation | null,
    o2: IInscriptionAdministrativeFormation | null,
  ): boolean => this.inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionDoctorat }) => {
      this.inscriptionDoctorat = inscriptionDoctorat;
      if (inscriptionDoctorat) {
        this.updateForm(inscriptionDoctorat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionDoctorat = this.inscriptionDoctoratFormService.getInscriptionDoctorat(this.editForm);
    if (inscriptionDoctorat.id !== null) {
      this.subscribeToSaveResponse(this.inscriptionDoctoratService.update(inscriptionDoctorat));
    } else {
      this.subscribeToSaveResponse(this.inscriptionDoctoratService.create(inscriptionDoctorat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionDoctorat>>): void {
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

  protected updateForm(inscriptionDoctorat: IInscriptionDoctorat): void {
    this.inscriptionDoctorat = inscriptionDoctorat;
    this.inscriptionDoctoratFormService.resetForm(this.editForm, inscriptionDoctorat);

    this.doctoratsSharedCollection = this.doctoratService.addDoctoratToCollectionIfMissing<IDoctorat>(
      this.doctoratsSharedCollection,
      inscriptionDoctorat.doctorat,
    );
    this.inscriptionAdministrativeFormationsSharedCollection =
      this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
        this.inscriptionAdministrativeFormationsSharedCollection,
        inscriptionDoctorat.inscriptionAdministrativeFormation,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.doctoratService
      .query()
      .pipe(map((res: HttpResponse<IDoctorat[]>) => res.body ?? []))
      .pipe(
        map((doctorats: IDoctorat[]) =>
          this.doctoratService.addDoctoratToCollectionIfMissing<IDoctorat>(doctorats, this.inscriptionDoctorat?.doctorat),
        ),
      )
      .subscribe((doctorats: IDoctorat[]) => (this.doctoratsSharedCollection = doctorats));

    this.inscriptionAdministrativeFormationService
      .query()
      .pipe(map((res: HttpResponse<IInscriptionAdministrativeFormation[]>) => res.body ?? []))
      .pipe(
        map((inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          this.inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing<IInscriptionAdministrativeFormation>(
            inscriptionAdministrativeFormations,
            this.inscriptionDoctorat?.inscriptionAdministrativeFormation,
          ),
        ),
      )
      .subscribe(
        (inscriptionAdministrativeFormations: IInscriptionAdministrativeFormation[]) =>
          (this.inscriptionAdministrativeFormationsSharedCollection = inscriptionAdministrativeFormations),
      );
  }
}
