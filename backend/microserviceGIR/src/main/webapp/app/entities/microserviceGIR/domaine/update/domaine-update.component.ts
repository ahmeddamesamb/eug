import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDomaine } from '../domaine.model';
import { DomaineService } from '../service/domaine.service';
import { DomaineFormService, DomaineFormGroup } from './domaine-form.service';

@Component({
  standalone: true,
  selector: 'jhi-domaine-update',
  templateUrl: './domaine-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DomaineUpdateComponent implements OnInit {
  isSaving = false;
  domaine: IDomaine | null = null;

  editForm: DomaineFormGroup = this.domaineFormService.createDomaineFormGroup();

  constructor(
    protected domaineService: DomaineService,
    protected domaineFormService: DomaineFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaine }) => {
      this.domaine = domaine;
      if (domaine) {
        this.updateForm(domaine);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domaine = this.domaineFormService.getDomaine(this.editForm);
    if (domaine.id !== null) {
      this.subscribeToSaveResponse(this.domaineService.update(domaine));
    } else {
      this.subscribeToSaveResponse(this.domaineService.create(domaine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaine>>): void {
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

  protected updateForm(domaine: IDomaine): void {
    this.domaine = domaine;
    this.domaineFormService.resetForm(this.editForm, domaine);
  }
}
