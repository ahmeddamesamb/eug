import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../document-delivre.test-samples';

import { DocumentDelivreFormService } from './document-delivre-form.service';

describe('DocumentDelivre Form Service', () => {
  let service: DocumentDelivreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentDelivreFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentDelivreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentDelivreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleDoc: expect.any(Object),
            anneeDoc: expect.any(Object),
            dateEnregistrement: expect.any(Object),
            typeDocument: expect.any(Object),
          }),
        );
      });

      it('passing IDocumentDelivre should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentDelivreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleDoc: expect.any(Object),
            anneeDoc: expect.any(Object),
            dateEnregistrement: expect.any(Object),
            typeDocument: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocumentDelivre', () => {
      it('should return NewDocumentDelivre for default DocumentDelivre initial value', () => {
        const formGroup = service.createDocumentDelivreFormGroup(sampleWithNewData);

        const documentDelivre = service.getDocumentDelivre(formGroup) as any;

        expect(documentDelivre).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocumentDelivre for empty DocumentDelivre initial value', () => {
        const formGroup = service.createDocumentDelivreFormGroup();

        const documentDelivre = service.getDocumentDelivre(formGroup) as any;

        expect(documentDelivre).toMatchObject({});
      });

      it('should return IDocumentDelivre', () => {
        const formGroup = service.createDocumentDelivreFormGroup(sampleWithRequiredData);

        const documentDelivre = service.getDocumentDelivre(formGroup) as any;

        expect(documentDelivre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocumentDelivre should not enable id FormControl', () => {
        const formGroup = service.createDocumentDelivreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocumentDelivre should disable id FormControl', () => {
        const formGroup = service.createDocumentDelivreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
