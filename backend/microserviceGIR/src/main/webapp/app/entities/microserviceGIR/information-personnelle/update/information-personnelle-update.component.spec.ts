import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { ITypeHandicap } from 'app/entities/microserviceGIR/type-handicap/type-handicap.model';
import { TypeHandicapService } from 'app/entities/microserviceGIR/type-handicap/service/type-handicap.service';
import { ITypeBourse } from 'app/entities/microserviceGIR/type-bourse/type-bourse.model';
import { TypeBourseService } from 'app/entities/microserviceGIR/type-bourse/service/type-bourse.service';
import { IInformationPersonnelle } from '../information-personnelle.model';
import { InformationPersonnelleService } from '../service/information-personnelle.service';
import { InformationPersonnelleFormService } from './information-personnelle-form.service';

import { InformationPersonnelleUpdateComponent } from './information-personnelle-update.component';

describe('InformationPersonnelle Management Update Component', () => {
  let comp: InformationPersonnelleUpdateComponent;
  let fixture: ComponentFixture<InformationPersonnelleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let informationPersonnelleFormService: InformationPersonnelleFormService;
  let informationPersonnelleService: InformationPersonnelleService;
  let etudiantService: EtudiantService;
  let typeHandicapService: TypeHandicapService;
  let typeBourseService: TypeBourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InformationPersonnelleUpdateComponent],
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
      .overrideTemplate(InformationPersonnelleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InformationPersonnelleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    informationPersonnelleFormService = TestBed.inject(InformationPersonnelleFormService);
    informationPersonnelleService = TestBed.inject(InformationPersonnelleService);
    etudiantService = TestBed.inject(EtudiantService);
    typeHandicapService = TestBed.inject(TypeHandicapService);
    typeBourseService = TestBed.inject(TypeBourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call etudiant query and add missing value', () => {
      const informationPersonnelle: IInformationPersonnelle = { id: 456 };
      const etudiant: IEtudiant = { id: 21442 };
      informationPersonnelle.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 16129 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const expectedCollection: IEtudiant[] = [etudiant, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, etudiant);
      expect(comp.etudiantsCollection).toEqual(expectedCollection);
    });

    it('Should call TypeHandicap query and add missing value', () => {
      const informationPersonnelle: IInformationPersonnelle = { id: 456 };
      const typeHandicap: ITypeHandicap = { id: 28923 };
      informationPersonnelle.typeHandicap = typeHandicap;

      const typeHandicapCollection: ITypeHandicap[] = [{ id: 21832 }];
      jest.spyOn(typeHandicapService, 'query').mockReturnValue(of(new HttpResponse({ body: typeHandicapCollection })));
      const additionalTypeHandicaps = [typeHandicap];
      const expectedCollection: ITypeHandicap[] = [...additionalTypeHandicaps, ...typeHandicapCollection];
      jest.spyOn(typeHandicapService, 'addTypeHandicapToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      expect(typeHandicapService.query).toHaveBeenCalled();
      expect(typeHandicapService.addTypeHandicapToCollectionIfMissing).toHaveBeenCalledWith(
        typeHandicapCollection,
        ...additionalTypeHandicaps.map(expect.objectContaining),
      );
      expect(comp.typeHandicapsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeBourse query and add missing value', () => {
      const informationPersonnelle: IInformationPersonnelle = { id: 456 };
      const typeBourse: ITypeBourse = { id: 30393 };
      informationPersonnelle.typeBourse = typeBourse;

      const typeBourseCollection: ITypeBourse[] = [{ id: 27625 }];
      jest.spyOn(typeBourseService, 'query').mockReturnValue(of(new HttpResponse({ body: typeBourseCollection })));
      const additionalTypeBourses = [typeBourse];
      const expectedCollection: ITypeBourse[] = [...additionalTypeBourses, ...typeBourseCollection];
      jest.spyOn(typeBourseService, 'addTypeBourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      expect(typeBourseService.query).toHaveBeenCalled();
      expect(typeBourseService.addTypeBourseToCollectionIfMissing).toHaveBeenCalledWith(
        typeBourseCollection,
        ...additionalTypeBourses.map(expect.objectContaining),
      );
      expect(comp.typeBoursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const informationPersonnelle: IInformationPersonnelle = { id: 456 };
      const etudiant: IEtudiant = { id: 20599 };
      informationPersonnelle.etudiant = etudiant;
      const typeHandicap: ITypeHandicap = { id: 23234 };
      informationPersonnelle.typeHandicap = typeHandicap;
      const typeBourse: ITypeBourse = { id: 9250 };
      informationPersonnelle.typeBourse = typeBourse;

      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      expect(comp.etudiantsCollection).toContain(etudiant);
      expect(comp.typeHandicapsSharedCollection).toContain(typeHandicap);
      expect(comp.typeBoursesSharedCollection).toContain(typeBourse);
      expect(comp.informationPersonnelle).toEqual(informationPersonnelle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationPersonnelle>>();
      const informationPersonnelle = { id: 123 };
      jest.spyOn(informationPersonnelleFormService, 'getInformationPersonnelle').mockReturnValue(informationPersonnelle);
      jest.spyOn(informationPersonnelleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informationPersonnelle }));
      saveSubject.complete();

      // THEN
      expect(informationPersonnelleFormService.getInformationPersonnelle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(informationPersonnelleService.update).toHaveBeenCalledWith(expect.objectContaining(informationPersonnelle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationPersonnelle>>();
      const informationPersonnelle = { id: 123 };
      jest.spyOn(informationPersonnelleFormService, 'getInformationPersonnelle').mockReturnValue({ id: null });
      jest.spyOn(informationPersonnelleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationPersonnelle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informationPersonnelle }));
      saveSubject.complete();

      // THEN
      expect(informationPersonnelleFormService.getInformationPersonnelle).toHaveBeenCalled();
      expect(informationPersonnelleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationPersonnelle>>();
      const informationPersonnelle = { id: 123 };
      jest.spyOn(informationPersonnelleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationPersonnelle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(informationPersonnelleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEtudiant', () => {
      it('Should forward to etudiantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(etudiantService, 'compareEtudiant');
        comp.compareEtudiant(entity, entity2);
        expect(etudiantService.compareEtudiant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeHandicap', () => {
      it('Should forward to typeHandicapService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeHandicapService, 'compareTypeHandicap');
        comp.compareTypeHandicap(entity, entity2);
        expect(typeHandicapService.compareTypeHandicap).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeBourse', () => {
      it('Should forward to typeBourseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeBourseService, 'compareTypeBourse');
        comp.compareTypeBourse(entity, entity2);
        expect(typeBourseService.compareTypeBourse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
