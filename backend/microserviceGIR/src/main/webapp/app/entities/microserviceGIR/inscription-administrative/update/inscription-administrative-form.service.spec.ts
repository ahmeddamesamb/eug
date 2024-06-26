import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inscription-administrative.test-samples';

import { InscriptionAdministrativeFormService } from './inscription-administrative-form.service';

describe('InscriptionAdministrative Form Service', () => {
  let service: InscriptionAdministrativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionAdministrativeFormService);
  });

  describe('Service methods', () => {
    describe('createInscriptionAdministrativeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nouveauInscritYN: expect.any(Object),
            repriseYN: expect.any(Object),
            autoriseYN: expect.any(Object),
            ordreInscription: expect.any(Object),
            typeAdmission: expect.any(Object),
            anneeAcademique: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });

      it('passing IInscriptionAdministrative should create a new form with FormGroup', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nouveauInscritYN: expect.any(Object),
            repriseYN: expect.any(Object),
            autoriseYN: expect.any(Object),
            ordreInscription: expect.any(Object),
            typeAdmission: expect.any(Object),
            anneeAcademique: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });
    });

    describe('getInscriptionAdministrative', () => {
      it('should return NewInscriptionAdministrative for default InscriptionAdministrative initial value', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup(sampleWithNewData);

        const inscriptionAdministrative = service.getInscriptionAdministrative(formGroup) as any;

        expect(inscriptionAdministrative).toMatchObject(sampleWithNewData);
      });

      it('should return NewInscriptionAdministrative for empty InscriptionAdministrative initial value', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup();

        const inscriptionAdministrative = service.getInscriptionAdministrative(formGroup) as any;

        expect(inscriptionAdministrative).toMatchObject({});
      });

      it('should return IInscriptionAdministrative', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup(sampleWithRequiredData);

        const inscriptionAdministrative = service.getInscriptionAdministrative(formGroup) as any;

        expect(inscriptionAdministrative).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInscriptionAdministrative should not enable id FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInscriptionAdministrative should disable id FormControl', () => {
        const formGroup = service.createInscriptionAdministrativeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
