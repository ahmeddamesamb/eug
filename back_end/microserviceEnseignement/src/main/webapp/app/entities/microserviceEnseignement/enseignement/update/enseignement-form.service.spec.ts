import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../enseignement.test-samples';

import { EnseignementFormService } from './enseignement-form.service';

describe('Enseignement Form Service', () => {
  let service: EnseignementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnseignementFormService);
  });

  describe('Service methods', () => {
    describe('createEnseignementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnseignementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleEnseignements: expect.any(Object),
            volumeHoraire: expect.any(Object),
            nombreInscrits: expect.any(Object),
            groupeYN: expect.any(Object),
          }),
        );
      });

      it('passing IEnseignement should create a new form with FormGroup', () => {
        const formGroup = service.createEnseignementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleEnseignements: expect.any(Object),
            volumeHoraire: expect.any(Object),
            nombreInscrits: expect.any(Object),
            groupeYN: expect.any(Object),
          }),
        );
      });
    });

    describe('getEnseignement', () => {
      it('should return NewEnseignement for default Enseignement initial value', () => {
        const formGroup = service.createEnseignementFormGroup(sampleWithNewData);

        const enseignement = service.getEnseignement(formGroup) as any;

        expect(enseignement).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnseignement for empty Enseignement initial value', () => {
        const formGroup = service.createEnseignementFormGroup();

        const enseignement = service.getEnseignement(formGroup) as any;

        expect(enseignement).toMatchObject({});
      });

      it('should return IEnseignement', () => {
        const formGroup = service.createEnseignementFormGroup(sampleWithRequiredData);

        const enseignement = service.getEnseignement(formGroup) as any;

        expect(enseignement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnseignement should not enable id FormControl', () => {
        const formGroup = service.createEnseignementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnseignement should disable id FormControl', () => {
        const formGroup = service.createEnseignementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
