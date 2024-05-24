import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { FormationPriveeService } from '../service/formation-privee.service';
import { IFormationPrivee } from '../formation-privee.model';
import { FormationPriveeFormService } from './formation-privee-form.service';

import { FormationPriveeUpdateComponent } from './formation-privee-update.component';

describe('FormationPrivee Management Update Component', () => {
  let comp: FormationPriveeUpdateComponent;
  let fixture: ComponentFixture<FormationPriveeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationPriveeFormService: FormationPriveeFormService;
  let formationPriveeService: FormationPriveeService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormationPriveeUpdateComponent],
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
      .overrideTemplate(FormationPriveeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationPriveeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationPriveeFormService = TestBed.inject(FormationPriveeFormService);
    formationPriveeService = TestBed.inject(FormationPriveeService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call formation query and add missing value', () => {
      const formationPrivee: IFormationPrivee = { id: 456 };
      const formation: IFormation = { id: 2039 };
      formationPrivee.formation = formation;

      const formationCollection: IFormation[] = [{ id: 27460 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const expectedCollection: IFormation[] = [formation, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationPrivee });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(formationCollection, formation);
      expect(comp.formationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationPrivee: IFormationPrivee = { id: 456 };
      const formation: IFormation = { id: 8343 };
      formationPrivee.formation = formation;

      activatedRoute.data = of({ formationPrivee });
      comp.ngOnInit();

      expect(comp.formationsCollection).toContain(formation);
      expect(comp.formationPrivee).toEqual(formationPrivee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationPrivee>>();
      const formationPrivee = { id: 123 };
      jest.spyOn(formationPriveeFormService, 'getFormationPrivee').mockReturnValue(formationPrivee);
      jest.spyOn(formationPriveeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationPrivee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationPrivee }));
      saveSubject.complete();

      // THEN
      expect(formationPriveeFormService.getFormationPrivee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationPriveeService.update).toHaveBeenCalledWith(expect.objectContaining(formationPrivee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationPrivee>>();
      const formationPrivee = { id: 123 };
      jest.spyOn(formationPriveeFormService, 'getFormationPrivee').mockReturnValue({ id: null });
      jest.spyOn(formationPriveeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationPrivee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationPrivee }));
      saveSubject.complete();

      // THEN
      expect(formationPriveeFormService.getFormationPrivee).toHaveBeenCalled();
      expect(formationPriveeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationPrivee>>();
      const formationPrivee = { id: 123 };
      jest.spyOn(formationPriveeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationPrivee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationPriveeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFormation', () => {
      it('Should forward to formationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formationService, 'compareFormation');
        comp.compareFormation(entity, entity2);
        expect(formationService.compareFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
