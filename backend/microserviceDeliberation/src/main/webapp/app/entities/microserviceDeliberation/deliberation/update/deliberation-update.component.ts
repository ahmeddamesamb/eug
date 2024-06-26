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
import { DeliberationService } from '../service/deliberation.service';
import { IDeliberation } from '../deliberation.model';
import { DeliberationFormService, DeliberationFormGroup } from './deliberation-form.service';

@Component({
  standalone: true,
  selector: 'ugb-deliberation-update',
  templateUrl: './deliberation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DeliberationUpdateComponent implements OnInit {
  isSaving = false;
  deliberation: IDeliberation | null = null;

  editForm: DeliberationFormGroup = this.deliberationFormService.createDeliberationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected deliberationService: DeliberationService,
    protected deliberationFormService: DeliberationFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliberation }) => {
      this.deliberation = deliberation;
      if (deliberation) {
        this.updateForm(deliberation);
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
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('microserviceDeliberationApp.error', { ...err, key: 'error.file.' + err.key }),
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliberation = this.deliberationFormService.getDeliberation(this.editForm);
    if (deliberation.id !== null) {
      this.subscribeToSaveResponse(this.deliberationService.update(deliberation));
    } else {
      this.subscribeToSaveResponse(this.deliberationService.create(deliberation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliberation>>): void {
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

  protected updateForm(deliberation: IDeliberation): void {
    this.deliberation = deliberation;
    this.deliberationFormService.resetForm(this.editForm, deliberation);
  }
}
