import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { TypeFraisService } from 'app/entities/microserviceGIR/type-frais/service/type-frais.service';
import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { CycleService } from 'app/entities/microserviceGIR/cycle/service/cycle.service';
import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { IFrais } from '../frais.model';
import { FraisService } from '../service/frais.service';
import { FraisFormService } from './frais-form.service';

import { FraisUpdateComponent } from './frais-update.component';

describe('Frais Management Update Component', () => {
  let comp: FraisUpdateComponent;
  let fixture: ComponentFixture<FraisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fraisFormService: FraisFormService;
  let fraisService: FraisService;
  let typeFraisService: TypeFraisService;
  let cycleService: CycleService;
  let universiteService: UniversiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FraisUpdateComponent],
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
      .overrideTemplate(FraisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FraisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fraisFormService = TestBed.inject(FraisFormService);
    fraisService = TestBed.inject(FraisService);
    typeFraisService = TestBed.inject(TypeFraisService);
    cycleService = TestBed.inject(CycleService);
    universiteService = TestBed.inject(UniversiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeFrais query and add missing value', () => {
      const frais: IFrais = { id: 456 };
      const typeFrais: ITypeFrais = { id: 28979 };
      frais.typeFrais = typeFrais;

      const typeFraisCollection: ITypeFrais[] = [{ id: 29085 }];
      jest.spyOn(typeFraisService, 'query').mockReturnValue(of(new HttpResponse({ body: typeFraisCollection })));
      const additionalTypeFrais = [typeFrais];
      const expectedCollection: ITypeFrais[] = [...additionalTypeFrais, ...typeFraisCollection];
      jest.spyOn(typeFraisService, 'addTypeFraisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(typeFraisService.query).toHaveBeenCalled();
      expect(typeFraisService.addTypeFraisToCollectionIfMissing).toHaveBeenCalledWith(
        typeFraisCollection,
        ...additionalTypeFrais.map(expect.objectContaining),
      );
      expect(comp.typeFraisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cycle query and add missing value', () => {
      const frais: IFrais = { id: 456 };
      const typeCycle: ICycle = { id: 13304 };
      frais.typeCycle = typeCycle;

      const cycleCollection: ICycle[] = [{ id: 14123 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [typeCycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Universite query and add missing value', () => {
      const frais: IFrais = { id: 456 };
      const universites: IUniversite[] = [{ id: 11692 }];
      frais.universites = universites;

      const universiteCollection: IUniversite[] = [{ id: 13829 }];
      jest.spyOn(universiteService, 'query').mockReturnValue(of(new HttpResponse({ body: universiteCollection })));
      const additionalUniversites = [...universites];
      const expectedCollection: IUniversite[] = [...additionalUniversites, ...universiteCollection];
      jest.spyOn(universiteService, 'addUniversiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(universiteService.query).toHaveBeenCalled();
      expect(universiteService.addUniversiteToCollectionIfMissing).toHaveBeenCalledWith(
        universiteCollection,
        ...additionalUniversites.map(expect.objectContaining),
      );
      expect(comp.universitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const frais: IFrais = { id: 456 };
      const typeFrais: ITypeFrais = { id: 14399 };
      frais.typeFrais = typeFrais;
      const typeCycle: ICycle = { id: 2230 };
      frais.typeCycle = typeCycle;
      const universite: IUniversite = { id: 25221 };
      frais.universites = [universite];

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(comp.typeFraisSharedCollection).toContain(typeFrais);
      expect(comp.cyclesSharedCollection).toContain(typeCycle);
      expect(comp.universitesSharedCollection).toContain(universite);
      expect(comp.frais).toEqual(frais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisFormService, 'getFrais').mockReturnValue(frais);
      jest.spyOn(fraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frais }));
      saveSubject.complete();

      // THEN
      expect(fraisFormService.getFrais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fraisService.update).toHaveBeenCalledWith(expect.objectContaining(frais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisFormService, 'getFrais').mockReturnValue({ id: null });
      jest.spyOn(fraisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frais }));
      saveSubject.complete();

      // THEN
      expect(fraisFormService.getFrais).toHaveBeenCalled();
      expect(fraisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fraisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeFrais', () => {
      it('Should forward to typeFraisService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeFraisService, 'compareTypeFrais');
        comp.compareTypeFrais(entity, entity2);
        expect(typeFraisService.compareTypeFrais).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCycle', () => {
      it('Should forward to cycleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUniversite', () => {
      it('Should forward to universiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(universiteService, 'compareUniversite');
        comp.compareUniversite(entity, entity2);
        expect(universiteService.compareUniversite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
