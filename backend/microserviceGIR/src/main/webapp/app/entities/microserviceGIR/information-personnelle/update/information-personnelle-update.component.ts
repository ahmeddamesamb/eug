import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { ITypeHandicap } from 'app/entities/microserviceGIR/type-handicap/type-handicap.model';
import { TypeHandicapService } from 'app/entities/microserviceGIR/type-handicap/service/type-handicap.service';
import { ITypeBourse } from 'app/entities/microserviceGIR/type-bourse/type-bourse.model';
import { TypeBourseService } from 'app/entities/microserviceGIR/type-bourse/service/type-bourse.service';
import { InformationPersonnelleService } from '../service/information-personnelle.service';
import { IInformationPersonnelle } from '../information-personnelle.model';
import { InformationPersonnelleFormService, InformationPersonnelleFormGroup } from './information-personnelle-form.service';

@Component({
  standalone: true,
  selector: 'jhi-information-personnelle-update',
  templateUrl: './information-personnelle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InformationPersonnelleUpdateComponent implements OnInit {
  isSaving = false;
  informationPersonnelle: IInformationPersonnelle | null = null;

  etudiantsCollection: IEtudiant[] = [];
  typeHandicapsSharedCollection: ITypeHandicap[] = [];
  typeBoursesSharedCollection: ITypeBourse[] = [];

  editForm: InformationPersonnelleFormGroup = this.informationPersonnelleFormService.createInformationPersonnelleFormGroup();

  constructor(
    protected informationPersonnelleService: InformationPersonnelleService,
    protected informationPersonnelleFormService: InformationPersonnelleFormService,
    protected etudiantService: EtudiantService,
    protected typeHandicapService: TypeHandicapService,
    protected typeBourseService: TypeBourseService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  compareTypeHandicap = (o1: ITypeHandicap | null, o2: ITypeHandicap | null): boolean =>
    this.typeHandicapService.compareTypeHandicap(o1, o2);

  compareTypeBourse = (o1: ITypeBourse | null, o2: ITypeBourse | null): boolean => this.typeBourseService.compareTypeBourse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ informationPersonnelle }) => {
      this.informationPersonnelle = informationPersonnelle;
      if (informationPersonnelle) {
        this.updateForm(informationPersonnelle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const informationPersonnelle = this.informationPersonnelleFormService.getInformationPersonnelle(this.editForm);
    if (informationPersonnelle.id !== null) {
      this.subscribeToSaveResponse(this.informationPersonnelleService.update(informationPersonnelle));
    } else {
      this.subscribeToSaveResponse(this.informationPersonnelleService.create(informationPersonnelle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInformationPersonnelle>>): void {
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

  protected updateForm(informationPersonnelle: IInformationPersonnelle): void {
    this.informationPersonnelle = informationPersonnelle;
    this.informationPersonnelleFormService.resetForm(this.editForm, informationPersonnelle);

    this.etudiantsCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsCollection,
      informationPersonnelle.etudiant,
    );
    this.typeHandicapsSharedCollection = this.typeHandicapService.addTypeHandicapToCollectionIfMissing<ITypeHandicap>(
      this.typeHandicapsSharedCollection,
      informationPersonnelle.typeHadique,
    );
    this.typeBoursesSharedCollection = this.typeBourseService.addTypeBourseToCollectionIfMissing<ITypeBourse>(
      this.typeBoursesSharedCollection,
      informationPersonnelle.typeBourse,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query({ filter: 'informationpersonnelle-is-null' })
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.informationPersonnelle?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsCollection = etudiants));

    this.typeHandicapService
      .query()
      .pipe(map((res: HttpResponse<ITypeHandicap[]>) => res.body ?? []))
      .pipe(
        map((typeHandicaps: ITypeHandicap[]) =>
          this.typeHandicapService.addTypeHandicapToCollectionIfMissing<ITypeHandicap>(
            typeHandicaps,
            this.informationPersonnelle?.typeHadique,
          ),
        ),
      )
      .subscribe((typeHandicaps: ITypeHandicap[]) => (this.typeHandicapsSharedCollection = typeHandicaps));

    this.typeBourseService
      .query()
      .pipe(map((res: HttpResponse<ITypeBourse[]>) => res.body ?? []))
      .pipe(
        map((typeBourses: ITypeBourse[]) =>
          this.typeBourseService.addTypeBourseToCollectionIfMissing<ITypeBourse>(typeBourses, this.informationPersonnelle?.typeBourse),
        ),
      )
      .subscribe((typeBourses: ITypeBourse[]) => (this.typeBoursesSharedCollection = typeBourses));
  }
}
