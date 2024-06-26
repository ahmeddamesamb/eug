import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../information-image.test-samples';

import { InformationImageFormService } from './information-image-form.service';

describe('InformationImage Form Service', () => {
  let service: InformationImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InformationImageFormService);
  });

  describe('Service methods', () => {
    describe('createInformationImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInformationImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            cheminPath: expect.any(Object),
            cheminFile: expect.any(Object),
            enCoursYN: expect.any(Object),
          }),
        );
      });

      it('passing IInformationImage should create a new form with FormGroup', () => {
        const formGroup = service.createInformationImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            cheminPath: expect.any(Object),
            cheminFile: expect.any(Object),
            enCoursYN: expect.any(Object),
          }),
        );
      });
    });

    describe('getInformationImage', () => {
      it('should return NewInformationImage for default InformationImage initial value', () => {
        const formGroup = service.createInformationImageFormGroup(sampleWithNewData);

        const informationImage = service.getInformationImage(formGroup) as any;

        expect(informationImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewInformationImage for empty InformationImage initial value', () => {
        const formGroup = service.createInformationImageFormGroup();

        const informationImage = service.getInformationImage(formGroup) as any;

        expect(informationImage).toMatchObject({});
      });

      it('should return IInformationImage', () => {
        const formGroup = service.createInformationImageFormGroup(sampleWithRequiredData);

        const informationImage = service.getInformationImage(formGroup) as any;

        expect(informationImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInformationImage should not enable id FormControl', () => {
        const formGroup = service.createInformationImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInformationImage should disable id FormControl', () => {
        const formGroup = service.createInformationImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
