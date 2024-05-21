import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { RegionService } from 'app/entities/microserviceGIR/region/service/region.service';
import { LyceeService } from '../service/lycee.service';
import { ILycee } from '../lycee.model';
import { LyceeFormService } from './lycee-form.service';

import { LyceeUpdateComponent } from './lycee-update.component';

describe('Lycee Management Update Component', () => {
  let comp: LyceeUpdateComponent;
  let fixture: ComponentFixture<LyceeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lyceeFormService: LyceeFormService;
  let lyceeService: LyceeService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LyceeUpdateComponent],
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
      .overrideTemplate(LyceeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LyceeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lyceeFormService = TestBed.inject(LyceeFormService);
    lyceeService = TestBed.inject(LyceeService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const lycee: ILycee = { id: 456 };
      const region: IRegion = { id: 10665 };
      lycee.region = region;

      const regionCollection: IRegion[] = [{ id: 9571 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lycee });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining),
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lycee: ILycee = { id: 456 };
      const region: IRegion = { id: 22432 };
      lycee.region = region;

      activatedRoute.data = of({ lycee });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.lycee).toEqual(lycee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILycee>>();
      const lycee = { id: 123 };
      jest.spyOn(lyceeFormService, 'getLycee').mockReturnValue(lycee);
      jest.spyOn(lyceeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lycee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lycee }));
      saveSubject.complete();

      // THEN
      expect(lyceeFormService.getLycee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(lyceeService.update).toHaveBeenCalledWith(expect.objectContaining(lycee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILycee>>();
      const lycee = { id: 123 };
      jest.spyOn(lyceeFormService, 'getLycee').mockReturnValue({ id: null });
      jest.spyOn(lyceeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lycee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lycee }));
      saveSubject.complete();

      // THEN
      expect(lyceeFormService.getLycee).toHaveBeenCalled();
      expect(lyceeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILycee>>();
      const lycee = { id: 123 };
      jest.spyOn(lyceeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lycee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lyceeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegion', () => {
      it('Should forward to regionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
