import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-profile-bloc-fonctionnel.test-samples';

import { UserProfileBlocFonctionnelFormService } from './user-profile-bloc-fonctionnel-form.service';

describe('UserProfileBlocFonctionnel Form Service', () => {
  let service: UserProfileBlocFonctionnelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserProfileBlocFonctionnelFormService);
  });

  describe('Service methods', () => {
    describe('createUserProfileBlocFonctionnelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            enCoursYN: expect.any(Object),
            userProfil: expect.any(Object),
            blocFonctionnel: expect.any(Object),
          }),
        );
      });

      it('passing IUserProfileBlocFonctionnel should create a new form with FormGroup', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            enCoursYN: expect.any(Object),
            userProfil: expect.any(Object),
            blocFonctionnel: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserProfileBlocFonctionnel', () => {
      it('should return NewUserProfileBlocFonctionnel for default UserProfileBlocFonctionnel initial value', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup(sampleWithNewData);

        const userProfileBlocFonctionnel = service.getUserProfileBlocFonctionnel(formGroup) as any;

        expect(userProfileBlocFonctionnel).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserProfileBlocFonctionnel for empty UserProfileBlocFonctionnel initial value', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup();

        const userProfileBlocFonctionnel = service.getUserProfileBlocFonctionnel(formGroup) as any;

        expect(userProfileBlocFonctionnel).toMatchObject({});
      });

      it('should return IUserProfileBlocFonctionnel', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup(sampleWithRequiredData);

        const userProfileBlocFonctionnel = service.getUserProfileBlocFonctionnel(formGroup) as any;

        expect(userProfileBlocFonctionnel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserProfileBlocFonctionnel should not enable id FormControl', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserProfileBlocFonctionnel should disable id FormControl', () => {
        const formGroup = service.createUserProfileBlocFonctionnelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
