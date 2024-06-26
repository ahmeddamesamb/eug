import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';
import { UfrService } from 'app/entities/microserviceGIR/ufr/service/ufr.service';
import { IDepartement } from '../departement.model';
import { DepartementService } from '../service/departement.service';
import { DepartementFormService, DepartementFormGroup } from './departement-form.service';

@Component({
  standalone: true,
  selector: 'ugb-departement-update',
  templateUrl: './departement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartementUpdateComponent implements OnInit {
  isSaving = false;
  departement: IDepartement | null = null;

  ufrsSharedCollection: IUfr[] = [];

  editForm: DepartementFormGroup = this.departementFormService.createDepartementFormGroup();

  constructor(
    protected departementService: DepartementService,
    protected departementFormService: DepartementFormService,
    protected ufrService: UfrService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUfr = (o1: IUfr | null, o2: IUfr | null): boolean => this.ufrService.compareUfr(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departement }) => {
      this.departement = departement;
      if (departement) {
        this.updateForm(departement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departement = this.departementFormService.getDepartement(this.editForm);
    if (departement.id !== null) {
      this.subscribeToSaveResponse(this.departementService.update(departement));
    } else {
      this.subscribeToSaveResponse(this.departementService.create(departement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartement>>): void {
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

  protected updateForm(departement: IDepartement): void {
    this.departement = departement;
    this.departementFormService.resetForm(this.editForm, departement);

    this.ufrsSharedCollection = this.ufrService.addUfrToCollectionIfMissing<IUfr>(this.ufrsSharedCollection, departement.ufr);
  }

  protected loadRelationshipsOptions(): void {
    this.ufrService
      .query()
      .pipe(map((res: HttpResponse<IUfr[]>) => res.body ?? []))
      .pipe(map((ufrs: IUfr[]) => this.ufrService.addUfrToCollectionIfMissing<IUfr>(ufrs, this.departement?.ufr)))
      .subscribe((ufrs: IUfr[]) => (this.ufrsSharedCollection = ufrs));
  }
}
