import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ressource-aide.test-samples';

import { RessourceAideFormService } from './ressource-aide-form.service';

describe('RessourceAide Form Service', () => {
  let service: RessourceAideFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RessourceAideFormService);
  });

  describe('Service methods', () => {
    describe('createRessourceAideFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRessourceAideFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IRessourceAide should create a new form with FormGroup', () => {
        const formGroup = service.createRessourceAideFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getRessourceAide', () => {
      it('should return NewRessourceAide for default RessourceAide initial value', () => {
        const formGroup = service.createRessourceAideFormGroup(sampleWithNewData);

        const ressourceAide = service.getRessourceAide(formGroup) as any;

        expect(ressourceAide).toMatchObject(sampleWithNewData);
      });

      it('should return NewRessourceAide for empty RessourceAide initial value', () => {
        const formGroup = service.createRessourceAideFormGroup();

        const ressourceAide = service.getRessourceAide(formGroup) as any;

        expect(ressourceAide).toMatchObject({});
      });

      it('should return IRessourceAide', () => {
        const formGroup = service.createRessourceAideFormGroup(sampleWithRequiredData);

        const ressourceAide = service.getRessourceAide(formGroup) as any;

        expect(ressourceAide).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRessourceAide should not enable id FormControl', () => {
        const formGroup = service.createRessourceAideFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRessourceAide should disable id FormControl', () => {
        const formGroup = service.createRessourceAideFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
