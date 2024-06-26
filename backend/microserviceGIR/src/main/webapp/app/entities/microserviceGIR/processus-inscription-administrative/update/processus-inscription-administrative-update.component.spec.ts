import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInscriptionAdministrative } from 'app/entities/microserviceGIR/inscription-administrative/inscription-administrative.model';
import { InscriptionAdministrativeService } from 'app/entities/microserviceGIR/inscription-administrative/service/inscription-administrative.service';
import { ProcessusInscriptionAdministrativeService } from '../service/processus-inscription-administrative.service';
import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import { ProcessusInscriptionAdministrativeFormService } from './processus-inscription-administrative-form.service';

import { ProcessusInscriptionAdministrativeUpdateComponent } from './processus-inscription-administrative-update.component';

describe('ProcessusInscriptionAdministrative Management Update Component', () => {
  let comp: ProcessusInscriptionAdministrativeUpdateComponent;
  let fixture: ComponentFixture<ProcessusInscriptionAdministrativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let processusInscriptionAdministrativeFormService: ProcessusInscriptionAdministrativeFormService;
  let processusInscriptionAdministrativeService: ProcessusInscriptionAdministrativeService;
  let inscriptionAdministrativeService: InscriptionAdministrativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProcessusInscriptionAdministrativeUpdateComponent],
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
      .overrideTemplate(ProcessusInscriptionAdministrativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessusInscriptionAdministrativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    processusInscriptionAdministrativeFormService = TestBed.inject(ProcessusInscriptionAdministrativeFormService);
    processusInscriptionAdministrativeService = TestBed.inject(ProcessusInscriptionAdministrativeService);
    inscriptionAdministrativeService = TestBed.inject(InscriptionAdministrativeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call inscriptionAdministrative query and add missing value', () => {
      const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 19782 };
      processusInscriptionAdministrative.inscriptionAdministrative = inscriptionAdministrative;

      const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [{ id: 19631 }];
      jest
        .spyOn(inscriptionAdministrativeService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: inscriptionAdministrativeCollection })));
      const expectedCollection: IInscriptionAdministrative[] = [inscriptionAdministrative, ...inscriptionAdministrativeCollection];
      jest.spyOn(inscriptionAdministrativeService, 'addInscriptionAdministrativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ processusInscriptionAdministrative });
      comp.ngOnInit();

      expect(inscriptionAdministrativeService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeService.addInscriptionAdministrativeToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeCollection,
        inscriptionAdministrative,
      );
      expect(comp.inscriptionAdministrativesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = { id: 456 };
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 3384 };
      processusInscriptionAdministrative.inscriptionAdministrative = inscriptionAdministrative;

      activatedRoute.data = of({ processusInscriptionAdministrative });
      comp.ngOnInit();

      expect(comp.inscriptionAdministrativesCollection).toContain(inscriptionAdministrative);
      expect(comp.processusInscriptionAdministrative).toEqual(processusInscriptionAdministrative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusInscriptionAdministrative>>();
      const processusInscriptionAdministrative = { id: 123 };
      jest
        .spyOn(processusInscriptionAdministrativeFormService, 'getProcessusInscriptionAdministrative')
        .mockReturnValue(processusInscriptionAdministrative);
      jest.spyOn(processusInscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusInscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processusInscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(processusInscriptionAdministrativeFormService.getProcessusInscriptionAdministrative).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(processusInscriptionAdministrativeService.update).toHaveBeenCalledWith(
        expect.objectContaining(processusInscriptionAdministrative),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusInscriptionAdministrative>>();
      const processusInscriptionAdministrative = { id: 123 };
      jest.spyOn(processusInscriptionAdministrativeFormService, 'getProcessusInscriptionAdministrative').mockReturnValue({ id: null });
      jest.spyOn(processusInscriptionAdministrativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusInscriptionAdministrative: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processusInscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(processusInscriptionAdministrativeFormService.getProcessusInscriptionAdministrative).toHaveBeenCalled();
      expect(processusInscriptionAdministrativeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcessusInscriptionAdministrative>>();
      const processusInscriptionAdministrative = { id: 123 };
      jest.spyOn(processusInscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processusInscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(processusInscriptionAdministrativeService.update).toHaveBeenCalled();
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
