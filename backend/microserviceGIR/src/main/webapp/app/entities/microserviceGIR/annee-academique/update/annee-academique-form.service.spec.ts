import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../annee-academique.test-samples';

import { AnneeAcademiqueFormService } from './annee-academique-form.service';

describe('AnneeAcademique Form Service', () => {
  let service: AnneeAcademiqueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnneeAcademiqueFormService);
  });

  describe('Service methods', () => {
    describe('createAnneeAcademiqueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleAnneeAcademique: expect.any(Object),
            anneeCourante: expect.any(Object),
          }),
        );
      });

      it('passing IAnneeAcademique should create a new form with FormGroup', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleAnneeAcademique: expect.any(Object),
            anneeCourante: expect.any(Object),
          }),
        );
      });
    });

    describe('getAnneeAcademique', () => {
      it('should return NewAnneeAcademique for default AnneeAcademique initial value', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup(sampleWithNewData);

        const anneeAcademique = service.getAnneeAcademique(formGroup) as any;

        expect(anneeAcademique).toMatchObject(sampleWithNewData);
      });

      it('should return NewAnneeAcademique for empty AnneeAcademique initial value', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup();

        const anneeAcademique = service.getAnneeAcademique(formGroup) as any;

        expect(anneeAcademique).toMatchObject({});
      });

      it('should return IAnneeAcademique', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup(sampleWithRequiredData);

        const anneeAcademique = service.getAnneeAcademique(formGroup) as any;

        expect(anneeAcademique).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAnneeAcademique should not enable id FormControl', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAnneeAcademique should disable id FormControl', () => {
        const formGroup = service.createAnneeAcademiqueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
