import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeFormation } from 'app/entities/microserviceGIR/type-formation/type-formation.model';
import { TypeFormationService } from 'app/entities/microserviceGIR/type-formation/service/type-formation.service';
import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { NiveauService } from 'app/entities/microserviceGIR/niveau/service/niveau.service';
import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { SpecialiteService } from 'app/entities/microserviceGIR/specialite/service/specialite.service';
import { IDepartement } from 'app/entities/microserviceGIR/departement/departement.model';
import { DepartementService } from 'app/entities/microserviceGIR/departement/service/departement.service';
import { IFormation } from '../formation.model';
import { FormationService } from '../service/formation.service';
import { FormationFormService } from './formation-form.service';

import { FormationUpdateComponent } from './formation-update.component';

describe('Formation Management Update Component', () => {
  let comp: FormationUpdateComponent;
  let fixture: ComponentFixture<FormationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationFormService: FormationFormService;
  let formationService: FormationService;
  let typeFormationService: TypeFormationService;
  let niveauService: NiveauService;
  let specialiteService: SpecialiteService;
  let departementService: DepartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormationUpdateComponent],
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
      .overrideTemplate(FormationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationFormService = TestBed.inject(FormationFormService);
    formationService = TestBed.inject(FormationService);
    typeFormationService = TestBed.inject(TypeFormationService);
    niveauService = TestBed.inject(NiveauService);
    specialiteService = TestBed.inject(SpecialiteService);
    departementService = TestBed.inject(DepartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeFormation query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const typeFormation: ITypeFormation = { id: 7037 };
      formation.typeFormation = typeFormation;

      const typeFormationCollection: ITypeFormation[] = [{ id: 10597 }];
      jest.spyOn(typeFormationService, 'query').mockReturnValue(of(new HttpResponse({ body: typeFormationCollection })));
      const additionalTypeFormations = [typeFormation];
      const expectedCollection: ITypeFormation[] = [...additionalTypeFormations, ...typeFormationCollection];
      jest.spyOn(typeFormationService, 'addTypeFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(typeFormationService.query).toHaveBeenCalled();
      expect(typeFormationService.addTypeFormationToCollectionIfMissing).toHaveBeenCalledWith(
        typeFormationCollection,
        ...additionalTypeFormations.map(expect.objectContaining),
      );
      expect(comp.typeFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Niveau query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const niveau: INiveau = { id: 11608 };
      formation.niveau = niveau;

      const niveauCollection: INiveau[] = [{ id: 22510 }];
      jest.spyOn(niveauService, 'query').mockReturnValue(of(new HttpResponse({ body: niveauCollection })));
      const additionalNiveaus = [niveau];
      const expectedCollection: INiveau[] = [...additionalNiveaus, ...niveauCollection];
      jest.spyOn(niveauService, 'addNiveauToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(niveauService.query).toHaveBeenCalled();
      expect(niveauService.addNiveauToCollectionIfMissing).toHaveBeenCalledWith(
        niveauCollection,
        ...additionalNiveaus.map(expect.objectContaining),
      );
      expect(comp.niveausSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Specialite query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const specialite: ISpecialite = { id: 24105 };
      formation.specialite = specialite;

      const specialiteCollection: ISpecialite[] = [{ id: 6966 }];
      jest.spyOn(specialiteService, 'query').mockReturnValue(of(new HttpResponse({ body: specialiteCollection })));
      const additionalSpecialites = [specialite];
      const expectedCollection: ISpecialite[] = [...additionalSpecialites, ...specialiteCollection];
      jest.spyOn(specialiteService, 'addSpecialiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(specialiteService.query).toHaveBeenCalled();
      expect(specialiteService.addSpecialiteToCollectionIfMissing).toHaveBeenCalledWith(
        specialiteCollection,
        ...additionalSpecialites.map(expect.objectContaining),
      );
      expect(comp.specialitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departement query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const departement: IDepartement = { id: 1231 };
      formation.departement = departement;

      const departementCollection: IDepartement[] = [{ id: 7542 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
        departementCollection,
        ...additionalDepartements.map(expect.objectContaining),
      );
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formation: IFormation = { id: 456 };
      const typeFormation: ITypeFormation = { id: 22436 };
      formation.typeFormation = typeFormation;
      const niveau: INiveau = { id: 1293 };
      formation.niveau = niveau;
      const specialite: ISpecialite = { id: 4769 };
      formation.specialite = specialite;
      const departement: IDepartement = { id: 26111 };
      formation.departement = departement;

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(comp.typeFormationsSharedCollection).toContain(typeFormation);
      expect(comp.niveausSharedCollection).toContain(niveau);
      expect(comp.specialitesSharedCollection).toContain(specialite);
      expect(comp.departementsSharedCollection).toContain(departement);
      expect(comp.formation).toEqual(formation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormation>>();
      const formation = { id: 123 };
      jest.spyOn(formationFormService, 'getFormation').mockReturnValue(formation);
      jest.spyOn(formationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formation }));
      saveSubject.complete();

      // THEN
      expect(formationFormService.getFormation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationService.update).toHaveBeenCalledWith(expect.objectContaining(formation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormation>>();
      const formation = { id: 123 };
      jest.spyOn(formationFormService, 'getFormation').mockReturnValue({ id: null });
      jest.spyOn(formationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formation }));
      saveSubject.complete();

      // THEN
      expect(formationFormService.getFormation).toHaveBeenCalled();
      expect(formationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormation>>();
      const formation = { id: 123 };
      jest.spyOn(formationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeFormation', () => {
      it('Should forward to typeFormationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeFormationService, 'compareTypeFormation');
        comp.compareTypeFormation(entity, entity2);
        expect(typeFormationService.compareTypeFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareNiveau', () => {
      it('Should forward to niveauService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(niveauService, 'compareNiveau');
        comp.compareNiveau(entity, entity2);
        expect(niveauService.compareNiveau).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSpecialite', () => {
      it('Should forward to specialiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(specialiteService, 'compareSpecialite');
        comp.compareSpecialite(entity, entity2);
        expect(specialiteService.compareSpecialite).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartement', () => {
      it('Should forward to departementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departementService, 'compareDepartement');
        comp.compareDepartement(entity, entity2);
        expect(departementService.compareDepartement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
