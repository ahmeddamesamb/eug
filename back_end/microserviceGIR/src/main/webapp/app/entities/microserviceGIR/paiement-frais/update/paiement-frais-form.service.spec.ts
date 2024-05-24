import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paiement-frais.test-samples';

import { PaiementFraisFormService } from './paiement-frais-form.service';

describe('PaiementFrais Form Service', () => {
  let service: PaiementFraisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaiementFraisFormService);
  });

  describe('Service methods', () => {
    describe('createPaiementFraisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaiementFraisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePaiement: expect.any(Object),
            obligatoireYN: expect.any(Object),
            echeancePayeeYN: expect.any(Object),
            emailUser: expect.any(Object),
            dateForclos: expect.any(Object),
            forclosYN: expect.any(Object),
            frais: expect.any(Object),
            operateur: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
          }),
        );
      });

      it('passing IPaiementFrais should create a new form with FormGroup', () => {
        const formGroup = service.createPaiementFraisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePaiement: expect.any(Object),
            obligatoireYN: expect.any(Object),
            echeancePayeeYN: expect.any(Object),
            emailUser: expect.any(Object),
            dateForclos: expect.any(Object),
            forclosYN: expect.any(Object),
            frais: expect.any(Object),
            operateur: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaiementFrais', () => {
      it('should return NewPaiementFrais for default PaiementFrais initial value', () => {
        const formGroup = service.createPaiementFraisFormGroup(sampleWithNewData);

        const paiementFrais = service.getPaiementFrais(formGroup) as any;

        expect(paiementFrais).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaiementFrais for empty PaiementFrais initial value', () => {
        const formGroup = service.createPaiementFraisFormGroup();

        const paiementFrais = service.getPaiementFrais(formGroup) as any;

        expect(paiementFrais).toMatchObject({});
      });

      it('should return IPaiementFrais', () => {
        const formGroup = service.createPaiementFraisFormGroup(sampleWithRequiredData);

        const paiementFrais = service.getPaiementFrais(formGroup) as any;

        expect(paiementFrais).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaiementFrais should not enable id FormControl', () => {
        const formGroup = service.createPaiementFraisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaiementFrais should disable id FormControl', () => {
        const formGroup = service.createPaiementFraisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
