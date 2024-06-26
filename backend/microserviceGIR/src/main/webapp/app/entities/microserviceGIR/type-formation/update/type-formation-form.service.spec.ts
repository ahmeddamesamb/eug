import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-formation.test-samples';

import { TypeFormationFormService } from './type-formation-form.service';

describe('TypeFormation Form Service', () => {
  let service: TypeFormationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeFormationFormService);
  });

  describe('Service methods', () => {
    describe('createTypeFormationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeFormationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeFormation: expect.any(Object),
          }),
        );
      });

      it('passing ITypeFormation should create a new form with FormGroup', () => {
        const formGroup = service.createTypeFormationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeFormation: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeFormation', () => {
      it('should return NewTypeFormation for default TypeFormation initial value', () => {
        const formGroup = service.createTypeFormationFormGroup(sampleWithNewData);

        const typeFormation = service.getTypeFormation(formGroup) as any;

        expect(typeFormation).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeFormation for empty TypeFormation initial value', () => {
        const formGroup = service.createTypeFormationFormGroup();

        const typeFormation = service.getTypeFormation(formGroup) as any;

        expect(typeFormation).toMatchObject({});
      });

      it('should return ITypeFormation', () => {
        const formGroup = service.createTypeFormationFormGroup(sampleWithRequiredData);

        const typeFormation = service.getTypeFormation(formGroup) as any;

        expect(typeFormation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeFormation should not enable id FormControl', () => {
        const formGroup = service.createTypeFormationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeFormation should disable id FormControl', () => {
        const formGroup = service.createTypeFormationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
