import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-frais.test-samples';

import { TypeFraisFormService } from './type-frais-form.service';

describe('TypeFrais Form Service', () => {
  let service: TypeFraisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeFraisFormService);
  });

  describe('Service methods', () => {
    describe('createTypeFraisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeFraisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeFrais: expect.any(Object),
          }),
        );
      });

      it('passing ITypeFrais should create a new form with FormGroup', () => {
        const formGroup = service.createTypeFraisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeFrais: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeFrais', () => {
      it('should return NewTypeFrais for default TypeFrais initial value', () => {
        const formGroup = service.createTypeFraisFormGroup(sampleWithNewData);

        const typeFrais = service.getTypeFrais(formGroup) as any;

        expect(typeFrais).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeFrais for empty TypeFrais initial value', () => {
        const formGroup = service.createTypeFraisFormGroup();

        const typeFrais = service.getTypeFrais(formGroup) as any;

        expect(typeFrais).toMatchObject({});
      });

      it('should return ITypeFrais', () => {
        const formGroup = service.createTypeFraisFormGroup(sampleWithRequiredData);

        const typeFrais = service.getTypeFrais(formGroup) as any;

        expect(typeFrais).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeFrais should not enable id FormControl', () => {
        const formGroup = service.createTypeFraisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeFrais should disable id FormControl', () => {
        const formGroup = service.createTypeFraisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
