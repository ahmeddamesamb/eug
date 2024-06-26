import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../discipline-sportive-etudiant.test-samples';

import { DisciplineSportiveEtudiantFormService } from './discipline-sportive-etudiant-form.service';

describe('DisciplineSportiveEtudiant Form Service', () => {
  let service: DisciplineSportiveEtudiantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisciplineSportiveEtudiantFormService);
  });

  describe('Service methods', () => {
    describe('createDisciplineSportiveEtudiantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licenceSportiveYN: expect.any(Object),
            competitionUGBYN: expect.any(Object),
            disciplineSportive: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });

      it('passing IDisciplineSportiveEtudiant should create a new form with FormGroup', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licenceSportiveYN: expect.any(Object),
            competitionUGBYN: expect.any(Object),
            disciplineSportive: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisciplineSportiveEtudiant', () => {
      it('should return NewDisciplineSportiveEtudiant for default DisciplineSportiveEtudiant initial value', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup(sampleWithNewData);

        const disciplineSportiveEtudiant = service.getDisciplineSportiveEtudiant(formGroup) as any;

        expect(disciplineSportiveEtudiant).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisciplineSportiveEtudiant for empty DisciplineSportiveEtudiant initial value', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup();

        const disciplineSportiveEtudiant = service.getDisciplineSportiveEtudiant(formGroup) as any;

        expect(disciplineSportiveEtudiant).toMatchObject({});
      });

      it('should return IDisciplineSportiveEtudiant', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup(sampleWithRequiredData);

        const disciplineSportiveEtudiant = service.getDisciplineSportiveEtudiant(formGroup) as any;

        expect(disciplineSportiveEtudiant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisciplineSportiveEtudiant should not enable id FormControl', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisciplineSportiveEtudiant should disable id FormControl', () => {
        const formGroup = service.createDisciplineSportiveEtudiantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
