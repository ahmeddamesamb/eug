import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-rapport.test-samples';

import { TypeRapportFormService } from './type-rapport-form.service';

describe('TypeRapport Form Service', () => {
  let service: TypeRapportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeRapportFormService);
  });

  describe('Service methods', () => {
    describe('createTypeRapportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeRapportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeRapport: expect.any(Object),
          }),
        );
      });

      it('passing ITypeRapport should create a new form with FormGroup', () => {
        const formGroup = service.createTypeRapportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeRapport: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeRapport', () => {
      it('should return NewTypeRapport for default TypeRapport initial value', () => {
        const formGroup = service.createTypeRapportFormGroup(sampleWithNewData);

        const typeRapport = service.getTypeRapport(formGroup) as any;

        expect(typeRapport).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeRapport for empty TypeRapport initial value', () => {
        const formGroup = service.createTypeRapportFormGroup();

        const typeRapport = service.getTypeRapport(formGroup) as any;

        expect(typeRapport).toMatchObject({});
      });

      it('should return ITypeRapport', () => {
        const formGroup = service.createTypeRapportFormGroup(sampleWithRequiredData);

        const typeRapport = service.getTypeRapport(formGroup) as any;

        expect(typeRapport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeRapport should not enable id FormControl', () => {
        const formGroup = service.createTypeRapportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeRapport should disable id FormControl', () => {
        const formGroup = service.createTypeRapportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
