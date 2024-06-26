import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bloc-fonctionnel.test-samples';

import { BlocFonctionnelFormService } from './bloc-fonctionnel-form.service';

describe('BlocFonctionnel Form Service', () => {
  let service: BlocFonctionnelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BlocFonctionnelFormService);
  });

  describe('Service methods', () => {
    describe('createBlocFonctionnelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBlocFonctionnelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleBloc: expect.any(Object),
            dateAjoutBloc: expect.any(Object),
            actifYN: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });

      it('passing IBlocFonctionnel should create a new form with FormGroup', () => {
        const formGroup = service.createBlocFonctionnelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleBloc: expect.any(Object),
            dateAjoutBloc: expect.any(Object),
            actifYN: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });
    });

    describe('getBlocFonctionnel', () => {
      it('should return NewBlocFonctionnel for default BlocFonctionnel initial value', () => {
        const formGroup = service.createBlocFonctionnelFormGroup(sampleWithNewData);

        const blocFonctionnel = service.getBlocFonctionnel(formGroup) as any;

        expect(blocFonctionnel).toMatchObject(sampleWithNewData);
      });

      it('should return NewBlocFonctionnel for empty BlocFonctionnel initial value', () => {
        const formGroup = service.createBlocFonctionnelFormGroup();

        const blocFonctionnel = service.getBlocFonctionnel(formGroup) as any;

        expect(blocFonctionnel).toMatchObject({});
      });

      it('should return IBlocFonctionnel', () => {
        const formGroup = service.createBlocFonctionnelFormGroup(sampleWithRequiredData);

        const blocFonctionnel = service.getBlocFonctionnel(formGroup) as any;

        expect(blocFonctionnel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBlocFonctionnel should not enable id FormControl', () => {
        const formGroup = service.createBlocFonctionnelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBlocFonctionnel should disable id FormControl', () => {
        const formGroup = service.createBlocFonctionnelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
