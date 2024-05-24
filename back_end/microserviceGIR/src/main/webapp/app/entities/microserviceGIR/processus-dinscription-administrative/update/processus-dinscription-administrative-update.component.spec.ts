import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { ProcessusDinscriptionAdministrativeService } from '../service/processus-dinscription-administrative.service';
import { IProcessusDinscriptionAdministrative } from '../processus-dinscription-administrative.model';
import { ProcessusDinscriptionAdministrativeFormService } from './processus-dinscription-administrative-form.service';

import { ProcessusDinscriptionAdministrativeUpdateComponent } from './processus-dinscription-administrative-update.component';

describe('ProcessusDinscriptionAdministrative Management Update Component', () => {
  let comp: ProcessusDinscriptionAdministrativeUpdateComponent;
  let fixture: ComponentFixture<ProcessusDinscriptionAdministrativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let processusDinscriptionAdministrativeFormService: ProcessusDinscriptionAdministrativeFormService;
  let processusDinscriptionAdministrativeService: ProcessusDinscriptionAdministrativeService;
  let inscriptionAdministrativeService: InscriptionAdministrativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProcessusDinscriptionAdministrativeUpdateComponent],
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
      .overrideTemplate(ProcessusDinscriptionAdministrativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessusDinscriptionAdministrativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    processusDinscriptionAdministrativeFormService = TestBed.inject(ProcessusDinscriptionAdministrativeFormService);
    processusDinscriptionAdministrativeService = TestBed.inject(ProcessusDinscriptionAdministrativeService);
    inscriptionAdministrativeService = TestBed.inject(InscriptionAdministrativeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call inscriptionAdministrative query and add missing value', () => {
      const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 19782 };
      processusDinscriptionAdministrative.inscriptionAdministrative = inscriptionAdministrative;

      const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [{ id: 19631 }];
      jest
        .spyOn(inscriptionAdministrativeService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: inscriptionAdministrativeCollection })));
      const expectedCollection: IInscriptionAdministrative[] = [inscriptionAdministrative, ...inscriptionAdministrativeCollection];
      jest.spyOn(inscriptionAdministrativeService, 'addInscriptionAdministrativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ processusDinscriptionAdministrative });
      comp.ngOnInit();

      expect(inscriptionAdministrativeService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeCollection,
        inscriptionAdministrative,
      );
      expect(comp.inscriptionAdministrativesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 3384 };
      processusDinscriptionAdministrative.inscriptionAdministrative = inscriptionAdministrative;

      activatedRoute.data = of({ processusDinscriptionAdministrative });
      comp.ngOnInit();

      expect(comp.inscriptionAdministrativesCollection).toContain(inscriptionAdministrative);
      expect(comp.processusDinscriptionAdministrative).toEqual(processusDinscriptionAdministrative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusDinscriptionAdministrative>>();
      const processusDinscriptionAdministrative = { id: 123 };
      jest
        .spyOn(processusDinscriptionAdministrativeFormService, 'getProcessusDinscriptionAdministrative')
        .mockReturnValue(processusDinscriptionAdministrative);
      jest.spyOn(processusDinscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusDinscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processusDinscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(processusDinscriptionAdministrativeFormService.getProcessusDinscriptionAdministrative).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(processusDinscriptionAdministrativeService.update).toHaveBeenCalledWith(
        expect.objectContaining(processusDinscriptionAdministrative),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusDinscriptionAdministrative>>();
      const processusDinscriptionAdministrative = { id: 123 };
      jest.spyOn(processusDinscriptionAdministrativeFormService, 'getProcessusDinscriptionAdministrative').mockReturnValue({ id: null });
      jest.spyOn(processusDinscriptionAdministrativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusDinscriptionAdministrative: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processusDinscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(processusDinscriptionAdministrativeFormService.getProcessusDinscriptionAdministrative).toHaveBeenCalled();
      expect(processusDinscriptionAdministrativeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusDinscriptionAdministrative>>();
      const processusDinscriptionAdministrative = { id: 123 };
      jest.spyOn(processusDinscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusDinscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(processusDinscriptionAdministrativeService.update).toHaveBeenCalled();
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
  });
});
