import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../formation-autorisee.test-samples';

import { FormationAutoriseeFormService } from './formation-autorisee-form.service';

describe('FormationAutorisee Form Service', () => {
  let service: FormationAutoriseeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationAutoriseeFormService);
  });

  describe('Service methods', () => {
    describe('createFormationAutoriseeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormationAutoriseeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            enCoursYN: expect.any(Object),
            actifYN: expect.any(Object),
            formations: expect.any(Object),
          }),
        );
      });

      it('passing IFormationAutorisee should create a new form with FormGroup', () => {
        const formGroup = service.createFormationAutoriseeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            enCoursYN: expect.any(Object),
            actifYN: expect.any(Object),
            formations: expect.any(Object),
          }),
        );
      });
    });

    describe('getFormationAutorisee', () => {
      it('should return NewFormationAutorisee for default FormationAutorisee initial value', () => {
        const formGroup = service.createFormationAutoriseeFormGroup(sampleWithNewData);

        const formationAutorisee = service.getFormationAutorisee(formGroup) as any;

        expect(formationAutorisee).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormationAutorisee for empty FormationAutorisee initial value', () => {
        const formGroup = service.createFormationAutoriseeFormGroup();

        const formationAutorisee = service.getFormationAutorisee(formGroup) as any;

        expect(formationAutorisee).toMatchObject({});
      });

      it('should return IFormationAutorisee', () => {
        const formGroup = service.createFormationAutoriseeFormGroup(sampleWithRequiredData);

        const formationAutorisee = service.getFormationAutorisee(formGroup) as any;

        expect(formationAutorisee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormationAutorisee should not enable id FormControl', () => {
        const formGroup = service.createFormationAutoriseeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormationAutorisee should disable id FormControl', () => {
        const formGroup = service.createFormationAutoriseeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
