import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../processus-inscription-administrative.test-samples';

import { ProcessusInscriptionAdministrativeFormService } from './processus-inscription-administrative-form.service';

describe('ProcessusInscriptionAdministrative Form Service', () => {
  let service: ProcessusInscriptionAdministrativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcessusInscriptionAdministrativeFormService);
  });

  describe('Service methods', () => {
    describe('createProcessusInscriptionAdministrativeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup();

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

      it('passing IProcessusInscriptionAdministrative should create a new form with FormGroup', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup(sampleWithRequiredData);

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

    describe('getProcessusInscriptionAdministrative', () => {
      it('should return NewProcessusInscriptionAdministrative for default ProcessusInscriptionAdministrative initial value', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup(sampleWithNewData);

        const processusInscriptionAdministrative = service.getProcessusInscriptionAdministrative(formGroup) as any;

        expect(processusInscriptionAdministrative).toMatchObject(sampleWithNewData);
      });

      it('should return NewProcessusInscriptionAdministrative for empty ProcessusInscriptionAdministrative initial value', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup();

        const processusInscriptionAdministrative = service.getProcessusInscriptionAdministrative(formGroup) as any;

        expect(processusInscriptionAdministrative).toMatchObject({});
      });

      it('should return IProcessusInscriptionAdministrative', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup(sampleWithRequiredData);

        const processusInscriptionAdministrative = service.getProcessusInscriptionAdministrative(formGroup) as any;

        expect(processusInscriptionAdministrative).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProcessusInscriptionAdministrative should not enable id FormControl', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProcessusInscriptionAdministrative should disable id FormControl', () => {
        const formGroup = service.createProcessusInscriptionAdministrativeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
