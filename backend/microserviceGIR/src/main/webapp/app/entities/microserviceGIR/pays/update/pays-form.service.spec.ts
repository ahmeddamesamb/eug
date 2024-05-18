import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pays.test-samples';

import { PaysFormService } from './pays-form.service';

describe('Pays Form Service', () => {
  let service: PaysFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaysFormService);
  });

  describe('Service methods', () => {
    describe('createPaysFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaysFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libellePays: expect.any(Object),
            paysEnAnglais: expect.any(Object),
            nationalite: expect.any(Object),
            codePays: expect.any(Object),
            uEMOAYN: expect.any(Object),
            cEDEAOYN: expect.any(Object),
            rIMYN: expect.any(Object),
            autreYN: expect.any(Object),
            estEtrangerYN: expect.any(Object),
            zones: expect.any(Object),
          }),
        );
      });

      it('passing IPays should create a new form with FormGroup', () => {
        const formGroup = service.createPaysFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libellePays: expect.any(Object),
            paysEnAnglais: expect.any(Object),
            nationalite: expect.any(Object),
            codePays: expect.any(Object),
            uEMOAYN: expect.any(Object),
            cEDEAOYN: expect.any(Object),
            rIMYN: expect.any(Object),
            autreYN: expect.any(Object),
            estEtrangerYN: expect.any(Object),
            zones: expect.any(Object),
          }),
        );
      });
    });

    describe('getPays', () => {
      it('should return NewPays for default Pays initial value', () => {
        const formGroup = service.createPaysFormGroup(sampleWithNewData);

        const pays = service.getPays(formGroup) as any;

        expect(pays).toMatchObject(sampleWithNewData);
      });

      it('should return NewPays for empty Pays initial value', () => {
        const formGroup = service.createPaysFormGroup();

        const pays = service.getPays(formGroup) as any;

        expect(pays).toMatchObject({});
      });

      it('should return IPays', () => {
        const formGroup = service.createPaysFormGroup(sampleWithRequiredData);

        const pays = service.getPays(formGroup) as any;

        expect(pays).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPays should not enable id FormControl', () => {
        const formGroup = service.createPaysFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPays should disable id FormControl', () => {
        const formGroup = service.createPaysFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
