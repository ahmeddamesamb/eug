import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeFormation } from '../type-formation.model';
import { TypeFormationService } from '../service/type-formation.service';
import { TypeFormationFormService, TypeFormationFormGroup } from './type-formation-form.service';

@Component({
  standalone: true,
  selector: 'ugb-type-formation-update',
  templateUrl: './type-formation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeFormationUpdateComponent implements OnInit {
  isSaving = false;
  typeFormation: ITypeFormation | null = null;

  editForm: TypeFormationFormGroup = this.typeFormationFormService.createTypeFormationFormGroup();

  constructor(
    protected typeFormationService: TypeFormationService,
    protected typeFormationFormService: TypeFormationFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeFormation }) => {
      this.typeFormation = typeFormation;
      if (typeFormation) {
        this.updateForm(typeFormation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeFormation = this.typeFormationFormService.getTypeFormation(this.editForm);
    if (typeFormation.id !== null) {
      this.subscribeToSaveResponse(this.typeFormationService.update(typeFormation));
    } else {
      this.subscribeToSaveResponse(this.typeFormationService.create(typeFormation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeFormation>>): void {
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

  protected updateForm(typeFormation: ITypeFormation): void {
    this.typeFormation = typeFormation;
    this.typeFormationFormService.resetForm(this.editForm, typeFormation);
  }
}
