import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../formation-privee.test-samples';

import { FormationPriveeFormService } from './formation-privee-form.service';

describe('FormationPrivee Form Service', () => {
  let service: FormationPriveeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationPriveeFormService);
  });

  describe('Service methods', () => {
    describe('createFormationPriveeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormationPriveeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreMensualites: expect.any(Object),
            paiementPremierMoisYN: expect.any(Object),
            paiementDernierMoisYN: expect.any(Object),
            fraisDossierYN: expect.any(Object),
            coutTotal: expect.any(Object),
            mensualite: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });

      it('passing IFormationPrivee should create a new form with FormGroup', () => {
        const formGroup = service.createFormationPriveeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreMensualites: expect.any(Object),
            paiementPremierMoisYN: expect.any(Object),
            paiementDernierMoisYN: expect.any(Object),
            fraisDossierYN: expect.any(Object),
            coutTotal: expect.any(Object),
            mensualite: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });
    });

    describe('getFormationPrivee', () => {
      it('should return NewFormationPrivee for default FormationPrivee initial value', () => {
        const formGroup = service.createFormationPriveeFormGroup(sampleWithNewData);

        const formationPrivee = service.getFormationPrivee(formGroup) as any;

        expect(formationPrivee).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormationPrivee for empty FormationPrivee initial value', () => {
        const formGroup = service.createFormationPriveeFormGroup();

        const formationPrivee = service.getFormationPrivee(formGroup) as any;

        expect(formationPrivee).toMatchObject({});
      });

      it('should return IFormationPrivee', () => {
        const formGroup = service.createFormationPriveeFormGroup(sampleWithRequiredData);

        const formationPrivee = service.getFormationPrivee(formGroup) as any;

        expect(formationPrivee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormationPrivee should not enable id FormControl', () => {
        const formGroup = service.createFormationPriveeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormationPrivee should disable id FormControl', () => {
        const formGroup = service.createFormationPriveeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
