import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../laboratoire.test-samples';

import { LaboratoireFormService } from './laboratoire-form.service';

describe('Laboratoire Form Service', () => {
  let service: LaboratoireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LaboratoireFormService);
  });

  describe('Service methods', () => {
    describe('createLaboratoireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLaboratoireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLaboratoire: expect.any(Object),
            laboratoireCotutelleYN: expect.any(Object),
          }),
        );
      });

      it('passing ILaboratoire should create a new form with FormGroup', () => {
        const formGroup = service.createLaboratoireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLaboratoire: expect.any(Object),
            laboratoireCotutelleYN: expect.any(Object),
          }),
        );
      });
    });

    describe('getLaboratoire', () => {
      it('should return NewLaboratoire for default Laboratoire initial value', () => {
        const formGroup = service.createLaboratoireFormGroup(sampleWithNewData);

        const laboratoire = service.getLaboratoire(formGroup) as any;

        expect(laboratoire).toMatchObject(sampleWithNewData);
      });

      it('should return NewLaboratoire for empty Laboratoire initial value', () => {
        const formGroup = service.createLaboratoireFormGroup();

        const laboratoire = service.getLaboratoire(formGroup) as any;

        expect(laboratoire).toMatchObject({});
      });

      it('should return ILaboratoire', () => {
        const formGroup = service.createLaboratoireFormGroup(sampleWithRequiredData);

        const laboratoire = service.getLaboratoire(formGroup) as any;

        expect(laboratoire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILaboratoire should not enable id FormControl', () => {
        const formGroup = service.createLaboratoireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLaboratoire should disable id FormControl', () => {
        const formGroup = service.createLaboratoireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
