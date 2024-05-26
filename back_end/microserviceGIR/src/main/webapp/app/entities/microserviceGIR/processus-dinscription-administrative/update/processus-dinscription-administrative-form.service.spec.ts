import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../processus-dinscription-administrative.test-samples';

import { ProcessusDinscriptionAdministrativeFormService } from './processus-dinscription-administrative-form.service';

describe('ProcessusDinscriptionAdministrative Form Service', () => {
  let service: ProcessusDinscriptionAdministrativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcessusDinscriptionAdministrativeFormService);
  });

  describe('Service methods', () => {
    describe('createProcessusDinscriptionAdministrativeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inscriptionDemarreeYN: expect.any(Object),
            dateDemarrageInscription: expect.any(Object),
            inscriptionAnnuleeYN: expect.any(Object),
            dateAnnulationInscription: expect.any(Object),
            apteYN: expect.any(Object),
            dateVisiteMedicale: expect.any(Object),
            beneficiaireCROUSYN: expect.any(Object),
            dateRemiseQuitusCROUS: expect.any(Object),
            nouveauCodeBUYN: expect.any(Object),
            quitusBUYN: expect.any(Object),
            dateRemiseQuitusBU: expect.any(Object),
            exporterBUYN: expect.any(Object),
            fraisObligatoiresPayesYN: expect.any(Object),
            datePaiementFraisObligatoires: expect.any(Object),
            numeroQuitusCROUS: expect.any(Object),
            carteEturemiseYN: expect.any(Object),
            carteDupremiseYN: expect.any(Object),
            dateRemiseCarteEtu: expect.any(Object),
            dateRemiseCarteDup: expect.any(Object),
            inscritAdministrativementYN: expect.any(Object),
            dateInscriptionAdministrative: expect.any(Object),
            derniereModification: expect.any(Object),
            inscritOnlineYN: expect.any(Object),
            emailUser: expect.any(Object),
            inscriptionAdministrative: expect.any(Object),
          }),
        );
      });

      it('passing IProcessusDinscriptionAdministrative should create a new form with FormGroup', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            inscriptionDemarreeYN: expect.any(Object),
            dateDemarrageInscription: expect.any(Object),
            inscriptionAnnuleeYN: expect.any(Object),
            dateAnnulationInscription: expect.any(Object),
            apteYN: expect.any(Object),
            dateVisiteMedicale: expect.any(Object),
            beneficiaireCROUSYN: expect.any(Object),
            dateRemiseQuitusCROUS: expect.any(Object),
            nouveauCodeBUYN: expect.any(Object),
            quitusBUYN: expect.any(Object),
            dateRemiseQuitusBU: expect.any(Object),
            exporterBUYN: expect.any(Object),
            fraisObligatoiresPayesYN: expect.any(Object),
            datePaiementFraisObligatoires: expect.any(Object),
            numeroQuitusCROUS: expect.any(Object),
            carteEturemiseYN: expect.any(Object),
            carteDupremiseYN: expect.any(Object),
            dateRemiseCarteEtu: expect.any(Object),
            dateRemiseCarteDup: expect.any(Object),
            inscritAdministrativementYN: expect.any(Object),
            dateInscriptionAdministrative: expect.any(Object),
            derniereModification: expect.any(Object),
            inscritOnlineYN: expect.any(Object),
            emailUser: expect.any(Object),
            inscriptionAdministrative: expect.any(Object),
          }),
        );
      });
    });

    describe('getProcessusDinscriptionAdministrative', () => {
      it('should return NewProcessusDinscriptionAdministrative for default ProcessusDinscriptionAdministrative initial value', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup(sampleWithNewData);

        const processusDinscriptionAdministrative = service.getProcessusDinscriptionAdministrative(formGroup) as any;

        expect(processusDinscriptionAdministrative).toMatchObject(sampleWithNewData);
      });

      it('should return NewProcessusDinscriptionAdministrative for empty ProcessusDinscriptionAdministrative initial value', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup();

        const processusDinscriptionAdministrative = service.getProcessusDinscriptionAdministrative(formGroup) as any;

        expect(processusDinscriptionAdministrative).toMatchObject({});
      });

      it('should return IProcessusDinscriptionAdministrative', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup(sampleWithRequiredData);

        const processusDinscriptionAdministrative = service.getProcessusDinscriptionAdministrative(formGroup) as any;

        expect(processusDinscriptionAdministrative).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProcessusDinscriptionAdministrative should not enable id FormControl', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProcessusDinscriptionAdministrative should disable id FormControl', () => {
        const formGroup = service.createProcessusDinscriptionAdministrativeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
