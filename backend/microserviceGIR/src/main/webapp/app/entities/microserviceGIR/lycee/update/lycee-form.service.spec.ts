import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../lycee.test-samples';

import { LyceeFormService } from './lycee-form.service';

describe('Lycee Form Service', () => {
  let service: LyceeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LyceeFormService);
  });

  describe('Service methods', () => {
    describe('createLyceeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLyceeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLycee: expect.any(Object),
            codeLycee: expect.any(Object),
            villeLycee: expect.any(Object),
            academieLycee: expect.any(Object),
            centreExamen: expect.any(Object),
            region: expect.any(Object),
          }),
        );
      });

      it('passing ILycee should create a new form with FormGroup', () => {
        const formGroup = service.createLyceeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLycee: expect.any(Object),
            codeLycee: expect.any(Object),
            villeLycee: expect.any(Object),
            academieLycee: expect.any(Object),
            centreExamen: expect.any(Object),
            region: expect.any(Object),
          }),
        );
      });
    });

    describe('getLycee', () => {
      it('should return NewLycee for default Lycee initial value', () => {
        const formGroup = service.createLyceeFormGroup(sampleWithNewData);

        const lycee = service.getLycee(formGroup) as any;

        expect(lycee).toMatchObject(sampleWithNewData);
      });

      it('should return NewLycee for empty Lycee initial value', () => {
        const formGroup = service.createLyceeFormGroup();

        const lycee = service.getLycee(formGroup) as any;

        expect(lycee).toMatchObject({});
      });

      it('should return ILycee', () => {
        const formGroup = service.createLyceeFormGroup(sampleWithRequiredData);

        const lycee = service.getLycee(formGroup) as any;

        expect(lycee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILycee should not enable id FormControl', () => {
        const formGroup = service.createLyceeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLycee should disable id FormControl', () => {
        const formGroup = service.createLyceeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
