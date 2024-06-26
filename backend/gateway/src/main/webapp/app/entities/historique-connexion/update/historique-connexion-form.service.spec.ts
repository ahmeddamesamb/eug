import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../historique-connexion.test-samples';

import { HistoriqueConnexionFormService } from './historique-connexion-form.service';

describe('HistoriqueConnexion Form Service', () => {
  let service: HistoriqueConnexionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoriqueConnexionFormService);
  });

  describe('Service methods', () => {
    describe('createHistoriqueConnexionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebutConnexion: expect.any(Object),
            dateFinConnexion: expect.any(Object),
            adresseIp: expect.any(Object),
            actifYN: expect.any(Object),
            infoUser: expect.any(Object),
          }),
        );
      });

      it('passing IHistoriqueConnexion should create a new form with FormGroup', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebutConnexion: expect.any(Object),
            dateFinConnexion: expect.any(Object),
            adresseIp: expect.any(Object),
            actifYN: expect.any(Object),
            infoUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getHistoriqueConnexion', () => {
      it('should return NewHistoriqueConnexion for default HistoriqueConnexion initial value', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup(sampleWithNewData);

        const historiqueConnexion = service.getHistoriqueConnexion(formGroup) as any;

        expect(historiqueConnexion).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoriqueConnexion for empty HistoriqueConnexion initial value', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup();

        const historiqueConnexion = service.getHistoriqueConnexion(formGroup) as any;

        expect(historiqueConnexion).toMatchObject({});
      });

      it('should return IHistoriqueConnexion', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup(sampleWithRequiredData);

        const historiqueConnexion = service.getHistoriqueConnexion(formGroup) as any;

        expect(historiqueConnexion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoriqueConnexion should not enable id FormControl', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoriqueConnexion should disable id FormControl', () => {
        const formGroup = service.createHistoriqueConnexionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
