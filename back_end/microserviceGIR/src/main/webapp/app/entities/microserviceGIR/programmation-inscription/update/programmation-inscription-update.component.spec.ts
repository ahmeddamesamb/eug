import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { IFormation } from 'app/entities/microserviceGIR/formation/formation.model';
import { FormationService } from 'app/entities/microserviceGIR/formation/service/formation.service';
import { ICampagne } from 'app/entities/microserviceGIR/campagne/campagne.model';
import { CampagneService } from 'app/entities/microserviceGIR/campagne/service/campagne.service';
import { IProgrammationInscription } from '../programmation-inscription.model';
import { ProgrammationInscriptionService } from '../service/programmation-inscription.service';
import { ProgrammationInscriptionFormService } from './programmation-inscription-form.service';

import { ProgrammationInscriptionUpdateComponent } from './programmation-inscription-update.component';

describe('ProgrammationInscription Management Update Component', () => {
  let comp: ProgrammationInscriptionUpdateComponent;
  let fixture: ComponentFixture<ProgrammationInscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let programmationInscriptionFormService: ProgrammationInscriptionFormService;
  let programmationInscriptionService: ProgrammationInscriptionService;
  let anneeAcademiqueService: AnneeAcademiqueService;
  let formationService: FormationService;
  let campagneService: CampagneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProgrammationInscriptionUpdateComponent],
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
      .overrideTemplate(ProgrammationInscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgrammationInscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    programmationInscriptionFormService = TestBed.inject(ProgrammationInscriptionFormService);
    programmationInscriptionService = TestBed.inject(ProgrammationInscriptionService);
    anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);
    formationService = TestBed.inject(FormationService);
    campagneService = TestBed.inject(CampagneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AnneeAcademique query and add missing value', () => {
      const programmationInscription: IProgrammationInscription = { id: 456 };
      const anneeAcademique: IAnneeAcademique = { id: 17585 };
      programmationInscription.anneeAcademique = anneeAcademique;

      const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 30108 }];
      jest.spyOn(anneeAcademiqueService, 'query').mockReturnValue(of(new HttpResponse({ body: anneeAcademiqueCollection })));
      const additionalAnneeAcademiques = [anneeAcademique];
      const expectedCollection: IAnneeAcademique[] = [...additionalAnneeAcademiques, ...anneeAcademiqueCollection];
      jest.spyOn(anneeAcademiqueService, 'addAnneeAcademiqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      expect(anneeAcademiqueService.query).toHaveBeenCalled();
      expect(anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing).toHaveBeenCalledWith(
        anneeAcademiqueCollection,
        ...additionalAnneeAcademiques.map(expect.objectContaining),
      );
      expect(comp.anneeAcademiquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Formation query and add missing value', () => {
      const programmationInscription: IProgrammationInscription = { id: 456 };
      const formation: IFormation = { id: 28818 };
      programmationInscription.formation = formation;

      const formationCollection: IFormation[] = [{ id: 21102 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining),
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Campagne query and add missing value', () => {
      const programmationInscription: IProgrammationInscription = { id: 456 };
      const campagne: ICampagne = { id: 25500 };
      programmationInscription.campagne = campagne;

      const campagneCollection: ICampagne[] = [{ id: 26637 }];
      jest.spyOn(campagneService, 'query').mockReturnValue(of(new HttpResponse({ body: campagneCollection })));
      const additionalCampagnes = [campagne];
      const expectedCollection: ICampagne[] = [...additionalCampagnes, ...campagneCollection];
      jest.spyOn(campagneService, 'addCampagneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      expect(campagneService.query).toHaveBeenCalled();
      expect(campagneService.addCampagneToCollectionIfMissing).toHaveBeenCalledWith(
        campagneCollection,
        ...additionalCampagnes.map(expect.objectContaining),
      );
      expect(comp.campagnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const programmationInscription: IProgrammationInscription = { id: 456 };
      const anneeAcademique: IAnneeAcademique = { id: 18152 };
      programmationInscription.anneeAcademique = anneeAcademique;
      const formation: IFormation = { id: 15166 };
      programmationInscription.formation = formation;
      const campagne: ICampagne = { id: 7937 };
      programmationInscription.campagne = campagne;

      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      expect(comp.anneeAcademiquesSharedCollection).toContain(anneeAcademique);
      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.campagnesSharedCollection).toContain(campagne);
      expect(comp.programmationInscription).toEqual(programmationInscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgrammationInscription>>();
      const programmationInscription = { id: 123 };
      jest.spyOn(programmationInscriptionFormService, 'getProgrammationInscription').mockReturnValue(programmationInscription);
      jest.spyOn(programmationInscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programmationInscription }));
      saveSubject.complete();

      // THEN
      expect(programmationInscriptionFormService.getProgrammationInscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(programmationInscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(programmationInscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgrammationInscription>>();
      const programmationInscription = { id: 123 };
      jest.spyOn(programmationInscriptionFormService, 'getProgrammationInscription').mockReturnValue({ id: null });
      jest.spyOn(programmationInscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programmationInscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programmationInscription }));
      saveSubject.complete();

      // THEN
      expect(programmationInscriptionFormService.getProgrammationInscription).toHaveBeenCalled();
      expect(programmationInscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProgrammationInscription>>();
      const programmationInscription = { id: 123 };
      jest.spyOn(programmationInscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programmationInscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(programmationInscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAnneeAcademique', () => {
      it('Should forward to anneeAcademiqueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(anneeAcademiqueService, 'compareAnneeAcademique');
        comp.compareAnneeAcademique(entity, entity2);
        expect(anneeAcademiqueService.compareAnneeAcademique).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFormation', () => {
      it('Should forward to formationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formationService, 'compareFormation');
        comp.compareFormation(entity, entity2);
        expect(formationService.compareFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCampagne', () => {
      it('Should forward to campagneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(campagneService, 'compareCampagne');
        comp.compareCampagne(entity, entity2);
        expect(campagneService.compareCampagne).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
