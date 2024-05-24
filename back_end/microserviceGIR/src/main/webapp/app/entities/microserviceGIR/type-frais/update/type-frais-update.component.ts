import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeFrais } from '../type-frais.model';
import { TypeFraisService } from '../service/type-frais.service';
import { TypeFraisFormService, TypeFraisFormGroup } from './type-frais-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-frais-update',
  templateUrl: './type-frais-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeFraisUpdateComponent implements OnInit {
  isSaving = false;
  typeFrais: ITypeFrais | null = null;

  editForm: TypeFraisFormGroup = this.typeFraisFormService.createTypeFraisFormGroup();

  constructor(
    protected typeFraisService: TypeFraisService,
    protected typeFraisFormService: TypeFraisFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeFrais }) => {
      this.typeFrais = typeFrais;
      if (typeFrais) {
        this.updateForm(typeFrais);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeFrais = this.typeFraisFormService.getTypeFrais(this.editForm);
    if (typeFrais.id !== null) {
      this.subscribeToSaveResponse(this.typeFraisService.update(typeFrais));
    } else {
      this.subscribeToSaveResponse(this.typeFraisService.create(typeFrais));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeFrais>>): void {
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

  protected updateForm(typeFrais: ITypeFrais): void {
    this.typeFrais = typeFrais;
    this.typeFraisFormService.resetForm(this.editForm, typeFrais);
  }
}
