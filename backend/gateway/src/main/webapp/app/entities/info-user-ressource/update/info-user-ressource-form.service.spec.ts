import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../info-user-ressource.test-samples';

import { InfoUserRessourceFormService } from './info-user-ressource-form.service';

describe('InfoUserRessource Form Service', () => {
  let service: InfoUserRessourceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InfoUserRessourceFormService);
  });

  describe('Service methods', () => {
    describe('createInfoUserRessourceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInfoUserRessourceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAjout: expect.any(Object),
            enCoursYN: expect.any(Object),
            infosUser: expect.any(Object),
            ressource: expect.any(Object),
          }),
        );
      });

      it('passing IInfoUserRessource should create a new form with FormGroup', () => {
        const formGroup = service.createInfoUserRessourceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAjout: expect.any(Object),
            enCoursYN: expect.any(Object),
            infosUser: expect.any(Object),
            ressource: expect.any(Object),
          }),
        );
      });
    });

    describe('getInfoUserRessource', () => {
      it('should return NewInfoUserRessource for default InfoUserRessource initial value', () => {
        const formGroup = service.createInfoUserRessourceFormGroup(sampleWithNewData);

        const infoUserRessource = service.getInfoUserRessource(formGroup) as any;

        expect(infoUserRessource).toMatchObject(sampleWithNewData);
      });

      it('should return NewInfoUserRessource for empty InfoUserRessource initial value', () => {
        const formGroup = service.createInfoUserRessourceFormGroup();

        const infoUserRessource = service.getInfoUserRessource(formGroup) as any;

        expect(infoUserRessource).toMatchObject({});
      });

      it('should return IInfoUserRessource', () => {
        const formGroup = service.createInfoUserRessourceFormGroup(sampleWithRequiredData);

        const infoUserRessource = service.getInfoUserRessource(formGroup) as any;

        expect(infoUserRessource).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInfoUserRessource should not enable id FormControl', () => {
        const formGroup = service.createInfoUserRessourceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInfoUserRessource should disable id FormControl', () => {
        const formGroup = service.createInfoUserRessourceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
