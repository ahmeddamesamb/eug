import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnseignantService } from '../service/enseignant.service';
import { IEnseignant } from '../enseignant.model';
import { EnseignantFormService, EnseignantFormGroup } from './enseignant-form.service';

@Component({
  standalone: true,
  selector: 'jhi-enseignant-update',
  templateUrl: './enseignant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnseignantUpdateComponent implements OnInit {
  isSaving = false;
  enseignant: IEnseignant | null = null;

  editForm: EnseignantFormGroup = this.enseignantFormService.createEnseignantFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected enseignantService: EnseignantService,
    protected enseignantFormService: EnseignantFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enseignant }) => {
      this.enseignant = enseignant;
      if (enseignant) {
        this.updateForm(enseignant);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('microserviceGrhApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enseignant = this.enseignantFormService.getEnseignant(this.editForm);
    if (enseignant.id !== null) {
      this.subscribeToSaveResponse(this.enseignantService.update(enseignant));
    } else {
      this.subscribeToSaveResponse(this.enseignantService.create(enseignant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnseignant>>): void {
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

  protected updateForm(enseignant: IEnseignant): void {
    this.enseignant = enseignant;
    this.enseignantFormService.resetForm(this.editForm, enseignant);
  }
}
