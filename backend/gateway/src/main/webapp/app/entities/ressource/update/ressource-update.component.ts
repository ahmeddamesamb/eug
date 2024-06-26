import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRessource } from '../ressource.model';
import { RessourceService } from '../service/ressource.service';
import { RessourceFormService, RessourceFormGroup } from './ressource-form.service';

@Component({
  standalone: true,
  selector: 'ugb-ressource-update',
  templateUrl: './ressource-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RessourceUpdateComponent implements OnInit {
  isSaving = false;
  ressource: IRessource | null = null;

  editForm: RessourceFormGroup = this.ressourceFormService.createRessourceFormGroup();

  constructor(
    protected ressourceService: RessourceService,
    protected ressourceFormService: RessourceFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ressource }) => {
      this.ressource = ressource;
      if (ressource) {
        this.updateForm(ressource);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ressource = this.ressourceFormService.getRessource(this.editForm);
    if (ressource.id !== null) {
      this.subscribeToSaveResponse(this.ressourceService.update(ressource));
    } else {
      this.subscribeToSaveResponse(this.ressourceService.create(ressource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRessource>>): void {
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

  protected updateForm(ressource: IRessource): void {
    this.ressource = ressource;
    this.ressourceFormService.resetForm(this.editForm, ressource);
  }
}
