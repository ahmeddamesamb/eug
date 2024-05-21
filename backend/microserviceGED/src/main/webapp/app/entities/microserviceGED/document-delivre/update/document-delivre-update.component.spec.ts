import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeDocument } from 'app/entities/microserviceGED/type-document/type-document.model';
import { TypeDocumentService } from 'app/entities/microserviceGED/type-document/service/type-document.service';
import { DocumentDelivreService } from '../service/document-delivre.service';
import { IDocumentDelivre } from '../document-delivre.model';
import { DocumentDelivreFormService } from './document-delivre-form.service';

import { DocumentDelivreUpdateComponent } from './document-delivre-update.component';

describe('DocumentDelivre Management Update Component', () => {
  let comp: DocumentDelivreUpdateComponent;
  let fixture: ComponentFixture<DocumentDelivreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentDelivreFormService: DocumentDelivreFormService;
  let documentDelivreService: DocumentDelivreService;
  let typeDocumentService: TypeDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DocumentDelivreUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentDelivreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentDelivreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentDelivreFormService = TestBed.inject(DocumentDelivreFormService);
    documentDelivreService = TestBed.inject(DocumentDelivreService);
    typeDocumentService = TestBed.inject(TypeDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeDocument query and add missing value', () => {
      const documentDelivre: IDocumentDelivre = { id: 456 };
      const typeDocument: ITypeDocument = { id: 25577 };
      documentDelivre.typeDocument = typeDocument;

      const typeDocumentCollection: ITypeDocument[] = [{ id: 32114 }];
      jest.spyOn(typeDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: typeDocumentCollection })));
      const additionalTypeDocuments = [typeDocument];
      const expectedCollection: ITypeDocument[] = [...additionalTypeDocuments, ...typeDocumentCollection];
      jest.spyOn(typeDocumentService, 'addTypeDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentDelivre });
      comp.ngOnInit();

      expect(typeDocumentService.query).toHaveBeenCalled();
      expect(typeDocumentService.addTypeDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        typeDocumentCollection,
        ...additionalTypeDocuments.map(expect.objectContaining),
      );
      expect(comp.typeDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const documentDelivre: IDocumentDelivre = { id: 456 };
      const typeDocument: ITypeDocument = { id: 32764 };
      documentDelivre.typeDocument = typeDocument;

      activatedRoute.data = of({ documentDelivre });
      comp.ngOnInit();

      expect(comp.typeDocumentsSharedCollection).toContain(typeDocument);
      expect(comp.documentDelivre).toEqual(documentDelivre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDelivre>>();
      const documentDelivre = { id: 123 };
      jest.spyOn(documentDelivreFormService, 'getDocumentDelivre').mockReturnValue(documentDelivre);
      jest.spyOn(documentDelivreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDelivre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentDelivre }));
      saveSubject.complete();

      // THEN
      expect(documentDelivreFormService.getDocumentDelivre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentDelivreService.update).toHaveBeenCalledWith(expect.objectContaining(documentDelivre));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDelivre>>();
      const documentDelivre = { id: 123 };
      jest.spyOn(documentDelivreFormService, 'getDocumentDelivre').mockReturnValue({ id: null });
      jest.spyOn(documentDelivreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDelivre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentDelivre }));
      saveSubject.complete();

      // THEN
      expect(documentDelivreFormService.getDocumentDelivre).toHaveBeenCalled();
      expect(documentDelivreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentDelivre>>();
      const documentDelivre = { id: 123 };
      jest.spyOn(documentDelivreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentDelivre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentDelivreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeDocument', () => {
      it('Should forward to typeDocumentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeDocumentService, 'compareTypeDocument');
        comp.compareTypeDocument(entity, entity2);
        expect(typeDocumentService.compareTypeDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
