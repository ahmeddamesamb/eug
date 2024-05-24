import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from '../service/inscription-administrative-formation.service';
import { InscriptionAdministrativeFormationFormService } from './inscription-administrative-formation-form.service';

import { InscriptionAdministrativeFormationUpdateComponent } from './inscription-administrative-formation-update.component';

describe('InscriptionAdministrativeFormation Management Update Component', () => {
  let comp: InscriptionAdministrativeFormationUpdateComponent;
  let fixture: ComponentFixture<InscriptionAdministrativeFormationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionAdministrativeFormationFormService: InscriptionAdministrativeFormationFormService;
  let inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService;
  let inscriptionAdministrativeService: InscriptionAdministrativeService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InscriptionAdministrativeFormationUpdateComponent],
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
      .overrideTemplate(InscriptionAdministrativeFormationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionAdministrativeFormationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionAdministrativeFormationFormService = TestBed.inject(InscriptionAdministrativeFormationFormService);
    inscriptionAdministrativeFormationService = TestBed.inject(InscriptionAdministrativeFormationService);
    inscriptionAdministrativeService = TestBed.inject(InscriptionAdministrativeService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InscriptionAdministrative query and add missing value', () => {
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 22766 };
      inscriptionAdministrativeFormation.inscriptionAdministrative = inscriptionAdministrative;

      const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [{ id: 15282 }];
      jest
        .spyOn(inscriptionAdministrativeService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: inscriptionAdministrativeCollection })));
      const additionalInscriptionAdministratives = [inscriptionAdministrative];
      const expectedCollection: IInscriptionAdministrative[] = [
        ...additionalInscriptionAdministratives,
        ...inscriptionAdministrativeCollection,
      ];
      jest.spyOn(inscriptionAdministrativeService, 'addInscriptionAdministrativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionAdministrativeFormation });
      comp.ngOnInit();

      expect(inscriptionAdministrativeService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeCollection,
        ...additionalInscriptionAdministratives.map(expect.objectContaining),
      );
      expect(comp.inscriptionAdministrativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Formation query and add missing value', () => {
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 456 };
      const formation: IFormation = { id: 26768 };
      inscriptionAdministrativeFormation.formation = formation;

      const formationCollection: IFormation[] = [{ id: 30841 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionAdministrativeFormation });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining),
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 4352 };
      inscriptionAdministrativeFormation.inscriptionAdministrative = inscriptionAdministrative;
      const formation: IFormation = { id: 30864 };
      inscriptionAdministrativeFormation.formation = formation;

      activatedRoute.data = of({ inscriptionAdministrativeFormation });
      comp.ngOnInit();

      expect(comp.inscriptionAdministrativesSharedCollection).toContain(inscriptionAdministrative);
      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.inscriptionAdministrativeFormation).toEqual(inscriptionAdministrativeFormation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrativeFormation>>();
      const inscriptionAdministrativeFormation = { id: 123 };
      jest
        .spyOn(inscriptionAdministrativeFormationFormService, 'getInscriptionAdministrativeFormation')
        .mockReturnValue(inscriptionAdministrativeFormation);
      jest.spyOn(inscriptionAdministrativeFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrativeFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionAdministrativeFormation }));
      saveSubject.complete();

      // THEN
      expect(inscriptionAdministrativeFormationFormService.getInscriptionAdministrativeFormation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionAdministrativeFormationService.update).toHaveBeenCalledWith(
        expect.objectContaining(inscriptionAdministrativeFormation),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrativeFormation>>();
      const inscriptionAdministrativeFormation = { id: 123 };
      jest.spyOn(inscriptionAdministrativeFormationFormService, 'getInscriptionAdministrativeFormation').mockReturnValue({ id: null });
      jest.spyOn(inscriptionAdministrativeFormationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrativeFormation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionAdministrativeFormation }));
      saveSubject.complete();

      // THEN
      expect(inscriptionAdministrativeFormationFormService.getInscriptionAdministrativeFormation).toHaveBeenCalled();
      expect(inscriptionAdministrativeFormationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrativeFormation>>();
      const inscriptionAdministrativeFormation = { id: 123 };
      jest.spyOn(inscriptionAdministrativeFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrativeFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionAdministrativeFormationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInscriptionAdministrative', () => {
      it('Should forward to inscriptionAdministrativeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionAdministrativeService, 'compareInscriptionAdministrative');
        comp.compareInscriptionAdministrative(entity, entity2);
        expect(inscriptionAdministrativeService.compareInscriptionAdministrative).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFormation', () => {
      it('Should forward to formationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formationService, 'compareFormation');
        comp.compareFormation(entity, entity2);
        expect(formationService.compareFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
