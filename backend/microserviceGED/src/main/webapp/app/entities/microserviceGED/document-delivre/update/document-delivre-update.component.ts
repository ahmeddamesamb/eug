import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeDocument } from 'app/entities/microserviceGED/type-document/type-document.model';
import { TypeDocumentService } from 'app/entities/microserviceGED/type-document/service/type-document.service';
import { IDocumentDelivre } from '../document-delivre.model';
import { DocumentDelivreService } from '../service/document-delivre.service';
import { DocumentDelivreFormService, DocumentDelivreFormGroup } from './document-delivre-form.service';

@Component({
  standalone: true,
  selector: 'ugb-document-delivre-update',
  templateUrl: './document-delivre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentDelivreUpdateComponent implements OnInit {
  isSaving = false;
  documentDelivre: IDocumentDelivre | null = null;

  typeDocumentsSharedCollection: ITypeDocument[] = [];

  editForm: DocumentDelivreFormGroup = this.documentDelivreFormService.createDocumentDelivreFormGroup();

  constructor(
    protected documentDelivreService: DocumentDelivreService,
    protected documentDelivreFormService: DocumentDelivreFormService,
    protected typeDocumentService: TypeDocumentService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTypeDocument = (o1: ITypeDocument | null, o2: ITypeDocument | null): boolean =>
    this.typeDocumentService.compareTypeDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentDelivre }) => {
      this.documentDelivre = documentDelivre;
      if (documentDelivre) {
        this.updateForm(documentDelivre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentDelivre = this.documentDelivreFormService.getDocumentDelivre(this.editForm);
    if (documentDelivre.id !== null) {
      this.subscribeToSaveResponse(this.documentDelivreService.update(documentDelivre));
    } else {
      this.subscribeToSaveResponse(this.documentDelivreService.create(documentDelivre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentDelivre>>): void {
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

  protected updateForm(documentDelivre: IDocumentDelivre): void {
    this.documentDelivre = documentDelivre;
    this.documentDelivreFormService.resetForm(this.editForm, documentDelivre);

    this.typeDocumentsSharedCollection = this.typeDocumentService.addTypeDocumentToCollectionIfMissing<ITypeDocument>(
      this.typeDocumentsSharedCollection,
      documentDelivre.typeDocument,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeDocumentService
      .query()
      .pipe(map((res: HttpResponse<ITypeDocument[]>) => res.body ?? []))
      .pipe(
        map((typeDocuments: ITypeDocument[]) =>
          this.typeDocumentService.addTypeDocumentToCollectionIfMissing<ITypeDocument>(typeDocuments, this.documentDelivre?.typeDocument),
        ),
      )
      .subscribe((typeDocuments: ITypeDocument[]) => (this.typeDocumentsSharedCollection = typeDocuments));
  }
}
