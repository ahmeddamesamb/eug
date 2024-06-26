import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-handicap.test-samples';

import { TypeHandicapFormService } from './type-handicap-form.service';

describe('TypeHandicap Form Service', () => {
  let service: TypeHandicapFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeHandicapFormService);
  });

  describe('Service methods', () => {
    describe('createTypeHandicapFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeHandicapFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeHandicap: expect.any(Object),
          }),
        );
      });

      it('passing ITypeHandicap should create a new form with FormGroup', () => {
        const formGroup = service.createTypeHandicapFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleTypeHandicap: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeHandicap', () => {
      it('should return NewTypeHandicap for default TypeHandicap initial value', () => {
        const formGroup = service.createTypeHandicapFormGroup(sampleWithNewData);

        const typeHandicap = service.getTypeHandicap(formGroup) as any;

        expect(typeHandicap).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeHandicap for empty TypeHandicap initial value', () => {
        const formGroup = service.createTypeHandicapFormGroup();

        const typeHandicap = service.getTypeHandicap(formGroup) as any;

        expect(typeHandicap).toMatchObject({});
      });

      it('should return ITypeHandicap', () => {
        const formGroup = service.createTypeHandicapFormGroup(sampleWithRequiredData);

        const typeHandicap = service.getTypeHandicap(formGroup) as any;

        expect(typeHandicap).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeHandicap should not enable id FormControl', () => {
        const formGroup = service.createTypeHandicapFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeHandicap should disable id FormControl', () => {
        const formGroup = service.createTypeHandicapFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
