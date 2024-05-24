import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../frais.test-samples';

import { FraisFormService } from './frais-form.service';

describe('Frais Form Service', () => {
  let service: FraisFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FraisFormService);
  });

  describe('Service methods', () => {
    describe('createFraisFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFraisFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeurFrais: expect.any(Object),
            descriptionFrais: expect.any(Object),
            fraisPourAssimileYN: expect.any(Object),
            cycle: expect.any(Object),
            dia: expect.any(Object),
            dip: expect.any(Object),
            dipPrivee: expect.any(Object),
            dateApplication: expect.any(Object),
            dateFin: expect.any(Object),
            estEnApplicationYN: expect.any(Object),
            typeFrais: expect.any(Object),
          }),
        );
      });

      it('passing IFrais should create a new form with FormGroup', () => {
        const formGroup = service.createFraisFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeurFrais: expect.any(Object),
            descriptionFrais: expect.any(Object),
            fraisPourAssimileYN: expect.any(Object),
            cycle: expect.any(Object),
            dia: expect.any(Object),
            dip: expect.any(Object),
            dipPrivee: expect.any(Object),
            dateApplication: expect.any(Object),
            dateFin: expect.any(Object),
            estEnApplicationYN: expect.any(Object),
            typeFrais: expect.any(Object),
          }),
        );
      });
    });

    describe('getFrais', () => {
      it('should return NewFrais for default Frais initial value', () => {
        const formGroup = service.createFraisFormGroup(sampleWithNewData);

        const frais = service.getFrais(formGroup) as any;

        expect(frais).toMatchObject(sampleWithNewData);
      });

      it('should return NewFrais for empty Frais initial value', () => {
        const formGroup = service.createFraisFormGroup();

        const frais = service.getFrais(formGroup) as any;

        expect(frais).toMatchObject({});
      });

      it('should return IFrais', () => {
        const formGroup = service.createFraisFormGroup(sampleWithRequiredData);

        const frais = service.getFrais(formGroup) as any;

        expect(frais).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFrais should not enable id FormControl', () => {
        const formGroup = service.createFraisFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFrais should disable id FormControl', () => {
        const formGroup = service.createFraisFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
