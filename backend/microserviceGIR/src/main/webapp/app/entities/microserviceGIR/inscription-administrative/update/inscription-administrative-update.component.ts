import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeAdmission } from 'app/entities/microserviceGIR/type-admission/type-admission.model';
import { TypeAdmissionService } from 'app/entities/microserviceGIR/type-admission/service/type-admission.service';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { InscriptionAdministrativeService } from '../service/inscription-administrative.service';
import { IInscriptionAdministrative } from '../inscription-administrative.model';
import { InscriptionAdministrativeFormService, InscriptionAdministrativeFormGroup } from './inscription-administrative-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inscription-administrative-update',
  templateUrl: './inscription-administrative-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InscriptionAdministrativeUpdateComponent implements OnInit {
  isSaving = false;
  inscriptionAdministrative: IInscriptionAdministrative | null = null;

  typeAdmissionsSharedCollection: ITypeAdmission[] = [];
  anneeAcademiquesSharedCollection: IAnneeAcademique[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  editForm: InscriptionAdministrativeFormGroup = this.inscriptionAdministrativeFormService.createInscriptionAdministrativeFormGroup();

  constructor(
    protected inscriptionAdministrativeService: InscriptionAdministrativeService,
    protected inscriptionAdministrativeFormService: InscriptionAdministrativeFormService,
    protected typeAdmissionService: TypeAdmissionService,
    protected anneeAcademiqueService: AnneeAcademiqueService,
    protected etudiantService: EtudiantService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeAdmission = (o1: ITypeAdmission | null, o2: ITypeAdmission | null): boolean =>
    this.typeAdmissionService.compareTypeAdmission(o1, o2);

  compareAnneeAcademique = (o1: IAnneeAcademique | null, o2: IAnneeAcademique | null): boolean =>
    this.anneeAcademiqueService.compareAnneeAcademique(o1, o2);

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionAdministrative }) => {
      this.inscriptionAdministrative = inscriptionAdministrative;
      if (inscriptionAdministrative) {
        this.updateForm(inscriptionAdministrative);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionAdministrative = this.inscriptionAdministrativeFormService.getInscriptionAdministrative(this.editForm);
    if (inscriptionAdministrative.id !== null) {
      this.subscribeToSaveResponse(this.inscriptionAdministrativeService.update(inscriptionAdministrative));
    } else {
      this.subscribeToSaveResponse(this.inscriptionAdministrativeService.create(inscriptionAdministrative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionAdministrative>>): void {
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

  protected updateForm(inscriptionAdministrative: IInscriptionAdministrative): void {
    this.inscriptionAdministrative = inscriptionAdministrative;
    this.inscriptionAdministrativeFormService.resetForm(this.editForm, inscriptionAdministrative);

    this.typeAdmissionsSharedCollection = this.typeAdmissionService.addTypeAdmissionToCollectionIfMissing<ITypeAdmission>(
      this.typeAdmissionsSharedCollection,
      inscriptionAdministrative.typeAdmission,
    );
    this.anneeAcademiquesSharedCollection = this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
      this.anneeAcademiquesSharedCollection,
      inscriptionAdministrative.anneeAcademique,
    );
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsSharedCollection,
      inscriptionAdministrative.etudiant,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeAdmissionService
      .query()
      .pipe(map((res: HttpResponse<ITypeAdmission[]>) => res.body ?? []))
      .pipe(
        map((typeAdmissions: ITypeAdmission[]) =>
          this.typeAdmissionService.addTypeAdmissionToCollectionIfMissing<ITypeAdmission>(
            typeAdmissions,
            this.inscriptionAdministrative?.typeAdmission,
          ),
        ),
      )
      .subscribe((typeAdmissions: ITypeAdmission[]) => (this.typeAdmissionsSharedCollection = typeAdmissions));

    this.anneeAcademiqueService
      .query()
      .pipe(map((res: HttpResponse<IAnneeAcademique[]>) => res.body ?? []))
      .pipe(
        map((anneeAcademiques: IAnneeAcademique[]) =>
          this.anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing<IAnneeAcademique>(
            anneeAcademiques,
            this.inscriptionAdministrative?.anneeAcademique,
          ),
        ),
      )
      .subscribe((anneeAcademiques: IAnneeAcademique[]) => (this.anneeAcademiquesSharedCollection = anneeAcademiques));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.inscriptionAdministrative?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }
}
