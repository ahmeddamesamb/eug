import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../deliberation.test-samples';

import { DeliberationFormService } from './deliberation-form.service';

describe('Deliberation Form Service', () => {
  let service: DeliberationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliberationFormService);
  });

  describe('Service methods', () => {
    describe('createDeliberationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeliberationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estValidee: expect.any(Object),
            pvDeliberation: expect.any(Object),
          }),
        );
      });

      it('passing IDeliberation should create a new form with FormGroup', () => {
        const formGroup = service.createDeliberationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estValidee: expect.any(Object),
            pvDeliberation: expect.any(Object),
          }),
        );
      });
    });

    describe('getDeliberation', () => {
      it('should return NewDeliberation for default Deliberation initial value', () => {
        const formGroup = service.createDeliberationFormGroup(sampleWithNewData);

        const deliberation = service.getDeliberation(formGroup) as any;

        expect(deliberation).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeliberation for empty Deliberation initial value', () => {
        const formGroup = service.createDeliberationFormGroup();

        const deliberation = service.getDeliberation(formGroup) as any;

        expect(deliberation).toMatchObject({});
      });

      it('should return IDeliberation', () => {
        const formGroup = service.createDeliberationFormGroup(sampleWithRequiredData);

        const deliberation = service.getDeliberation(formGroup) as any;

        expect(deliberation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeliberation should not enable id FormControl', () => {
        const formGroup = service.createDeliberationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeliberation should disable id FormControl', () => {
        const formGroup = service.createDeliberationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
