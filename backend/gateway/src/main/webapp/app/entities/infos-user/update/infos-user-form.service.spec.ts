import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../infos-user.test-samples';

import { InfosUserFormService } from './infos-user-form.service';

describe('InfosUser Form Service', () => {
  let service: InfosUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InfosUserFormService);
  });

  describe('Service methods', () => {
    describe('createInfosUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInfosUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAjout: expect.any(Object),
            actifYN: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IInfosUser should create a new form with FormGroup', () => {
        const formGroup = service.createInfosUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAjout: expect.any(Object),
            actifYN: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getInfosUser', () => {
      it('should return NewInfosUser for default InfosUser initial value', () => {
        const formGroup = service.createInfosUserFormGroup(sampleWithNewData);

        const infosUser = service.getInfosUser(formGroup) as any;

        expect(infosUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewInfosUser for empty InfosUser initial value', () => {
        const formGroup = service.createInfosUserFormGroup();

        const infosUser = service.getInfosUser(formGroup) as any;

        expect(infosUser).toMatchObject({});
      });

      it('should return IInfosUser', () => {
        const formGroup = service.createInfosUserFormGroup(sampleWithRequiredData);

        const infosUser = service.getInfosUser(formGroup) as any;

        expect(infosUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInfosUser should not enable id FormControl', () => {
        const formGroup = service.createInfosUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInfosUser should disable id FormControl', () => {
        const formGroup = service.createInfosUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
