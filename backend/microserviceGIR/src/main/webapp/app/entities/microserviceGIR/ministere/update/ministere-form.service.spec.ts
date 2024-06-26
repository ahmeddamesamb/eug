import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ministere.test-samples';

import { MinistereFormService } from './ministere-form.service';

describe('Ministere Form Service', () => {
  let service: MinistereFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MinistereFormService);
  });

  describe('Service methods', () => {
    describe('createMinistereFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMinistereFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomMinistere: expect.any(Object),
            sigleMinistere: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            enCoursYN: expect.any(Object),
            actifYN: expect.any(Object),
          }),
        );
      });

      it('passing IMinistere should create a new form with FormGroup', () => {
        const formGroup = service.createMinistereFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomMinistere: expect.any(Object),
            sigleMinistere: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            enCoursYN: expect.any(Object),
            actifYN: expect.any(Object),
          }),
        );
      });
    });

    describe('getMinistere', () => {
      it('should return NewMinistere for default Ministere initial value', () => {
        const formGroup = service.createMinistereFormGroup(sampleWithNewData);

        const ministere = service.getMinistere(formGroup) as any;

        expect(ministere).toMatchObject(sampleWithNewData);
      });

      it('should return NewMinistere for empty Ministere initial value', () => {
        const formGroup = service.createMinistereFormGroup();

        const ministere = service.getMinistere(formGroup) as any;

        expect(ministere).toMatchObject({});
      });

      it('should return IMinistere', () => {
        const formGroup = service.createMinistereFormGroup(sampleWithRequiredData);

        const ministere = service.getMinistere(formGroup) as any;

        expect(ministere).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMinistere should not enable id FormControl', () => {
        const formGroup = service.createMinistereFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMinistere should disable id FormControl', () => {
        const formGroup = service.createMinistereFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
