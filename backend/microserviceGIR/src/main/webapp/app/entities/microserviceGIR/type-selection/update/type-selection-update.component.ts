import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeSelection } from '../type-selection.model';
import { TypeSelectionService } from '../service/type-selection.service';
import { TypeSelectionFormService, TypeSelectionFormGroup } from './type-selection-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-selection-update',
  templateUrl: './type-selection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeSelectionUpdateComponent implements OnInit {
  isSaving = false;
  typeSelection: ITypeSelection | null = null;

  editForm: TypeSelectionFormGroup = this.typeSelectionFormService.createTypeSelectionFormGroup();

  constructor(
    protected typeSelectionService: TypeSelectionService,
    protected typeSelectionFormService: TypeSelectionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSelection }) => {
      this.typeSelection = typeSelection;
      if (typeSelection) {
        this.updateForm(typeSelection);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeSelection = this.typeSelectionFormService.getTypeSelection(this.editForm);
    if (typeSelection.id !== null) {
      this.subscribeToSaveResponse(this.typeSelectionService.update(typeSelection));
    } else {
      this.subscribeToSaveResponse(this.typeSelectionService.create(typeSelection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeSelection>>): void {
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

  protected updateForm(typeSelection: ITypeSelection): void {
    this.typeSelection = typeSelection;
    this.typeSelectionFormService.resetForm(this.editForm, typeSelection);
  }
}
