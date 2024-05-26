import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../programmation-inscription.test-samples';

import { ProgrammationInscriptionFormService } from './programmation-inscription-form.service';

describe('ProgrammationInscription Form Service', () => {
  let service: ProgrammationInscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProgrammationInscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createProgrammationInscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleProgrammation: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            ouvertYN: expect.any(Object),
            emailUser: expect.any(Object),
            anneeAcademique: expect.any(Object),
            formation: expect.any(Object),
            campagne: expect.any(Object),
          }),
        );
      });

      it('passing IProgrammationInscription should create a new form with FormGroup', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleProgrammation: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            ouvertYN: expect.any(Object),
            emailUser: expect.any(Object),
            anneeAcademique: expect.any(Object),
            formation: expect.any(Object),
            campagne: expect.any(Object),
          }),
        );
      });
    });

    describe('getProgrammationInscription', () => {
      it('should return NewProgrammationInscription for default ProgrammationInscription initial value', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup(sampleWithNewData);

        const programmationInscription = service.getProgrammationInscription(formGroup) as any;

        expect(programmationInscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewProgrammationInscription for empty ProgrammationInscription initial value', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup();

        const programmationInscription = service.getProgrammationInscription(formGroup) as any;

        expect(programmationInscription).toMatchObject({});
      });

      it('should return IProgrammationInscription', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup(sampleWithRequiredData);

        const programmationInscription = service.getProgrammationInscription(formGroup) as any;

        expect(programmationInscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProgrammationInscription should not enable id FormControl', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProgrammationInscription should disable id FormControl', () => {
        const formGroup = service.createProgrammationInscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
