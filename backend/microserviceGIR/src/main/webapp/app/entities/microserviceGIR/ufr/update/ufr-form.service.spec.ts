import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ufr.test-samples';

import { UFRFormService } from './ufr-form.service';

describe('UFR Form Service', () => {
  let service: UFRFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UFRFormService);
  });

  describe('Service methods', () => {
    describe('createUFRFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUFRFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libeleUFR: expect.any(Object),
            sigleUFR: expect.any(Object),
            systemeLMDYN: expect.any(Object),
            ordreStat: expect.any(Object),
            universite: expect.any(Object),
          }),
        );
      });

      it('passing IUFR should create a new form with FormGroup', () => {
        const formGroup = service.createUFRFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libeleUFR: expect.any(Object),
            sigleUFR: expect.any(Object),
            systemeLMDYN: expect.any(Object),
            ordreStat: expect.any(Object),
            universite: expect.any(Object),
          }),
        );
      });
    });

    describe('getUFR', () => {
      it('should return NewUFR for default UFR initial value', () => {
        const formGroup = service.createUFRFormGroup(sampleWithNewData);

        const uFR = service.getUFR(formGroup) as any;

        expect(uFR).toMatchObject(sampleWithNewData);
      });

      it('should return NewUFR for empty UFR initial value', () => {
        const formGroup = service.createUFRFormGroup();

        const uFR = service.getUFR(formGroup) as any;

        expect(uFR).toMatchObject({});
      });

      it('should return IUFR', () => {
        const formGroup = service.createUFRFormGroup(sampleWithRequiredData);

        const uFR = service.getUFR(formGroup) as any;

        expect(uFR).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUFR should not enable id FormControl', () => {
        const formGroup = service.createUFRFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUFR should disable id FormControl', () => {
        const formGroup = service.createUFRFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
