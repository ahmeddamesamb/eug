import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../formation-valide.test-samples';

import { FormationValideFormService } from './formation-valide-form.service';

describe('FormationValide Form Service', () => {
  let service: FormationValideFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationValideFormService);
  });

  describe('Service methods', () => {
    describe('createFormationValideFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormationValideFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valideYN: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });

      it('passing IFormationValide should create a new form with FormGroup', () => {
        const formGroup = service.createFormationValideFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valideYN: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });
    });

    describe('getFormationValide', () => {
      it('should return NewFormationValide for default FormationValide initial value', () => {
        const formGroup = service.createFormationValideFormGroup(sampleWithNewData);

        const formationValide = service.getFormationValide(formGroup) as any;

        expect(formationValide).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormationValide for empty FormationValide initial value', () => {
        const formGroup = service.createFormationValideFormGroup();

        const formationValide = service.getFormationValide(formGroup) as any;

        expect(formationValide).toMatchObject({});
      });

      it('should return IFormationValide', () => {
        const formGroup = service.createFormationValideFormGroup(sampleWithRequiredData);

        const formationValide = service.getFormationValide(formGroup) as any;

        expect(formationValide).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormationValide should not enable id FormControl', () => {
        const formGroup = service.createFormationValideFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormationValide should disable id FormControl', () => {
        const formGroup = service.createFormationValideFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
