import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeRapport } from '../type-rapport.model';
import { TypeRapportService } from '../service/type-rapport.service';
import { TypeRapportFormService, TypeRapportFormGroup } from './type-rapport-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-rapport-update',
  templateUrl: './type-rapport-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeRapportUpdateComponent implements OnInit {
  isSaving = false;
  typeRapport: ITypeRapport | null = null;

  editForm: TypeRapportFormGroup = this.typeRapportFormService.createTypeRapportFormGroup();

  constructor(
    protected typeRapportService: TypeRapportService,
    protected typeRapportFormService: TypeRapportFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeRapport }) => {
      this.typeRapport = typeRapport;
      if (typeRapport) {
        this.updateForm(typeRapport);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeRapport = this.typeRapportFormService.getTypeRapport(this.editForm);
    if (typeRapport.id !== null) {
      this.subscribeToSaveResponse(this.typeRapportService.update(typeRapport));
    } else {
      this.subscribeToSaveResponse(this.typeRapportService.create(typeRapport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeRapport>>): void {
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

  protected updateForm(typeRapport: ITypeRapport): void {
    this.typeRapport = typeRapport;
    this.typeRapportFormService.resetForm(this.editForm, typeRapport);
  }
}
