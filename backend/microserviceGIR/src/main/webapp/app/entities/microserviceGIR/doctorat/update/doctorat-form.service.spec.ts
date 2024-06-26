import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../doctorat.test-samples';

import { DoctoratFormService } from './doctorat-form.service';

describe('Doctorat Form Service', () => {
  let service: DoctoratFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctoratFormService);
  });

  describe('Service methods', () => {
    describe('createDoctoratFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDoctoratFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sujet: expect.any(Object),
            anneeInscriptionDoctorat: expect.any(Object),
            encadreurId: expect.any(Object),
            laboratoirId: expect.any(Object),
          }),
        );
      });

      it('passing IDoctorat should create a new form with FormGroup', () => {
        const formGroup = service.createDoctoratFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sujet: expect.any(Object),
            anneeInscriptionDoctorat: expect.any(Object),
            encadreurId: expect.any(Object),
            laboratoirId: expect.any(Object),
          }),
        );
      });
    });

    describe('getDoctorat', () => {
      it('should return NewDoctorat for default Doctorat initial value', () => {
        const formGroup = service.createDoctoratFormGroup(sampleWithNewData);

        const doctorat = service.getDoctorat(formGroup) as any;

        expect(doctorat).toMatchObject(sampleWithNewData);
      });

      it('should return NewDoctorat for empty Doctorat initial value', () => {
        const formGroup = service.createDoctoratFormGroup();

        const doctorat = service.getDoctorat(formGroup) as any;

        expect(doctorat).toMatchObject({});
      });

      it('should return IDoctorat', () => {
        const formGroup = service.createDoctoratFormGroup(sampleWithRequiredData);

        const doctorat = service.getDoctorat(formGroup) as any;

        expect(doctorat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDoctorat should not enable id FormControl', () => {
        const formGroup = service.createDoctoratFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDoctorat should disable id FormControl', () => {
        const formGroup = service.createDoctoratFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
