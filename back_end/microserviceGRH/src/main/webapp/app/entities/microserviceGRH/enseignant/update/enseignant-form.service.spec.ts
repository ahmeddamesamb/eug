import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../enseignant.test-samples';

import { EnseignantFormService } from './enseignant-form.service';

describe('Enseignant Form Service', () => {
  let service: EnseignantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnseignantFormService);
  });

  describe('Service methods', () => {
    describe('createEnseignantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnseignantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titreCoEncadreur: expect.any(Object),
            nomEnseignant: expect.any(Object),
            prenomEnseignant: expect.any(Object),
            emailEnseignant: expect.any(Object),
            telephoneEnseignant: expect.any(Object),
            titresId: expect.any(Object),
            adresse: expect.any(Object),
            numeroPoste: expect.any(Object),
            photoEnseignant: expect.any(Object),
          }),
        );
      });

      it('passing IEnseignant should create a new form with FormGroup', () => {
        const formGroup = service.createEnseignantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titreCoEncadreur: expect.any(Object),
            nomEnseignant: expect.any(Object),
            prenomEnseignant: expect.any(Object),
            emailEnseignant: expect.any(Object),
            telephoneEnseignant: expect.any(Object),
            titresId: expect.any(Object),
            adresse: expect.any(Object),
            numeroPoste: expect.any(Object),
            photoEnseignant: expect.any(Object),
          }),
        );
      });
    });

    describe('getEnseignant', () => {
      it('should return NewEnseignant for default Enseignant initial value', () => {
        const formGroup = service.createEnseignantFormGroup(sampleWithNewData);

        const enseignant = service.getEnseignant(formGroup) as any;

        expect(enseignant).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnseignant for empty Enseignant initial value', () => {
        const formGroup = service.createEnseignantFormGroup();

        const enseignant = service.getEnseignant(formGroup) as any;

        expect(enseignant).toMatchObject({});
      });

      it('should return IEnseignant', () => {
        const formGroup = service.createEnseignantFormGroup(sampleWithRequiredData);

        const enseignant = service.getEnseignant(formGroup) as any;

        expect(enseignant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnseignant should not enable id FormControl', () => {
        const formGroup = service.createEnseignantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnseignant should disable id FormControl', () => {
        const formGroup = service.createEnseignantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
