import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-formation-privee.test-samples';

import { PaiementFormationPriveeFormService } from './paiement-formation-privee-form.service';

describe('PaiementFormationPrivee Form Service', () => {
  let service: PaiementFormationPriveeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementFormationPriveeFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementFormationPriveeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePaiement: expect.any(Object),
            moisPaiement: expect.any(Object),
            anneePaiement: expect.any(Object),
            payerMensualiteYN: expect.any(Object),
            emailUser: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
            operateur: expect.any(Object),
          }),
        );
      });

      it('passing IPaiementFormationPrivee should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePaiement: expect.any(Object),
            moisPaiement: expect.any(Object),
            anneePaiement: expect.any(Object),
            payerMensualiteYN: expect.any(Object),
            emailUser: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
            operateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaiementFormationPrivee', () => {
      it('should return NewPaiementFormationPrivee for default PaiementFormationPrivee initial value', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup(sampleWithNewData);

        const paiementFormationPrivee = service.getPaiementFormationPrivee(formGroup) as any;

        expect(paiementFormationPrivee).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementFormationPrivee for empty PaiementFormationPrivee initial value', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup();

        const paiementFormationPrivee = service.getPaiementFormationPrivee(formGroup) as any;

        expect(paiementFormationPrivee).toMatchObject({});
      });

      it('should return IPaiementFormationPrivee', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup(sampleWithRequiredData);

        const paiementFormationPrivee = service.getPaiementFormationPrivee(formGroup) as any;

        expect(paiementFormationPrivee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementFormationPrivee should not enable id FormControl', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementFormationPrivee should disable id FormControl', () => {
        const formGroup = service.createPaiementFormationPriveeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
