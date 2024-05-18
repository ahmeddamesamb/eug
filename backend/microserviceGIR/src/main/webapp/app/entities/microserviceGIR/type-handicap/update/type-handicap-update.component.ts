import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeHandicap } from '../type-handicap.model';
import { TypeHandicapService } from '../service/type-handicap.service';
import { TypeHandicapFormService, TypeHandicapFormGroup } from './type-handicap-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-handicap-update',
  templateUrl: './type-handicap-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeHandicapUpdateComponent implements OnInit {
  isSaving = false;
  typeHandicap: ITypeHandicap | null = null;

  editForm: TypeHandicapFormGroup = this.typeHandicapFormService.createTypeHandicapFormGroup();

  constructor(
    protected typeHandicapService: TypeHandicapService,
    protected typeHandicapFormService: TypeHandicapFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeHandicap }) => {
      this.typeHandicap = typeHandicap;
      if (typeHandicap) {
        this.updateForm(typeHandicap);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeHandicap = this.typeHandicapFormService.getTypeHandicap(this.editForm);
    if (typeHandicap.id !== null) {
      this.subscribeToSaveResponse(this.typeHandicapService.update(typeHandicap));
    } else {
      this.subscribeToSaveResponse(this.typeHandicapService.create(typeHandicap));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeHandicap>>): void {
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

  protected updateForm(typeHandicap: ITypeHandicap): void {
    this.typeHandicap = typeHandicap;
    this.typeHandicapFormService.resetForm(this.editForm, typeHandicap);
  }
}
