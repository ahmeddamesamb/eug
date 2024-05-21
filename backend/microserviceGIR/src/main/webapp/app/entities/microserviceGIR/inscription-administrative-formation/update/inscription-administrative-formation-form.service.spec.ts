import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inscription-administrative-formation.test-samples';

import { InscriptionAdministrativeFormationFormService } from './inscription-administrative-formation-form.service';

describe('InscriptionAdministrativeFormation Form Service', () => {
  let service: InscriptionAdministrativeFormationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionAdministrativeFormationFormService);
  });

  describe('Service methods', () => {
    describe('createInscriptionAdministrativeFormationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inscriptionPrincipaleYN: expect.any(Object),
            inscriptionAnnuleeYN: expect.any(Object),
            paiementFraisOblYN: expect.any(Object),
            paiementFraisIntegergYN: expect.any(Object),
            certificatDelivreYN: expect.any(Object),
            dateChoixFormation: expect.any(Object),
            dateValidationInscription: expect.any(Object),
            inscriptionAdministrative: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });

      it('passing IInscriptionAdministrativeFormation should create a new form with FormGroup', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inscriptionPrincipaleYN: expect.any(Object),
            inscriptionAnnuleeYN: expect.any(Object),
            paiementFraisOblYN: expect.any(Object),
            paiementFraisIntegergYN: expect.any(Object),
            certificatDelivreYN: expect.any(Object),
            dateChoixFormation: expect.any(Object),
            dateValidationInscription: expect.any(Object),
            inscriptionAdministrative: expect.any(Object),
            formation: expect.any(Object),
          }),
        );
      });
    });

    describe('getInscriptionAdministrativeFormation', () => {
      it('should return NewInscriptionAdministrativeFormation for default InscriptionAdministrativeFormation initial value', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup(sampleWithNewData);

        const inscriptionAdministrativeFormation = service.getInscriptionAdministrativeFormation(formGroup) as any;

        expect(inscriptionAdministrativeFormation).toMatchObject(sampleWithNewData);
      });

      it('should return NewInscriptionAdministrativeFormation for empty InscriptionAdministrativeFormation initial value', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup();

        const inscriptionAdministrativeFormation = service.getInscriptionAdministrativeFormation(formGroup) as any;

        expect(inscriptionAdministrativeFormation).toMatchObject({});
      });

      it('should return IInscriptionAdministrativeFormation', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup(sampleWithRequiredData);

        const inscriptionAdministrativeFormation = service.getInscriptionAdministrativeFormation(formGroup) as any;

        expect(inscriptionAdministrativeFormation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInscriptionAdministrativeFormation should not enable id FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInscriptionAdministrativeFormation should disable id FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
