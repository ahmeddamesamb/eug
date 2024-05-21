import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { UFRService } from '../service/ufr.service';
import { IUFR } from '../ufr.model';
import { UFRFormService } from './ufr-form.service';

import { UFRUpdateComponent } from './ufr-update.component';

describe('UFR Management Update Component', () => {
  let comp: UFRUpdateComponent;
  let fixture: ComponentFixture<UFRUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let uFRFormService: UFRFormService;
  let uFRService: UFRService;
  let universiteService: UniversiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UFRUpdateComponent],
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
      .overrideTemplate(UFRUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UFRUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uFRFormService = TestBed.inject(UFRFormService);
    uFRService = TestBed.inject(UFRService);
    universiteService = TestBed.inject(UniversiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Universite query and add missing value', () => {
      const uFR: IUFR = { id: 456 };
      const universite: IUniversite = { id: 4114 };
      uFR.universite = universite;

      const universiteCollection: IUniversite[] = [{ id: 32724 }];
      jest.spyOn(universiteService, 'query').mockReturnValue(of(new HttpResponse({ body: universiteCollection })));
      const additionalUniversites = [universite];
      const expectedCollection: IUniversite[] = [...additionalUniversites, ...universiteCollection];
      jest.spyOn(universiteService, 'addUniversiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uFR });
      comp.ngOnInit();

      expect(universiteService.query).toHaveBeenCalled();
      expect(universiteService.addUniversiteToCollectionIfMissing).toHaveBeenCalledWith(
        universiteCollection,
        ...additionalUniversites.map(expect.objectContaining),
      );
      expect(comp.universitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const uFR: IUFR = { id: 456 };
      const universite: IUniversite = { id: 18498 };
      uFR.universite = universite;

      activatedRoute.data = of({ uFR });
      comp.ngOnInit();

      expect(comp.universitesSharedCollection).toContain(universite);
      expect(comp.uFR).toEqual(uFR);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUFR>>();
      const uFR = { id: 123 };
      jest.spyOn(uFRFormService, 'getUFR').mockReturnValue(uFR);
      jest.spyOn(uFRService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uFR });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uFR }));
      saveSubject.complete();

      // THEN
      expect(uFRFormService.getUFR).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(uFRService.update).toHaveBeenCalledWith(expect.objectContaining(uFR));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUFR>>();
      const uFR = { id: 123 };
      jest.spyOn(uFRFormService, 'getUFR').mockReturnValue({ id: null });
      jest.spyOn(uFRService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uFR: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uFR }));
      saveSubject.complete();

      // THEN
      expect(uFRFormService.getUFR).toHaveBeenCalled();
      expect(uFRService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUFR>>();
      const uFR = { id: 123 };
      jest.spyOn(uFRService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uFR });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uFRService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
