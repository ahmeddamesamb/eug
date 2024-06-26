import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFrais } from 'app/entities/microserviceGIR/frais/frais.model';
import { FraisService } from 'app/entities/microserviceGIR/frais/service/frais.service';
import { IOperateur } from 'app/entities/microserviceGIR/operateur/operateur.model';
import { OperateurService } from 'app/entities/microserviceGIR/operateur/service/operateur.service';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { IPaiementFrais } from '../paiement-frais.model';
import { PaiementFraisService } from '../service/paiement-frais.service';
import { PaiementFraisFormService } from './paiement-frais-form.service';

import { PaiementFraisUpdateComponent } from './paiement-frais-update.component';

describe('PaiementFrais Management Update Component', () => {
  let comp: PaiementFraisUpdateComponent;
  let fixture: ComponentFixture<PaiementFraisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paiementFraisFormService: PaiementFraisFormService;
  let paiementFraisService: PaiementFraisService;
  let fraisService: FraisService;
  let operateurService: OperateurService;
  let inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PaiementFraisUpdateComponent],
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
      .overrideTemplate(PaiementFraisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementFraisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paiementFraisFormService = TestBed.inject(PaiementFraisFormService);
    paiementFraisService = TestBed.inject(PaiementFraisService);
    fraisService = TestBed.inject(FraisService);
    operateurService = TestBed.inject(OperateurService);
    inscriptionAdministrativeFormationService = TestBed.inject(InscriptionAdministrativeFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Frais query and add missing value', () => {
      const paiementFrais: IPaiementFrais = { id: 456 };
      const frais: IFrais = { id: 6690 };
      paiementFrais.frais = frais;

      const fraisCollection: IFrais[] = [{ id: 21684 }];
      jest.spyOn(fraisService, 'query').mockReturnValue(of(new HttpResponse({ body: fraisCollection })));
      const additionalFrais = [frais];
      const expectedCollection: IFrais[] = [...additionalFrais, ...fraisCollection];
      jest.spyOn(fraisService, 'addFraisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      expect(fraisService.query).toHaveBeenCalled();
      expect(fraisService.addFraisToCollectionIfMissing).toHaveBeenCalledWith(
        fraisCollection,
        ...additionalFrais.map(expect.objectContaining),
      );
      expect(comp.fraisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Operateur query and add missing value', () => {
      const paiementFrais: IPaiementFrais = { id: 456 };
      const operateur: IOperateur = { id: 4803 };
      paiementFrais.operateur = operateur;

      const operateurCollection: IOperateur[] = [{ id: 11757 }];
      jest.spyOn(operateurService, 'query').mockReturnValue(of(new HttpResponse({ body: operateurCollection })));
      const additionalOperateurs = [operateur];
      const expectedCollection: IOperateur[] = [...additionalOperateurs, ...operateurCollection];
      jest.spyOn(operateurService, 'addOperateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      expect(operateurService.query).toHaveBeenCalled();
      expect(operateurService.addOperateurToCollectionIfMissing).toHaveBeenCalledWith(
        operateurCollection,
        ...additionalOperateurs.map(expect.objectContaining),
      );
      expect(comp.operateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InscriptionAdministrativeFormation query and add missing value', () => {
      const paiementFrais: IPaiementFrais = { id: 456 };
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 27242 };
      paiementFrais.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;

      const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [{ id: 29016 }];
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

      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      expect(inscriptionAdministrativeFormationService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeFormationCollection,
        ...additionalInscriptionAdministrativeFormations.map(expect.objectContaining),
      );
      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paiementFrais: IPaiementFrais = { id: 456 };
      const frais: IFrais = { id: 13340 };
      paiementFrais.frais = frais;
      const operateur: IOperateur = { id: 31995 };
      paiementFrais.operateur = operateur;
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 26170 };
      paiementFrais.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;

      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      expect(comp.fraisSharedCollection).toContain(frais);
      expect(comp.operateursSharedCollection).toContain(operateur);
      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toContain(inscriptionAdministrativeFormation);
      expect(comp.paiementFrais).toEqual(paiementFrais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFrais>>();
      const paiementFrais = { id: 123 };
      jest.spyOn(paiementFraisFormService, 'getPaiementFrais').mockReturnValue(paiementFrais);
      jest.spyOn(paiementFraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementFrais }));
      saveSubject.complete();

      // THEN
      expect(paiementFraisFormService.getPaiementFrais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paiementFraisService.update).toHaveBeenCalledWith(expect.objectContaining(paiementFrais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFrais>>();
      const paiementFrais = { id: 123 };
      jest.spyOn(paiementFraisFormService, 'getPaiementFrais').mockReturnValue({ id: null });
      jest.spyOn(paiementFraisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFrais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paiementFrais }));
      saveSubject.complete();

      // THEN
      expect(paiementFraisFormService.getPaiementFrais).toHaveBeenCalled();
      expect(paiementFraisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaiementFrais>>();
      const paiementFrais = { id: 123 };
      jest.spyOn(paiementFraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paiementFrais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paiementFraisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFrais', () => {
      it('Should forward to fraisService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fraisService, 'compareFrais');
        comp.compareFrais(entity, entity2);
        expect(fraisService.compareFrais).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareInscriptionAdministrativeFormation', () => {
      it('Should forward to inscriptionAdministrativeFormationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionAdministrativeFormationService, 'compareInscriptionAdministrativeFormation');
        comp.compareInscriptionAdministrativeFormation(entity, entity2);
        expect(inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
