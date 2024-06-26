import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUniversite } from 'app/entities/microserviceGIR/universite/universite.model';
import { UniversiteService } from 'app/entities/microserviceGIR/universite/service/universite.service';
import { UfrService } from '../service/ufr.service';
import { IUfr } from '../ufr.model';
import { UfrFormService } from './ufr-form.service';

import { UfrUpdateComponent } from './ufr-update.component';

describe('Ufr Management Update Component', () => {
  let comp: UfrUpdateComponent;
  let fixture: ComponentFixture<UfrUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ufrFormService: UfrFormService;
  let ufrService: UfrService;
  let universiteService: UniversiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UfrUpdateComponent],
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
      .overrideTemplate(UfrUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UfrUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ufrFormService = TestBed.inject(UfrFormService);
    ufrService = TestBed.inject(UfrService);
    universiteService = TestBed.inject(UniversiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Universite query and add missing value', () => {
      const ufr: IUfr = { id: 456 };
      const universite: IUniversite = { id: 32724 };
      ufr.universite = universite;

      const universiteCollection: IUniversite[] = [{ id: 18498 }];
      jest.spyOn(universiteService, 'query').mockReturnValue(of(new HttpResponse({ body: universiteCollection })));
      const additionalUniversites = [universite];
      const expectedCollection: IUniversite[] = [...additionalUniversites, ...universiteCollection];
      jest.spyOn(universiteService, 'addUniversiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ufr });
      comp.ngOnInit();

      expect(universiteService.query).toHaveBeenCalled();
      expect(universiteService.addUniversiteToCollectionIfMissing).toHaveBeenCalledWith(
        universiteCollection,
        ...additionalUniversites.map(expect.objectContaining),
      );
      expect(comp.universitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ufr: IUfr = { id: 456 };
      const universite: IUniversite = { id: 9926 };
      ufr.universite = universite;

      activatedRoute.data = of({ ufr });
      comp.ngOnInit();

      expect(comp.universitesSharedCollection).toContain(universite);
      expect(comp.ufr).toEqual(ufr);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUfr>>();
      const ufr = { id: 123 };
      jest.spyOn(ufrFormService, 'getUfr').mockReturnValue(ufr);
      jest.spyOn(ufrService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ufr });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ufr }));
      saveSubject.complete();

      // THEN
      expect(ufrFormService.getUfr).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ufrService.update).toHaveBeenCalledWith(expect.objectContaining(ufr));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUfr>>();
      const ufr = { id: 123 };
      jest.spyOn(ufrFormService, 'getUfr').mockReturnValue({ id: null });
      jest.spyOn(ufrService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ufr: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ufr }));
      saveSubject.complete();

      // THEN
      expect(ufrFormService.getUfr).toHaveBeenCalled();
      expect(ufrService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUfr>>();
      const ufr = { id: 123 };
      jest.spyOn(ufrService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ufr });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ufrService.update).toHaveBeenCalled();
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
