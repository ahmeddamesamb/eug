import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-selection.test-samples';

import { TypeSelectionFormService } from './type-selection-form.service';

describe('TypeSelection Form Service', () => {
  let service: TypeSelectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeSelectionFormService);
  });

  describe('Service methods', () => {
    describe('createTypeSelectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeSelectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeSelection: expect.any(Object),
          }),
        );
      });

      it('passing ITypeSelection should create a new form with FormGroup', () => {
        const formGroup = service.createTypeSelectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeSelection: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeSelection', () => {
      it('should return NewTypeSelection for default TypeSelection initial value', () => {
        const formGroup = service.createTypeSelectionFormGroup(sampleWithNewData);

        const typeSelection = service.getTypeSelection(formGroup) as any;

        expect(typeSelection).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeSelection for empty TypeSelection initial value', () => {
        const formGroup = service.createTypeSelectionFormGroup();

        const typeSelection = service.getTypeSelection(formGroup) as any;

        expect(typeSelection).toMatchObject({});
      });

      it('should return ITypeSelection', () => {
        const formGroup = service.createTypeSelectionFormGroup(sampleWithRequiredData);

        const typeSelection = service.getTypeSelection(formGroup) as any;

        expect(typeSelection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeSelection should not enable id FormControl', () => {
        const formGroup = service.createTypeSelectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeSelection should disable id FormControl', () => {
        const formGroup = service.createTypeSelectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
