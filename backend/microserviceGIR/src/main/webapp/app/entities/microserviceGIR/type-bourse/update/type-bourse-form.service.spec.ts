import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-bourse.test-samples';

import { TypeBourseFormService } from './type-bourse-form.service';

describe('TypeBourse Form Service', () => {
  let service: TypeBourseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeBourseFormService);
  });

  describe('Service methods', () => {
    describe('createTypeBourseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeBourseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing ITypeBourse should create a new form with FormGroup', () => {
        const formGroup = service.createTypeBourseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeBourse', () => {
      it('should return NewTypeBourse for default TypeBourse initial value', () => {
        const formGroup = service.createTypeBourseFormGroup(sampleWithNewData);

        const typeBourse = service.getTypeBourse(formGroup) as any;

        expect(typeBourse).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeBourse for empty TypeBourse initial value', () => {
        const formGroup = service.createTypeBourseFormGroup();

        const typeBourse = service.getTypeBourse(formGroup) as any;

        expect(typeBourse).toMatchObject({});
      });

      it('should return ITypeBourse', () => {
        const formGroup = service.createTypeBourseFormGroup(sampleWithRequiredData);

        const typeBourse = service.getTypeBourse(formGroup) as any;

        expect(typeBourse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeBourse should not enable id FormControl', () => {
        const formGroup = service.createTypeBourseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeBourse should disable id FormControl', () => {
        const formGroup = service.createTypeBourseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
