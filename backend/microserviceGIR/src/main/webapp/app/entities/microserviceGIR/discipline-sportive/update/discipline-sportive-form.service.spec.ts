import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../discipline-sportive.test-samples';

import { DisciplineSportiveFormService } from './discipline-sportive-form.service';

describe('DisciplineSportive Form Service', () => {
  let service: DisciplineSportiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisciplineSportiveFormService);
  });

  describe('Service methods', () => {
    describe('createDisciplineSportiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDisciplineSportiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleDisciplineSportive: expect.any(Object),
          }),
        );
      });

      it('passing IDisciplineSportive should create a new form with FormGroup', () => {
        const formGroup = service.createDisciplineSportiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleDisciplineSportive: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisciplineSportive', () => {
      it('should return NewDisciplineSportive for default DisciplineSportive initial value', () => {
        const formGroup = service.createDisciplineSportiveFormGroup(sampleWithNewData);

        const disciplineSportive = service.getDisciplineSportive(formGroup) as any;

        expect(disciplineSportive).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisciplineSportive for empty DisciplineSportive initial value', () => {
        const formGroup = service.createDisciplineSportiveFormGroup();

        const disciplineSportive = service.getDisciplineSportive(formGroup) as any;

        expect(disciplineSportive).toMatchObject({});
      });

      it('should return IDisciplineSportive', () => {
        const formGroup = service.createDisciplineSportiveFormGroup(sampleWithRequiredData);

        const disciplineSportive = service.getDisciplineSportive(formGroup) as any;

        expect(disciplineSportive).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisciplineSportive should not enable id FormControl', () => {
        const formGroup = service.createDisciplineSportiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisciplineSportive should disable id FormControl', () => {
        const formGroup = service.createDisciplineSportiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
