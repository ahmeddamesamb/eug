import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMinistere } from 'app/entities/microserviceGIR/ministere/ministere.model';
import { MinistereService } from 'app/entities/microserviceGIR/ministere/service/ministere.service';
import { UniversiteService } from '../service/universite.service';
import { IUniversite } from '../universite.model';
import { UniversiteFormService } from './universite-form.service';

import { UniversiteUpdateComponent } from './universite-update.component';

describe('Universite Management Update Component', () => {
  let comp: UniversiteUpdateComponent;
  let fixture: ComponentFixture<UniversiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let universiteFormService: UniversiteFormService;
  let universiteService: UniversiteService;
  let ministereService: MinistereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UniversiteUpdateComponent],
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
      .overrideTemplate(UniversiteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UniversiteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    universiteFormService = TestBed.inject(UniversiteFormService);
    universiteService = TestBed.inject(UniversiteService);
    ministereService = TestBed.inject(MinistereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ministere query and add missing value', () => {
      const universite: IUniversite = { id: 456 };
      const ministere: IMinistere = { id: 1043 };
      universite.ministere = ministere;

      const ministereCollection: IMinistere[] = [{ id: 14031 }];
      jest.spyOn(ministereService, 'query').mockReturnValue(of(new HttpResponse({ body: ministereCollection })));
      const additionalMinisteres = [ministere];
      const expectedCollection: IMinistere[] = [...additionalMinisteres, ...ministereCollection];
      jest.spyOn(ministereService, 'addMinistereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ universite });
      comp.ngOnInit();

      expect(ministereService.query).toHaveBeenCalled();
      expect(ministereService.addMinistereToCollectionIfMissing).toHaveBeenCalledWith(
        ministereCollection,
        ...additionalMinisteres.map(expect.objectContaining),
      );
      expect(comp.ministeresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const universite: IUniversite = { id: 456 };
      const ministere: IMinistere = { id: 20256 };
      universite.ministere = ministere;

      activatedRoute.data = of({ universite });
      comp.ngOnInit();

      expect(comp.ministeresSharedCollection).toContain(ministere);
      expect(comp.universite).toEqual(universite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniversite>>();
      const universite = { id: 123 };
      jest.spyOn(universiteFormService, 'getUniversite').mockReturnValue(universite);
      jest.spyOn(universiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ universite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: universite }));
      saveSubject.complete();

      // THEN
      expect(universiteFormService.getUniversite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(universiteService.update).toHaveBeenCalledWith(expect.objectContaining(universite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniversite>>();
      const universite = { id: 123 };
      jest.spyOn(universiteFormService, 'getUniversite').mockReturnValue({ id: null });
      jest.spyOn(universiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ universite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: universite }));
      saveSubject.complete();

      // THEN
      expect(universiteFormService.getUniversite).toHaveBeenCalled();
      expect(universiteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUniversite>>();
      const universite = { id: 123 };
      jest.spyOn(universiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ universite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(universiteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMinistere', () => {
      it('Should forward to ministereService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ministereService, 'compareMinistere');
        comp.compareMinistere(entity, entity2);
        expect(ministereService.compareMinistere).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
