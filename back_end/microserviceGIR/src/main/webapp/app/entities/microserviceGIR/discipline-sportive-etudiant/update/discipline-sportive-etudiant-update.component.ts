import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDisciplineSportive } from 'app/entities/microserviceGIR/discipline-sportive/discipline-sportive.model';
import { DisciplineSportiveService } from 'app/entities/microserviceGIR/discipline-sportive/service/discipline-sportive.service';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { DisciplineSportiveEtudiantService } from '../service/discipline-sportive-etudiant.service';
import { IDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';
import { DisciplineSportiveEtudiantFormService, DisciplineSportiveEtudiantFormGroup } from './discipline-sportive-etudiant-form.service';

@Component({
  standalone: true,
  selector: 'jhi-discipline-sportive-etudiant-update',
  templateUrl: './discipline-sportive-etudiant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisciplineSportiveEtudiantUpdateComponent implements OnInit {
  isSaving = false;
  disciplineSportiveEtudiant: IDisciplineSportiveEtudiant | null = null;

  disciplineSportivesSharedCollection: IDisciplineSportive[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  editForm: DisciplineSportiveEtudiantFormGroup = this.disciplineSportiveEtudiantFormService.createDisciplineSportiveEtudiantFormGroup();

  constructor(
    protected disciplineSportiveEtudiantService: DisciplineSportiveEtudiantService,
    protected disciplineSportiveEtudiantFormService: DisciplineSportiveEtudiantFormService,
    protected disciplineSportiveService: DisciplineSportiveService,
    protected etudiantService: EtudiantService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDisciplineSportive = (o1: IDisciplineSportive | null, o2: IDisciplineSportive | null): boolean =>
    this.disciplineSportiveService.compareDisciplineSportive(o1, o2);

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disciplineSportiveEtudiant }) => {
      this.disciplineSportiveEtudiant = disciplineSportiveEtudiant;
      if (disciplineSportiveEtudiant) {
        this.updateForm(disciplineSportiveEtudiant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disciplineSportiveEtudiant = this.disciplineSportiveEtudiantFormService.getDisciplineSportiveEtudiant(this.editForm);
    if (disciplineSportiveEtudiant.id !== null) {
      this.subscribeToSaveResponse(this.disciplineSportiveEtudiantService.update(disciplineSportiveEtudiant));
    } else {
      this.subscribeToSaveResponse(this.disciplineSportiveEtudiantService.create(disciplineSportiveEtudiant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisciplineSportiveEtudiant>>): void {
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

  protected updateForm(disciplineSportiveEtudiant: IDisciplineSportiveEtudiant): void {
    this.disciplineSportiveEtudiant = disciplineSportiveEtudiant;
    this.disciplineSportiveEtudiantFormService.resetForm(this.editForm, disciplineSportiveEtudiant);

    this.disciplineSportivesSharedCollection =
      this.disciplineSportiveService.addDisciplineSportiveToCollectionIfMissing<IDisciplineSportive>(
        this.disciplineSportivesSharedCollection,
        disciplineSportiveEtudiant.disciplineSportive,
      );
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsSharedCollection,
      disciplineSportiveEtudiant.etudiant,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.disciplineSportiveService
      .query()
      .pipe(map((res: HttpResponse<IDisciplineSportive[]>) => res.body ?? []))
      .pipe(
        map((disciplineSportives: IDisciplineSportive[]) =>
          this.disciplineSportiveService.addDisciplineSportiveToCollectionIfMissing<IDisciplineSportive>(
            disciplineSportives,
            this.disciplineSportiveEtudiant?.disciplineSportive,
          ),
        ),
      )
      .subscribe((disciplineSportives: IDisciplineSportive[]) => (this.disciplineSportivesSharedCollection = disciplineSportives));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.disciplineSportiveEtudiant?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }
}
