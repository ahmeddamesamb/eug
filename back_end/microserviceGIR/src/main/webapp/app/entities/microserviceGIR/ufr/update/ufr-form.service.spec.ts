import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ufr.test-samples';

import { UfrFormService } from './ufr-form.service';

describe('Ufr Form Service', () => {
  let service: UfrFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UfrFormService);
  });

  describe('Service methods', () => {
    describe('createUfrFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUfrFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libeleUfr: expect.any(Object),
            sigleUfr: expect.any(Object),
            systemeLMDYN: expect.any(Object),
            ordreStat: expect.any(Object),
            universite: expect.any(Object),
          }),
        );
      });

      it('passing IUfr should create a new form with FormGroup', () => {
        const formGroup = service.createUfrFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libeleUfr: expect.any(Object),
            sigleUfr: expect.any(Object),
            systemeLMDYN: expect.any(Object),
            ordreStat: expect.any(Object),
            universite: expect.any(Object),
          }),
        );
      });
    });

    describe('getUfr', () => {
      it('should return NewUfr for default Ufr initial value', () => {
        const formGroup = service.createUfrFormGroup(sampleWithNewData);

        const ufr = service.getUfr(formGroup) as any;

        expect(ufr).toMatchObject(sampleWithNewData);
      });

      it('should return NewUfr for empty Ufr initial value', () => {
        const formGroup = service.createUfrFormGroup();

        const ufr = service.getUfr(formGroup) as any;

        expect(ufr).toMatchObject({});
      });

      it('should return IUfr', () => {
        const formGroup = service.createUfrFormGroup(sampleWithRequiredData);

        const ufr = service.getUfr(formGroup) as any;

        expect(ufr).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUfr should not enable id FormControl', () => {
        const formGroup = service.createUfrFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUfr should disable id FormControl', () => {
        const formGroup = service.createUfrFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
