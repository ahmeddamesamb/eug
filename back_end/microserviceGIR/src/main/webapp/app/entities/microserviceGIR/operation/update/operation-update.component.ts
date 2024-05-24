import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITypeOperation } from 'app/entities/microserviceGIR/type-operation/type-operation.model';
import { TypeOperationService } from 'app/entities/microserviceGIR/type-operation/service/type-operation.service';
import { OperationService } from '../service/operation.service';
import { IOperation } from '../operation.model';
import { OperationFormService, OperationFormGroup } from './operation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;
  operation: IOperation | null = null;

  typeOperationsSharedCollection: ITypeOperation[] = [];

  editForm: OperationFormGroup = this.operationFormService.createOperationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected operationService: OperationService,
    protected operationFormService: OperationFormService,
    protected typeOperationService: TypeOperationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeOperation = (o1: ITypeOperation | null, o2: ITypeOperation | null): boolean =>
    this.typeOperationService.compareTypeOperation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      this.operation = operation;
      if (operation) {
        this.updateForm(operation);
      }

      this.loadRelationshipsOptions();
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('microserviceGirApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.operationFormService.getOperation(this.editForm);
    if (operation.id !== null) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.operation = operation;
    this.operationFormService.resetForm(this.editForm, operation);

    this.typeOperationsSharedCollection = this.typeOperationService.addTypeOperationToCollectionIfMissing<ITypeOperation>(
      this.typeOperationsSharedCollection,
      operation.typeOperation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeOperationService
      .query()
      .pipe(map((res: HttpResponse<ITypeOperation[]>) => res.body ?? []))
      .pipe(
        map((typeOperations: ITypeOperation[]) =>
          this.typeOperationService.addTypeOperationToCollectionIfMissing<ITypeOperation>(typeOperations, this.operation?.typeOperation),
        ),
      )
      .subscribe((typeOperations: ITypeOperation[]) => (this.typeOperationsSharedCollection = typeOperations));
  }
}
