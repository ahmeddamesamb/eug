import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../information-personnelle.test-samples';

import { InformationPersonnelleFormService } from './information-personnelle-form.service';

describe('InformationPersonnelle Form Service', () => {
  let service: InformationPersonnelleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InformationPersonnelleFormService);
  });

  describe('Service methods', () => {
    describe('createInformationPersonnelleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInformationPersonnelleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomEtu: expect.any(Object),
            nomJeuneFilleEtu: expect.any(Object),
            prenomEtu: expect.any(Object),
            statutMarital: expect.any(Object),
            regime: expect.any(Object),
            profession: expect.any(Object),
            adresseEtu: expect.any(Object),
            telEtu: expect.any(Object),
            emailEtu: expect.any(Object),
            adresseParent: expect.any(Object),
            telParent: expect.any(Object),
            emailParent: expect.any(Object),
            nomParent: expect.any(Object),
            prenomParent: expect.any(Object),
            handicapYN: expect.any(Object),
            ordiPersoYN: expect.any(Object),
            derniereModification: expect.any(Object),
            emailUser: expect.any(Object),
            etudiant: expect.any(Object),
            typeHadique: expect.any(Object),
            typeBourse: expect.any(Object),
          }),
        );
      });

      it('passing IInformationPersonnelle should create a new form with FormGroup', () => {
        const formGroup = service.createInformationPersonnelleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomEtu: expect.any(Object),
            nomJeuneFilleEtu: expect.any(Object),
            prenomEtu: expect.any(Object),
            statutMarital: expect.any(Object),
            regime: expect.any(Object),
            profession: expect.any(Object),
            adresseEtu: expect.any(Object),
            telEtu: expect.any(Object),
            emailEtu: expect.any(Object),
            adresseParent: expect.any(Object),
            telParent: expect.any(Object),
            emailParent: expect.any(Object),
            nomParent: expect.any(Object),
            prenomParent: expect.any(Object),
            handicapYN: expect.any(Object),
            ordiPersoYN: expect.any(Object),
            derniereModification: expect.any(Object),
            emailUser: expect.any(Object),
            etudiant: expect.any(Object),
            typeHadique: expect.any(Object),
            typeBourse: expect.any(Object),
          }),
        );
      });
    });

    describe('getInformationPersonnelle', () => {
      it('should return NewInformationPersonnelle for default InformationPersonnelle initial value', () => {
        const formGroup = service.createInformationPersonnelleFormGroup(sampleWithNewData);

        const informationPersonnelle = service.getInformationPersonnelle(formGroup) as any;

        expect(informationPersonnelle).toMatchObject(sampleWithNewData);
      });

      it('should return NewInformationPersonnelle for empty InformationPersonnelle initial value', () => {
        const formGroup = service.createInformationPersonnelleFormGroup();

        const informationPersonnelle = service.getInformationPersonnelle(formGroup) as any;

        expect(informationPersonnelle).toMatchObject({});
      });

      it('should return IInformationPersonnelle', () => {
        const formGroup = service.createInformationPersonnelleFormGroup(sampleWithRequiredData);

        const informationPersonnelle = service.getInformationPersonnelle(formGroup) as any;

        expect(informationPersonnelle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInformationPersonnelle should not enable id FormControl', () => {
        const formGroup = service.createInformationPersonnelleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInformationPersonnelle should disable id FormControl', () => {
        const formGroup = service.createInformationPersonnelleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
