import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../formation-invalide.test-samples';

import { FormationInvalideFormService } from './formation-invalide-form.service';

describe('FormationInvalide Form Service', () => {
  let service: FormationInvalideFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationInvalideFormService);
  });

  describe('Service methods', () => {
    describe('createFormationInvalideFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormationInvalideFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            actifYN: expect.any(Object),
            formation: expect.any(Object),
            anneeAcademique: expect.any(Object),
          }),
        );
      });

      it('passing IFormationInvalide should create a new form with FormGroup', () => {
        const formGroup = service.createFormationInvalideFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            actifYN: expect.any(Object),
            formation: expect.any(Object),
            anneeAcademique: expect.any(Object),
          }),
        );
      });
    });

    describe('getFormationInvalide', () => {
      it('should return NewFormationInvalide for default FormationInvalide initial value', () => {
        const formGroup = service.createFormationInvalideFormGroup(sampleWithNewData);

        const formationInvalide = service.getFormationInvalide(formGroup) as any;

        expect(formationInvalide).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormationInvalide for empty FormationInvalide initial value', () => {
        const formGroup = service.createFormationInvalideFormGroup();

        const formationInvalide = service.getFormationInvalide(formGroup) as any;

        expect(formationInvalide).toMatchObject({});
      });

      it('should return IFormationInvalide', () => {
        const formGroup = service.createFormationInvalideFormGroup(sampleWithRequiredData);

        const formationInvalide = service.getFormationInvalide(formGroup) as any;

        expect(formationInvalide).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormationInvalide should not enable id FormControl', () => {
        const formGroup = service.createFormationInvalideFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormationInvalide should disable id FormControl', () => {
        const formGroup = service.createFormationInvalideFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
