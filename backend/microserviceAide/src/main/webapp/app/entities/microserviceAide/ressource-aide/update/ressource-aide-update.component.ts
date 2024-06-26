import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRessourceAide } from '../ressource-aide.model';
import { RessourceAideService } from '../service/ressource-aide.service';
import { RessourceAideFormService, RessourceAideFormGroup } from './ressource-aide-form.service';

@Component({
  standalone: true,
  selector: 'ugb-ressource-aide-update',
  templateUrl: './ressource-aide-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RessourceAideUpdateComponent implements OnInit {
  isSaving = false;
  ressourceAide: IRessourceAide | null = null;

  editForm: RessourceAideFormGroup = this.ressourceAideFormService.createRessourceAideFormGroup();

  constructor(
    protected ressourceAideService: RessourceAideService,
    protected ressourceAideFormService: RessourceAideFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ressourceAide }) => {
      this.ressourceAide = ressourceAide;
      if (ressourceAide) {
        this.updateForm(ressourceAide);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ressourceAide = this.ressourceAideFormService.getRessourceAide(this.editForm);
    if (ressourceAide.id !== null) {
      this.subscribeToSaveResponse(this.ressourceAideService.update(ressourceAide));
    } else {
      this.subscribeToSaveResponse(this.ressourceAideService.create(ressourceAide));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRessourceAide>>): void {
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

  protected updateForm(ressourceAide: IRessourceAide): void {
    this.ressourceAide = ressourceAide;
    this.ressourceAideFormService.resetForm(this.editForm, ressourceAide);
  }
}
