import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../baccalaureat.test-samples';

import { BaccalaureatFormService } from './baccalaureat-form.service';

describe('Baccalaureat Form Service', () => {
  let service: BaccalaureatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaccalaureatFormService);
  });

  describe('Service methods', () => {
    describe('createBaccalaureatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBaccalaureatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origineScolaire: expect.any(Object),
            anneeBac: expect.any(Object),
            numeroTable: expect.any(Object),
            natureBac: expect.any(Object),
            mentionBac: expect.any(Object),
            moyenneSelectionBac: expect.any(Object),
            moyenneBac: expect.any(Object),
            etudiant: expect.any(Object),
            serie: expect.any(Object),
          }),
        );
      });

      it('passing IBaccalaureat should create a new form with FormGroup', () => {
        const formGroup = service.createBaccalaureatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origineScolaire: expect.any(Object),
            anneeBac: expect.any(Object),
            numeroTable: expect.any(Object),
            natureBac: expect.any(Object),
            mentionBac: expect.any(Object),
            moyenneSelectionBac: expect.any(Object),
            moyenneBac: expect.any(Object),
            etudiant: expect.any(Object),
            serie: expect.any(Object),
          }),
        );
      });
    });

    describe('getBaccalaureat', () => {
      it('should return NewBaccalaureat for default Baccalaureat initial value', () => {
        const formGroup = service.createBaccalaureatFormGroup(sampleWithNewData);

        const baccalaureat = service.getBaccalaureat(formGroup) as any;

        expect(baccalaureat).toMatchObject(sampleWithNewData);
      });

      it('should return NewBaccalaureat for empty Baccalaureat initial value', () => {
        const formGroup = service.createBaccalaureatFormGroup();

        const baccalaureat = service.getBaccalaureat(formGroup) as any;

        expect(baccalaureat).toMatchObject({});
      });

      it('should return IBaccalaureat', () => {
        const formGroup = service.createBaccalaureatFormGroup(sampleWithRequiredData);

        const baccalaureat = service.getBaccalaureat(formGroup) as any;

        expect(baccalaureat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBaccalaureat should not enable id FormControl', () => {
        const formGroup = service.createBaccalaureatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBaccalaureat should disable id FormControl', () => {
        const formGroup = service.createBaccalaureatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
