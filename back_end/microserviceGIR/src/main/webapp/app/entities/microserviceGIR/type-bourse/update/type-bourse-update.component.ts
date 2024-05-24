import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeBourse } from '../type-bourse.model';
import { TypeBourseService } from '../service/type-bourse.service';
import { TypeBourseFormService, TypeBourseFormGroup } from './type-bourse-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-bourse-update',
  templateUrl: './type-bourse-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeBourseUpdateComponent implements OnInit {
  isSaving = false;
  typeBourse: ITypeBourse | null = null;

  editForm: TypeBourseFormGroup = this.typeBourseFormService.createTypeBourseFormGroup();

  constructor(
    protected typeBourseService: TypeBourseService,
    protected typeBourseFormService: TypeBourseFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeBourse }) => {
      this.typeBourse = typeBourse;
      if (typeBourse) {
        this.updateForm(typeBourse);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeBourse = this.typeBourseFormService.getTypeBourse(this.editForm);
    if (typeBourse.id !== null) {
      this.subscribeToSaveResponse(this.typeBourseService.update(typeBourse));
    } else {
      this.subscribeToSaveResponse(this.typeBourseService.create(typeBourse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeBourse>>): void {
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

  protected updateForm(typeBourse: ITypeBourse): void {
    this.typeBourse = typeBourse;
    this.typeBourseFormService.resetForm(this.editForm, typeBourse);
  }
}
