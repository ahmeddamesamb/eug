import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inscription-doctorat.test-samples';

import { InscriptionDoctoratFormService } from './inscription-doctorat-form.service';

describe('InscriptionDoctorat Form Service', () => {
  let service: InscriptionDoctoratFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionDoctoratFormService);
  });

  describe('Service methods', () => {
    describe('createInscriptionDoctoratFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceFinancement: expect.any(Object),
            coEncadreurId: expect.any(Object),
            nombreInscription: expect.any(Object),
            doctorat: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
          }),
        );
      });

      it('passing IInscriptionDoctorat should create a new form with FormGroup', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sourceFinancement: expect.any(Object),
            coEncadreurId: expect.any(Object),
            nombreInscription: expect.any(Object),
            doctorat: expect.any(Object),
            inscriptionAdministrativeFormation: expect.any(Object),
          }),
        );
      });
    });

    describe('getInscriptionDoctorat', () => {
      it('should return NewInscriptionDoctorat for default InscriptionDoctorat initial value', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup(sampleWithNewData);

        const inscriptionDoctorat = service.getInscriptionDoctorat(formGroup) as any;

        expect(inscriptionDoctorat).toMatchObject(sampleWithNewData);
      });

      it('should return NewInscriptionDoctorat for empty InscriptionDoctorat initial value', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup();

        const inscriptionDoctorat = service.getInscriptionDoctorat(formGroup) as any;

        expect(inscriptionDoctorat).toMatchObject({});
      });

      it('should return IInscriptionDoctorat', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup(sampleWithRequiredData);

        const inscriptionDoctorat = service.getInscriptionDoctorat(formGroup) as any;

        expect(inscriptionDoctorat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInscriptionDoctorat should not enable id FormControl', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInscriptionDoctorat should disable id FormControl', () => {
        const formGroup = service.createInscriptionDoctoratFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
