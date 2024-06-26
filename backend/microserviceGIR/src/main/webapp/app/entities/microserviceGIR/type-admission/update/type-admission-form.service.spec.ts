import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-admission.test-samples';

import { TypeAdmissionFormService } from './type-admission-form.service';

describe('TypeAdmission Form Service', () => {
  let service: TypeAdmissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeAdmissionFormService);
  });

  describe('Service methods', () => {
    describe('createTypeAdmissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeAdmissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeAdmission: expect.any(Object),
          }),
        );
      });

      it('passing ITypeAdmission should create a new form with FormGroup', () => {
        const formGroup = service.createTypeAdmissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeAdmission: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeAdmission', () => {
      it('should return NewTypeAdmission for default TypeAdmission initial value', () => {
        const formGroup = service.createTypeAdmissionFormGroup(sampleWithNewData);

        const typeAdmission = service.getTypeAdmission(formGroup) as any;

        expect(typeAdmission).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeAdmission for empty TypeAdmission initial value', () => {
        const formGroup = service.createTypeAdmissionFormGroup();

        const typeAdmission = service.getTypeAdmission(formGroup) as any;

        expect(typeAdmission).toMatchObject({});
      });

      it('should return ITypeAdmission', () => {
        const formGroup = service.createTypeAdmissionFormGroup(sampleWithRequiredData);

        const typeAdmission = service.getTypeAdmission(formGroup) as any;

        expect(typeAdmission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeAdmission should not enable id FormControl', () => {
        const formGroup = service.createTypeAdmissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeAdmission should disable id FormControl', () => {
        const formGroup = service.createTypeAdmissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
