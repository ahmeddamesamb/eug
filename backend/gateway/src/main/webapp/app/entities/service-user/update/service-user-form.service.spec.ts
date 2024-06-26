import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../service-user.test-samples';

import { ServiceUserFormService } from './service-user-form.service';

describe('ServiceUser Form Service', () => {
  let service: ServiceUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceUserFormService);
  });

  describe('Service methods', () => {
    describe('createServiceUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            dateAjout: expect.any(Object),
            actifYN: expect.any(Object),
          }),
        );
      });

      it('passing IServiceUser should create a new form with FormGroup', () => {
        const formGroup = service.createServiceUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            dateAjout: expect.any(Object),
            actifYN: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceUser', () => {
      it('should return NewServiceUser for default ServiceUser initial value', () => {
        const formGroup = service.createServiceUserFormGroup(sampleWithNewData);

        const serviceUser = service.getServiceUser(formGroup) as any;

        expect(serviceUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceUser for empty ServiceUser initial value', () => {
        const formGroup = service.createServiceUserFormGroup();

        const serviceUser = service.getServiceUser(formGroup) as any;

        expect(serviceUser).toMatchObject({});
      });

      it('should return IServiceUser', () => {
        const formGroup = service.createServiceUserFormGroup(sampleWithRequiredData);

        const serviceUser = service.getServiceUser(formGroup) as any;

        expect(serviceUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceUser should not enable id FormControl', () => {
        const formGroup = service.createServiceUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceUser should disable id FormControl', () => {
        const formGroup = service.createServiceUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
