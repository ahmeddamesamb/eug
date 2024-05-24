import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDisciplineSportive } from 'app/entities/microserviceGIR/discipline-sportive/discipline-sportive.model';
import { DisciplineSportiveService } from 'app/entities/microserviceGIR/discipline-sportive/service/discipline-sportive.service';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { IDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';
import { DisciplineSportiveEtudiantService } from '../service/discipline-sportive-etudiant.service';
import { DisciplineSportiveEtudiantFormService } from './discipline-sportive-etudiant-form.service';

import { DisciplineSportiveEtudiantUpdateComponent } from './discipline-sportive-etudiant-update.component';

describe('DisciplineSportiveEtudiant Management Update Component', () => {
  let comp: DisciplineSportiveEtudiantUpdateComponent;
  let fixture: ComponentFixture<DisciplineSportiveEtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disciplineSportiveEtudiantFormService: DisciplineSportiveEtudiantFormService;
  let disciplineSportiveEtudiantService: DisciplineSportiveEtudiantService;
  let disciplineSportiveService: DisciplineSportiveService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DisciplineSportiveEtudiantUpdateComponent],
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
      .overrideTemplate(DisciplineSportiveEtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplineSportiveEtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disciplineSportiveEtudiantFormService = TestBed.inject(DisciplineSportiveEtudiantFormService);
    disciplineSportiveEtudiantService = TestBed.inject(DisciplineSportiveEtudiantService);
    disciplineSportiveService = TestBed.inject(DisciplineSportiveService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DisciplineSportive query and add missing value', () => {
      const disciplineSportiveEtudiant: IDisciplineSportiveEtudiant = { id: 456 };
      const disciplineSportive: IDisciplineSportive = { id: 18926 };
      disciplineSportiveEtudiant.disciplineSportive = disciplineSportive;

      const disciplineSportiveCollection: IDisciplineSportive[] = [{ id: 18872 }];
      jest.spyOn(disciplineSportiveService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplineSportiveCollection })));
      const additionalDisciplineSportives = [disciplineSportive];
      const expectedCollection: IDisciplineSportive[] = [...additionalDisciplineSportives, ...disciplineSportiveCollection];
      jest.spyOn(disciplineSportiveService, 'addDisciplineSportiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disciplineSportiveEtudiant });
      comp.ngOnInit();

      expect(disciplineSportiveService.query).toHaveBeenCalled();
      expect(disciplineSportiveService.addDisciplineSportiveToCollectionIfMissing).toHaveBeenCalledWith(
        disciplineSportiveCollection,
        ...additionalDisciplineSportives.map(expect.objectContaining),
      );
      expect(comp.disciplineSportivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etudiant query and add missing value', () => {
      const disciplineSportiveEtudiant: IDisciplineSportiveEtudiant = { id: 456 };
      const etudiant: IEtudiant = { id: 16129 };
      disciplineSportiveEtudiant.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 20599 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disciplineSportiveEtudiant });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(
        etudiantCollection,
        ...additionalEtudiants.map(expect.objectContaining),
      );
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const disciplineSportiveEtudiant: IDisciplineSportiveEtudiant = { id: 456 };
      const disciplineSportive: IDisciplineSportive = { id: 17230 };
      disciplineSportiveEtudiant.disciplineSportive = disciplineSportive;
      const etudiant: IEtudiant = { id: 6289 };
      disciplineSportiveEtudiant.etudiant = etudiant;

      activatedRoute.data = of({ disciplineSportiveEtudiant });
      comp.ngOnInit();

      expect(comp.disciplineSportivesSharedCollection).toContain(disciplineSportive);
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.disciplineSportiveEtudiant).toEqual(disciplineSportiveEtudiant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportiveEtudiant>>();
      const disciplineSportiveEtudiant = { id: 123 };
      jest.spyOn(disciplineSportiveEtudiantFormService, 'getDisciplineSportiveEtudiant').mockReturnValue(disciplineSportiveEtudiant);
      jest.spyOn(disciplineSportiveEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportiveEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplineSportiveEtudiant }));
      saveSubject.complete();

      // THEN
      expect(disciplineSportiveEtudiantFormService.getDisciplineSportiveEtudiant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(disciplineSportiveEtudiantService.update).toHaveBeenCalledWith(expect.objectContaining(disciplineSportiveEtudiant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportiveEtudiant>>();
      const disciplineSportiveEtudiant = { id: 123 };
      jest.spyOn(disciplineSportiveEtudiantFormService, 'getDisciplineSportiveEtudiant').mockReturnValue({ id: null });
      jest.spyOn(disciplineSportiveEtudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportiveEtudiant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplineSportiveEtudiant }));
      saveSubject.complete();

      // THEN
      expect(disciplineSportiveEtudiantFormService.getDisciplineSportiveEtudiant).toHaveBeenCalled();
      expect(disciplineSportiveEtudiantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportiveEtudiant>>();
      const disciplineSportiveEtudiant = { id: 123 };
      jest.spyOn(disciplineSportiveEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportiveEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disciplineSportiveEtudiantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDisciplineSportive', () => {
      it('Should forward to disciplineSportiveService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(disciplineSportiveService, 'compareDisciplineSportive');
        comp.compareDisciplineSportive(entity, entity2);
        expect(disciplineSportiveService.compareDisciplineSportive).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEtudiant', () => {
      it('Should forward to etudiantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(etudiantService, 'compareEtudiant');
        comp.compareEtudiant(entity, entity2);
        expect(etudiantService.compareEtudiant).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
