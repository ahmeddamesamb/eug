import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeAdmission } from 'app/entities/microserviceGIR/type-admission/type-admission.model';
import { TypeAdmissionService } from 'app/entities/microserviceGIR/type-admission/service/type-admission.service';
import { IAnneeAcademique } from 'app/entities/microserviceGIR/annee-academique/annee-academique.model';
import { AnneeAcademiqueService } from 'app/entities/microserviceGIR/annee-academique/service/annee-academique.service';
import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { IInscriptionAdministrative } from '../inscription-administrative.model';
import { InscriptionAdministrativeService } from '../service/inscription-administrative.service';
import { InscriptionAdministrativeFormService } from './inscription-administrative-form.service';

import { InscriptionAdministrativeUpdateComponent } from './inscription-administrative-update.component';

describe('InscriptionAdministrative Management Update Component', () => {
  let comp: InscriptionAdministrativeUpdateComponent;
  let fixture: ComponentFixture<InscriptionAdministrativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionAdministrativeFormService: InscriptionAdministrativeFormService;
  let inscriptionAdministrativeService: InscriptionAdministrativeService;
  let typeAdmissionService: TypeAdmissionService;
  let anneeAcademiqueService: AnneeAcademiqueService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InscriptionAdministrativeUpdateComponent],
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
      .overrideTemplate(InscriptionAdministrativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionAdministrativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionAdministrativeFormService = TestBed.inject(InscriptionAdministrativeFormService);
    inscriptionAdministrativeService = TestBed.inject(InscriptionAdministrativeService);
    typeAdmissionService = TestBed.inject(TypeAdmissionService);
    anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeAdmission query and add missing value', () => {
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 456 };
      const typeAdmission: ITypeAdmission = { id: 16059 };
      inscriptionAdministrative.typeAdmission = typeAdmission;

      const typeAdmissionCollection: ITypeAdmission[] = [{ id: 28927 }];
      jest.spyOn(typeAdmissionService, 'query').mockReturnValue(of(new HttpResponse({ body: typeAdmissionCollection })));
      const additionalTypeAdmissions = [typeAdmission];
      const expectedCollection: ITypeAdmission[] = [...additionalTypeAdmissions, ...typeAdmissionCollection];
      jest.spyOn(typeAdmissionService, 'addTypeAdmissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      expect(typeAdmissionService.query).toHaveBeenCalled();
      expect(typeAdmissionService.addTypeAdmissionToCollectionIfMissing).toHaveBeenCalledWith(
        typeAdmissionCollection,
        ...additionalTypeAdmissions.map(expect.objectContaining),
      );
      expect(comp.typeAdmissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AnneeAcademique query and add missing value', () => {
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 456 };
      const anneeAcademique: IAnneeAcademique = { id: 23193 };
      inscriptionAdministrative.anneeAcademique = anneeAcademique;

      const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 866 }];
      jest.spyOn(anneeAcademiqueService, 'query').mockReturnValue(of(new HttpResponse({ body: anneeAcademiqueCollection })));
      const additionalAnneeAcademiques = [anneeAcademique];
      const expectedCollection: IAnneeAcademique[] = [...additionalAnneeAcademiques, ...anneeAcademiqueCollection];
      jest.spyOn(anneeAcademiqueService, 'addAnneeAcademiqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      expect(anneeAcademiqueService.query).toHaveBeenCalled();
      expect(anneeAcademiqueService.addAnneeAcademiqueToCollectionIfMissing).toHaveBeenCalledWith(
        anneeAcademiqueCollection,
        ...additionalAnneeAcademiques.map(expect.objectContaining),
      );
      expect(comp.anneeAcademiquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etudiant query and add missing value', () => {
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 456 };
      const etudiant: IEtudiant = { id: 20559 };
      inscriptionAdministrative.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 7431 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(
        etudiantCollection,
        ...additionalEtudiants.map(expect.objectContaining),
      );
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscriptionAdministrative: IInscriptionAdministrative = { id: 456 };
      const typeAdmission: ITypeAdmission = { id: 22645 };
      inscriptionAdministrative.typeAdmission = typeAdmission;
      const anneeAcademique: IAnneeAcademique = { id: 15603 };
      inscriptionAdministrative.anneeAcademique = anneeAcademique;
      const etudiant: IEtudiant = { id: 20150 };
      inscriptionAdministrative.etudiant = etudiant;

      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      expect(comp.typeAdmissionsSharedCollection).toContain(typeAdmission);
      expect(comp.anneeAcademiquesSharedCollection).toContain(anneeAcademique);
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.inscriptionAdministrative).toEqual(inscriptionAdministrative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrative>>();
      const inscriptionAdministrative = { id: 123 };
      jest.spyOn(inscriptionAdministrativeFormService, 'getInscriptionAdministrative').mockReturnValue(inscriptionAdministrative);
      jest.spyOn(inscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(inscriptionAdministrativeFormService.getInscriptionAdministrative).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionAdministrativeService.update).toHaveBeenCalledWith(expect.objectContaining(inscriptionAdministrative));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrative>>();
      const inscriptionAdministrative = { id: 123 };
      jest.spyOn(inscriptionAdministrativeFormService, 'getInscriptionAdministrative').mockReturnValue({ id: null });
      jest.spyOn(inscriptionAdministrativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrative: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionAdministrative }));
      saveSubject.complete();

      // THEN
      expect(inscriptionAdministrativeFormService.getInscriptionAdministrative).toHaveBeenCalled();
      expect(inscriptionAdministrativeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionAdministrative>>();
      const inscriptionAdministrative = { id: 123 };
      jest.spyOn(inscriptionAdministrativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionAdministrative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionAdministrativeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeAdmission', () => {
      it('Should forward to typeAdmissionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeAdmissionService, 'compareTypeAdmission');
        comp.compareTypeAdmission(entity, entity2);
        expect(typeAdmissionService.compareTypeAdmission).toHaveBeenCalledWith(entity, entity2);
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
