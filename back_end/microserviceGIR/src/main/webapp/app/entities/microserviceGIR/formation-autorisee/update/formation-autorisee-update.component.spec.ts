import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { FormationAutoriseeService } from '../service/formation-autorisee.service';
import { IFormationAutorisee } from '../formation-autorisee.model';
import { FormationAutoriseeFormService } from './formation-autorisee-form.service';

import { FormationAutoriseeUpdateComponent } from './formation-autorisee-update.component';

describe('FormationAutorisee Management Update Component', () => {
  let comp: FormationAutoriseeUpdateComponent;
  let fixture: ComponentFixture<FormationAutoriseeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationAutoriseeFormService: FormationAutoriseeFormService;
  let formationAutoriseeService: FormationAutoriseeService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormationAutoriseeUpdateComponent],
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
      .overrideTemplate(FormationAutoriseeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationAutoriseeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationAutoriseeFormService = TestBed.inject(FormationAutoriseeFormService);
    formationAutoriseeService = TestBed.inject(FormationAutoriseeService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formation query and add missing value', () => {
      const formationAutorisee: IFormationAutorisee = { id: 456 };
      const formations: IFormation[] = [{ id: 30398 }];
      formationAutorisee.formations = formations;

      const formationCollection: IFormation[] = [{ id: 14976 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [...formations];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationAutorisee });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining),
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationAutorisee: IFormationAutorisee = { id: 456 };
      const formation: IFormation = { id: 25099 };
      formationAutorisee.formations = [formation];

      activatedRoute.data = of({ formationAutorisee });
      comp.ngOnInit();

      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.formationAutorisee).toEqual(formationAutorisee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationAutorisee>>();
      const formationAutorisee = { id: 123 };
      jest.spyOn(formationAutoriseeFormService, 'getFormationAutorisee').mockReturnValue(formationAutorisee);
      jest.spyOn(formationAutoriseeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationAutorisee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationAutorisee }));
      saveSubject.complete();

      // THEN
      expect(formationAutoriseeFormService.getFormationAutorisee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationAutoriseeService.update).toHaveBeenCalledWith(expect.objectContaining(formationAutorisee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationAutorisee>>();
      const formationAutorisee = { id: 123 };
      jest.spyOn(formationAutoriseeFormService, 'getFormationAutorisee').mockReturnValue({ id: null });
      jest.spyOn(formationAutoriseeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationAutorisee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationAutorisee }));
      saveSubject.complete();

      // THEN
      expect(formationAutoriseeFormService.getFormationAutorisee).toHaveBeenCalled();
      expect(formationAutoriseeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationAutorisee>>();
      const formationAutorisee = { id: 123 };
      jest.spyOn(formationAutoriseeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationAutorisee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationAutoriseeService.update).toHaveBeenCalled();
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
