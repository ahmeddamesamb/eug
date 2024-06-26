import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUfr } from 'app/entities/microserviceGIR/ufr/ufr.model';
import { UfrService } from 'app/entities/microserviceGIR/ufr/service/ufr.service';
import { DepartementService } from '../service/departement.service';
import { IDepartement } from '../departement.model';
import { DepartementFormService } from './departement-form.service';

import { DepartementUpdateComponent } from './departement-update.component';

describe('Departement Management Update Component', () => {
  let comp: DepartementUpdateComponent;
  let fixture: ComponentFixture<DepartementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departementFormService: DepartementFormService;
  let departementService: DepartementService;
  let ufrService: UfrService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DepartementUpdateComponent],
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
      .overrideTemplate(DepartementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departementFormService = TestBed.inject(DepartementFormService);
    departementService = TestBed.inject(DepartementService);
    ufrService = TestBed.inject(UfrService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ufr query and add missing value', () => {
      const departement: IDepartement = { id: 456 };
      const ufr: IUfr = { id: 20261 };
      departement.ufr = ufr;

      const ufrCollection: IUfr[] = [{ id: 16079 }];
      jest.spyOn(ufrService, 'query').mockReturnValue(of(new HttpResponse({ body: ufrCollection })));
      const additionalUfrs = [ufr];
      const expectedCollection: IUfr[] = [...additionalUfrs, ...ufrCollection];
      jest.spyOn(ufrService, 'addUfrToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      expect(ufrService.query).toHaveBeenCalled();
      expect(ufrService.addUfrToCollectionIfMissing).toHaveBeenCalledWith(ufrCollection, ...additionalUfrs.map(expect.objectContaining));
      expect(comp.ufrsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const departement: IDepartement = { id: 456 };
      const ufr: IUfr = { id: 9011 };
      departement.ufr = ufr;

      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      expect(comp.ufrsSharedCollection).toContain(ufr);
      expect(comp.departement).toEqual(departement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 123 };
      jest.spyOn(departementFormService, 'getDepartement').mockReturnValue(departement);
      jest.spyOn(departementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departement }));
      saveSubject.complete();

      // THEN
      expect(departementFormService.getDepartement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departementService.update).toHaveBeenCalledWith(expect.objectContaining(departement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 123 };
      jest.spyOn(departementFormService, 'getDepartement').mockReturnValue({ id: null });
      jest.spyOn(departementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departement }));
      saveSubject.complete();

      // THEN
      expect(departementFormService.getDepartement).toHaveBeenCalled();
      expect(departementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 123 };
      jest.spyOn(departementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUfr', () => {
      it('Should forward to ufrService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ufrService, 'compareUfr');
        comp.compareUfr(entity, entity2);
        expect(ufrService.compareUfr).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
