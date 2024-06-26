import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';
import { OperateurService } from 'app/entities/microserviceGIR/operateur/service/operateur.service';
import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import { PaiementFormationPriveeService } from '../service/paiement-formation-privee.service';
import { PaiementFormationPriveeFormService } from './paiement-formation-privee-form.service';

import { PaiementFormationPriveeUpdateComponent } from './paiement-formation-privee-update.component';

describe('PaiementFormationPrivee Management Update Component', () => {
  let comp: PaiementFormationPriveeUpdateComponent;
  let fixture: ComponentFixture<PaiementFormationPriveeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementFormationPriveeFormService: PaiementFormationPriveeFormService;
  let paiementFormationPriveeService: PaiementFormationPriveeService;
  let inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService;
  let operateurService: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PaiementFormationPriveeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaiementFormationPriveeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementFormationPriveeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementFormationPriveeFormService = TestBed.inject(PaiementFormationPriveeFormService);
    paiementFormationPriveeService = TestBed.inject(PaiementFormationPriveeService);
    inscriptionAdministrativeFormationService = TestBed.inject(InscriptionAdministrativeFormationService);
    operateurService = TestBed.inject(OperateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InscriptionAdministrativeFormation query and add missing value', () => {
      const paiementFormationPrivee: IPaiementFormationPrivee = { id: 456 };
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 2641 };
      paiementFormationPrivee.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;

      const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [{ id: 11930 }];
      jest
        .spyOn(inscriptionAdministrativeFormationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: inscriptionAdministrativeFormationCollection })));
      const additionalInscriptionAdministrativeFormations = [inscriptionAdministrativeFormation];
      const expectedCollection: IInscriptionAdministrativeFormation[] = [
        ...additionalInscriptionAdministrativeFormations,
        ...inscriptionAdministrativeFormationCollection,
      ];
      jest
        .spyOn(inscriptionAdministrativeFormationService, 'addInscriptionAdministrativeFormationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementFormationPrivee });
      comp.ngOnInit();

      expect(inscriptionAdministrativeFormationService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeFormationCollection,
        ...additionalInscriptionAdministrativeFormations.map(expect.objectContaining),
      );
      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Operateur query and add missing value', () => {
      const paiementFormationPrivee: IPaiementFormationPrivee = { id: 456 };
      const operateur: IOperateur = { id: 3279 };
      paiementFormationPrivee.operateur = operateur;

      const operateurCollection: IOperateur[] = [{ id: 32511 }];
      jest.spyOn(operateurService, 'query').mockReturnValue(of(new HttpResponse({ body: operateurCollection })));
      const additionalOperateurs = [operateur];
      const expectedCollection: IOperateur[] = [...additionalOperateurs, ...operateurCollection];
      jest.spyOn(operateurService, 'addOperateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementFormationPrivee });
      comp.ngOnInit();

      expect(operateurService.query).toHaveBeenCalled();
      expect(operateurService.addOperateurToCollectionIfMissing).toHaveBeenCalledWith(
        operateurCollection,
        ...additionalOperateurs.map(expect.objectContaining),
      );
      expect(comp.operateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiementFormationPrivee: IPaiementFormationPrivee = { id: 456 };
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 13339 };
      paiementFormationPrivee.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;
      const operateur: IOperateur = { id: 5108 };
      paiementFormationPrivee.operateur = operateur;

      activatedRoute.data = of({ paiementFormationPrivee });
      comp.ngOnInit();

      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toContain(inscriptionAdministrativeFormation);
      expect(comp.operateursSharedCollection).toContain(operateur);
      expect(comp.paiementFormationPrivee).toEqual(paiementFormationPrivee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFormationPrivee>>();
      const paiementFormationPrivee = { id: 123 };
      jest.spyOn(paiementFormationPriveeFormService, 'getPaiementFormationPrivee').mockReturnValue(paiementFormationPrivee);
      jest.spyOn(paiementFormationPriveeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFormationPrivee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementFormationPrivee }));
      saveSubject.complete();

      // THEN
      expect(paiementFormationPriveeFormService.getPaiementFormationPrivee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementFormationPriveeService.update).toHaveBeenCalledWith(expect.objectContaining(paiementFormationPrivee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFormationPrivee>>();
      const paiementFormationPrivee = { id: 123 };
      jest.spyOn(paiementFormationPriveeFormService, 'getPaiementFormationPrivee').mockReturnValue({ id: null });
      jest.spyOn(paiementFormationPriveeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFormationPrivee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementFormationPrivee }));
      saveSubject.complete();

      // THEN
      expect(paiementFormationPriveeFormService.getPaiementFormationPrivee).toHaveBeenCalled();
      expect(paiementFormationPriveeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFormationPrivee>>();
      const paiementFormationPrivee = { id: 123 };
      jest.spyOn(paiementFormationPriveeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFormationPrivee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementFormationPriveeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInscriptionAdministrativeFormation', () => {
      it('Should forward to inscriptionAdministrativeFormationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionAdministrativeFormationService, 'compareInscriptionAdministrativeFormation');
        comp.compareInscriptionAdministrativeFormation(entity, entity2);
        expect(inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOperateur', () => {
      it('Should forward to operateurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(operateurService, 'compareOperateur');
        comp.compareOperateur(entity, entity2);
        expect(operateurService.compareOperateur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
