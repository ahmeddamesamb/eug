import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeAdmission } from '../type-admission.model';
import { TypeAdmissionService } from '../service/type-admission.service';
import { TypeAdmissionFormService, TypeAdmissionFormGroup } from './type-admission-form.service';

@Component({
  standalone: true,
  selector: 'ugb-type-admission-update',
  templateUrl: './type-admission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeAdmissionUpdateComponent implements OnInit {
  isSaving = false;
  typeAdmission: ITypeAdmission | null = null;

  editForm: TypeAdmissionFormGroup = this.typeAdmissionFormService.createTypeAdmissionFormGroup();

  constructor(
    protected typeAdmissionService: TypeAdmissionService,
    protected typeAdmissionFormService: TypeAdmissionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeAdmission }) => {
      this.typeAdmission = typeAdmission;
      if (typeAdmission) {
        this.updateForm(typeAdmission);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeAdmission = this.typeAdmissionFormService.getTypeAdmission(this.editForm);
    if (typeAdmission.id !== null) {
      this.subscribeToSaveResponse(this.typeAdmissionService.update(typeAdmission));
    } else {
      this.subscribeToSaveResponse(this.typeAdmissionService.create(typeAdmission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeAdmission>>): void {
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

  protected updateForm(typeAdmission: ITypeAdmission): void {
    this.typeAdmission = typeAdmission;
    this.typeAdmissionFormService.resetForm(this.editForm, typeAdmission);
  }
}
