import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDoctorat } from 'app/entities/microserviceGIR/doctorat/doctorat.model';
import { DoctoratService } from 'app/entities/microserviceGIR/doctorat/service/doctorat.service';
import { IInscriptionAdministrativeFormation } from 'app/entities/microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from 'app/entities/microserviceGIR/inscription-administrative-formation/service/inscription-administrative-formation.service';
import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { InscriptionDoctoratService } from '../service/inscription-doctorat.service';
import { InscriptionDoctoratFormService } from './inscription-doctorat-form.service';

import { InscriptionDoctoratUpdateComponent } from './inscription-doctorat-update.component';

describe('InscriptionDoctorat Management Update Component', () => {
  let comp: InscriptionDoctoratUpdateComponent;
  let fixture: ComponentFixture<InscriptionDoctoratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionDoctoratFormService: InscriptionDoctoratFormService;
  let inscriptionDoctoratService: InscriptionDoctoratService;
  let doctoratService: DoctoratService;
  let inscriptionAdministrativeFormationService: InscriptionAdministrativeFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InscriptionDoctoratUpdateComponent],
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
      .overrideTemplate(InscriptionDoctoratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionDoctoratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionDoctoratFormService = TestBed.inject(InscriptionDoctoratFormService);
    inscriptionDoctoratService = TestBed.inject(InscriptionDoctoratService);
    doctoratService = TestBed.inject(DoctoratService);
    inscriptionAdministrativeFormationService = TestBed.inject(InscriptionAdministrativeFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Doctorat query and add missing value', () => {
      const inscriptionDoctorat: IInscriptionDoctorat = { id: 456 };
      const doctorat: IDoctorat = { id: 5773 };
      inscriptionDoctorat.doctorat = doctorat;

      const doctoratCollection: IDoctorat[] = [{ id: 24079 }];
      jest.spyOn(doctoratService, 'query').mockReturnValue(of(new HttpResponse({ body: doctoratCollection })));
      const additionalDoctorats = [doctorat];
      const expectedCollection: IDoctorat[] = [...additionalDoctorats, ...doctoratCollection];
      jest.spyOn(doctoratService, 'addDoctoratToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionDoctorat });
      comp.ngOnInit();

      expect(doctoratService.query).toHaveBeenCalled();
      expect(doctoratService.addDoctoratToCollectionIfMissing).toHaveBeenCalledWith(
        doctoratCollection,
        ...additionalDoctorats.map(expect.objectContaining),
      );
      expect(comp.doctoratsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InscriptionAdministrativeFormation query and add missing value', () => {
      const inscriptionDoctorat: IInscriptionDoctorat = { id: 456 };
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 16683 };
      inscriptionDoctorat.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;

      const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [{ id: 27242 }];
      jest
        .spyOn(inscriptionAdministrativeFormationService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: inscriptionAdministrativeFormationCollection })));
      const additionalInscriptionAdministrativeFormations = [inscriptionAdministrativeFormation];
      const expectedCollection: IInscriptionAdministrativeFormation[] = [
        ...additionalInscriptionAdministrativeFormations,
        ...inscriptionAdministrativeFormationCollection,
      ];
      jest
        .spyOn(inscriptionAdministrativeFormationService, 'addInscriptionAdministrativeFormationToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionDoctorat });
      comp.ngOnInit();

      expect(inscriptionAdministrativeFormationService.query).toHaveBeenCalled();
      expect(inscriptionAdministrativeFormationService.addInscriptionAdministrativeFormationToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionAdministrativeFormationCollection,
        ...additionalInscriptionAdministrativeFormations.map(expect.objectContaining),
      );
      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscriptionDoctorat: IInscriptionDoctorat = { id: 456 };
      const doctorat: IDoctorat = { id: 16816 };
      inscriptionDoctorat.doctorat = doctorat;
      const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = { id: 29016 };
      inscriptionDoctorat.inscriptionAdministrativeFormation = inscriptionAdministrativeFormation;

      activatedRoute.data = of({ inscriptionDoctorat });
      comp.ngOnInit();

      expect(comp.doctoratsSharedCollection).toContain(doctorat);
      expect(comp.inscriptionAdministrativeFormationsSharedCollection).toContain(inscriptionAdministrativeFormation);
      expect(comp.inscriptionDoctorat).toEqual(inscriptionDoctorat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionDoctorat>>();
      const inscriptionDoctorat = { id: 123 };
      jest.spyOn(inscriptionDoctoratFormService, 'getInscriptionDoctorat').mockReturnValue(inscriptionDoctorat);
      jest.spyOn(inscriptionDoctoratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionDoctorat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionDoctorat }));
      saveSubject.complete();

      // THEN
      expect(inscriptionDoctoratFormService.getInscriptionDoctorat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionDoctoratService.update).toHaveBeenCalledWith(expect.objectContaining(inscriptionDoctorat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionDoctorat>>();
      const inscriptionDoctorat = { id: 123 };
      jest.spyOn(inscriptionDoctoratFormService, 'getInscriptionDoctorat').mockReturnValue({ id: null });
      jest.spyOn(inscriptionDoctoratService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionDoctorat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionDoctorat }));
      saveSubject.complete();

      // THEN
      expect(inscriptionDoctoratFormService.getInscriptionDoctorat).toHaveBeenCalled();
      expect(inscriptionDoctoratService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionDoctorat>>();
      const inscriptionDoctorat = { id: 123 };
      jest.spyOn(inscriptionDoctoratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionDoctorat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionDoctoratService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDoctorat', () => {
      it('Should forward to doctoratService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(doctoratService, 'compareDoctorat');
        comp.compareDoctorat(entity, entity2);
        expect(doctoratService.compareDoctorat).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInscriptionAdministrativeFormation', () => {
      it('Should forward to inscriptionAdministrativeFormationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionAdministrativeFormationService, 'compareInscriptionAdministrativeFormation');
        comp.compareInscriptionAdministrativeFormation(entity, entity2);
        expect(inscriptionAdministrativeFormationService.compareInscriptionAdministrativeFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
