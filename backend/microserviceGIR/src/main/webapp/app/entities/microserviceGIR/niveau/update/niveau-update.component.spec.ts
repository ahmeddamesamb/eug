import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICycle } from 'app/entities/microserviceGIR/cycle/cycle.model';
import { CycleService } from 'app/entities/microserviceGIR/cycle/service/cycle.service';
import { NiveauService } from '../service/niveau.service';
import { INiveau } from '../niveau.model';
import { NiveauFormService } from './niveau-form.service';

import { NiveauUpdateComponent } from './niveau-update.component';

describe('Niveau Management Update Component', () => {
  let comp: NiveauUpdateComponent;
  let fixture: ComponentFixture<NiveauUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let niveauFormService: NiveauFormService;
  let niveauService: NiveauService;
  let cycleService: CycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), NiveauUpdateComponent],
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
      .overrideTemplate(NiveauUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NiveauUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    niveauFormService = TestBed.inject(NiveauFormService);
    niveauService = TestBed.inject(NiveauService);
    cycleService = TestBed.inject(CycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cycle query and add missing value', () => {
      const niveau: INiveau = { id: 456 };
      const typeCycle: ICycle = { id: 7146 };
      niveau.typeCycle = typeCycle;

      const cycleCollection: ICycle[] = [{ id: 30215 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [typeCycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const niveau: INiveau = { id: 456 };
      const typeCycle: ICycle = { id: 17925 };
      niveau.typeCycle = typeCycle;

      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      expect(comp.cyclesSharedCollection).toContain(typeCycle);
      expect(comp.niveau).toEqual(niveau);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauFormService, 'getNiveau').mockReturnValue(niveau);
      jest.spyOn(niveauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveau }));
      saveSubject.complete();

      // THEN
      expect(niveauFormService.getNiveau).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(niveauService.update).toHaveBeenCalledWith(expect.objectContaining(niveau));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauFormService, 'getNiveau').mockReturnValue({ id: null });
      jest.spyOn(niveauService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveau }));
      saveSubject.complete();

      // THEN
      expect(niveauFormService.getNiveau).toHaveBeenCalled();
      expect(niveauService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INiveau>>();
      const niveau = { id: 123 };
      jest.spyOn(niveauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(niveauService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCycle', () => {
      it('Should forward to cycleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
