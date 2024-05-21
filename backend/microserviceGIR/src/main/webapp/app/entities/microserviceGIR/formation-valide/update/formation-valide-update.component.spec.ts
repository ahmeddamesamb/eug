import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { FormationValideService } from '../service/formation-valide.service';
import { IFormationValide } from '../formation-valide.model';
import { FormationValideFormService } from './formation-valide-form.service';

import { FormationValideUpdateComponent } from './formation-valide-update.component';

describe('FormationValide Management Update Component', () => {
  let comp: FormationValideUpdateComponent;
  let fixture: ComponentFixture<FormationValideUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationValideFormService: FormationValideFormService;
  let formationValideService: FormationValideService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormationValideUpdateComponent],
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
      .overrideTemplate(FormationValideUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationValideUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationValideFormService = TestBed.inject(FormationValideFormService);
    formationValideService = TestBed.inject(FormationValideService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formation query and add missing value', () => {
      const formationValide: IFormationValide = { id: 456 };
      const formation: IFormation = { id: 30834 };
      formationValide.formation = formation;

      const formationCollection: IFormation[] = [{ id: 1524 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationValide });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining),
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationValide: IFormationValide = { id: 456 };
      const formation: IFormation = { id: 7911 };
      formationValide.formation = formation;

      activatedRoute.data = of({ formationValide });
      comp.ngOnInit();

      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.formationValide).toEqual(formationValide);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationValide>>();
      const formationValide = { id: 123 };
      jest.spyOn(formationValideFormService, 'getFormationValide').mockReturnValue(formationValide);
      jest.spyOn(formationValideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationValide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationValide }));
      saveSubject.complete();

      // THEN
      expect(formationValideFormService.getFormationValide).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationValideService.update).toHaveBeenCalledWith(expect.objectContaining(formationValide));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationValide>>();
      const formationValide = { id: 123 };
      jest.spyOn(formationValideFormService, 'getFormationValide').mockReturnValue({ id: null });
      jest.spyOn(formationValideService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationValide: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationValide }));
      saveSubject.complete();

      // THEN
      expect(formationValideFormService.getFormationValide).toHaveBeenCalled();
      expect(formationValideService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationValide>>();
      const formationValide = { id: 123 };
      jest.spyOn(formationValideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationValide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationValideService.update).toHaveBeenCalled();
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
