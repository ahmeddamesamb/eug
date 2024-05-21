import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISpecialite } from 'app/entities/microserviceGIR/specialite/specialite.model';
import { SpecialiteService } from 'app/entities/microserviceGIR/specialite/service/specialite.service';
import { INiveau } from 'app/entities/microserviceGIR/niveau/niveau.model';
import { NiveauService } from 'app/entities/microserviceGIR/niveau/service/niveau.service';
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
  let specialiteService: SpecialiteService;
  let niveauService: NiveauService;

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
    specialiteService = TestBed.inject(SpecialiteService);
    niveauService = TestBed.inject(NiveauService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Specialite query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const specialite: ISpecialite = { id: 13200 };
      formation.specialite = specialite;

      const specialiteCollection: ISpecialite[] = [{ id: 12069 }];
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

    it('Should call Niveau query and add missing value', () => {
      const formation: IFormation = { id: 456 };
      const niveau: INiveau = { id: 17803 };
      formation.niveau = niveau;

      const niveauCollection: INiveau[] = [{ id: 3655 }];
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

    it('Should update editForm', () => {
      const formation: IFormation = { id: 456 };
      const specialite: ISpecialite = { id: 20960 };
      formation.specialite = specialite;
      const niveau: INiveau = { id: 25138 };
      formation.niveau = niveau;

      activatedRoute.data = of({ formation });
      comp.ngOnInit();

      expect(comp.specialitesSharedCollection).toContain(specialite);
      expect(comp.niveausSharedCollection).toContain(niveau);
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
    describe('compareSpecialite', () => {
      it('Should forward to specialiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(specialiteService, 'compareSpecialite');
        comp.compareSpecialite(entity, entity2);
        expect(specialiteService.compareSpecialite).toHaveBeenCalledWith(entity, entity2);
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
  });
});
