import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { IFormationInvalide } from '../formation-invalide.model';
import { FormationInvalideService } from '../service/formation-invalide.service';
import { FormationInvalideFormService } from './formation-invalide-form.service';

import { FormationInvalideUpdateComponent } from './formation-invalide-update.component';

describe('FormationInvalide Management Update Component', () => {
  let comp: FormationInvalideUpdateComponent;
  let fixture: ComponentFixture<FormationInvalideUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationInvalideFormService: FormationInvalideFormService;
  let formationInvalideService: FormationInvalideService;
  let formationService: FormationService;
  let anneeAcademiqueService: AnneeAcademiqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormationInvalideUpdateComponent],
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
      .overrideTemplate(FormationInvalideUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationInvalideUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationInvalideFormService = TestBed.inject(FormationInvalideFormService);
    formationInvalideService = TestBed.inject(FormationInvalideService);
    formationService = TestBed.inject(FormationService);
    anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formation query and add missing value', () => {
      const formationInvalide: IFormationInvalide = { id: 456 };
      const formation: IFormation = { id: 1524 };
      formationInvalide.formation = formation;

      const formationCollection: IFormation[] = [{ id: 7911 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationInvalide });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining),
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AnneeAcademique query and add missing value', () => {
      const formationInvalide: IFormationInvalide = { id: 456 };
      const anneeAcademique: IAnneeAcademique = { id: 1375 };
      formationInvalide.anneeAcademique = anneeAcademique;

      const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 23856 }];
      jest.spyOn(anneeAcademiqueService, 'query').mockReturnValue(of(new HttpResponse({ body: anneeAcademiqueCollection })));
      const additionalAnneeAcademiques = [anneeAcademique];
      const expectedCollection: IAnneeAcademique[] = [...additionalAnneeAcademiques, ...anneeAcademiqueCollection];
      jest.spyOn(anneeAcademiqueService, 'addAnneeAcademiqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationInvalide });
      comp.ngOnInit();

      expect(anneeAcademiqueService.query).toHaveBeenCalled();
      expect(anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing).toHaveBeenCalledWith(
        anneeAcademiqueCollection,
        ...additionalAnneeAcademiques.map(expect.objectContaining),
      );
      expect(comp.anneeAcademiquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationInvalide: IFormationInvalide = { id: 456 };
      const formation: IFormation = { id: 23951 };
      formationInvalide.formation = formation;
      const anneeAcademique: IAnneeAcademique = { id: 2535 };
      formationInvalide.anneeAcademique = anneeAcademique;

      activatedRoute.data = of({ formationInvalide });
      comp.ngOnInit();

      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.anneeAcademiquesSharedCollection).toContain(anneeAcademique);
      expect(comp.formationInvalide).toEqual(formationInvalide);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationInvalide>>();
      const formationInvalide = { id: 123 };
      jest.spyOn(formationInvalideFormService, 'getFormationInvalide').mockReturnValue(formationInvalide);
      jest.spyOn(formationInvalideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInvalide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationInvalide }));
      saveSubject.complete();

      // THEN
      expect(formationInvalideFormService.getFormationInvalide).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationInvalideService.update).toHaveBeenCalledWith(expect.objectContaining(formationInvalide));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationInvalide>>();
      const formationInvalide = { id: 123 };
      jest.spyOn(formationInvalideFormService, 'getFormationInvalide').mockReturnValue({ id: null });
      jest.spyOn(formationInvalideService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInvalide: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationInvalide }));
      saveSubject.complete();

      // THEN
      expect(formationInvalideFormService.getFormationInvalide).toHaveBeenCalled();
      expect(formationInvalideService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormationInvalide>>();
      const formationInvalide = { id: 123 };
      jest.spyOn(formationInvalideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInvalide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationInvalideService.update).toHaveBeenCalled();
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

    describe('compareAnneeAcademique', () => {
      it('Should forward to anneeAcademiqueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(anneeAcademiqueService, 'compareAnneeAcademique');
        comp.compareAnneeAcademique(entity, entity2);
        expect(anneeAcademiqueService.compareAnneeAcademique).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
