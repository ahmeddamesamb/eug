import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRegion } from 'app/entities/microserviceGIR/region/region.model';
import { RegionService } from 'app/entities/microserviceGIR/region/service/region.service';
import { ITypeSelection } from 'app/entities/microserviceGIR/type-selection/type-selection.model';
import { TypeSelectionService } from 'app/entities/microserviceGIR/type-selection/service/type-selection.service';
import { ILycee } from 'app/entities/microserviceGIR/lycee/lycee.model';
import { LyceeService } from 'app/entities/microserviceGIR/lycee/service/lycee.service';
import { IEtudiant } from '../etudiant.model';
import { EtudiantService } from '../service/etudiant.service';
import { EtudiantFormService } from './etudiant-form.service';

import { EtudiantUpdateComponent } from './etudiant-update.component';

describe('Etudiant Management Update Component', () => {
  let comp: EtudiantUpdateComponent;
  let fixture: ComponentFixture<EtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etudiantFormService: EtudiantFormService;
  let etudiantService: EtudiantService;
  let regionService: RegionService;
  let typeSelectionService: TypeSelectionService;
  let lyceeService: LyceeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EtudiantUpdateComponent],
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
      .overrideTemplate(EtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etudiantFormService = TestBed.inject(EtudiantFormService);
    etudiantService = TestBed.inject(EtudiantService);
    regionService = TestBed.inject(RegionService);
    typeSelectionService = TestBed.inject(TypeSelectionService);
    lyceeService = TestBed.inject(LyceeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const etudiant: IEtudiant = { id: 456 };
      const region: IRegion = { id: 8363 };
      etudiant.region = region;

      const regionCollection: IRegion[] = [{ id: 1148 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining),
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeSelection query and add missing value', () => {
      const etudiant: IEtudiant = { id: 456 };
      const typeSelection: ITypeSelection = { id: 9853 };
      etudiant.typeSelection = typeSelection;

      const typeSelectionCollection: ITypeSelection[] = [{ id: 22274 }];
      jest.spyOn(typeSelectionService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSelectionCollection })));
      const additionalTypeSelections = [typeSelection];
      const expectedCollection: ITypeSelection[] = [...additionalTypeSelections, ...typeSelectionCollection];
      jest.spyOn(typeSelectionService, 'addTypeSelectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(typeSelectionService.query).toHaveBeenCalled();
      expect(typeSelectionService.addTypeSelectionToCollectionIfMissing).toHaveBeenCalledWith(
        typeSelectionCollection,
        ...additionalTypeSelections.map(expect.objectContaining),
      );
      expect(comp.typeSelectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Lycee query and add missing value', () => {
      const etudiant: IEtudiant = { id: 456 };
      const lycee: ILycee = { id: 3619 };
      etudiant.lycee = lycee;

      const lyceeCollection: ILycee[] = [{ id: 13148 }];
      jest.spyOn(lyceeService, 'query').mockReturnValue(of(new HttpResponse({ body: lyceeCollection })));
      const additionalLycees = [lycee];
      const expectedCollection: ILycee[] = [...additionalLycees, ...lyceeCollection];
      jest.spyOn(lyceeService, 'addLyceeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(lyceeService.query).toHaveBeenCalled();
      expect(lyceeService.addLyceeToCollectionIfMissing).toHaveBeenCalledWith(
        lyceeCollection,
        ...additionalLycees.map(expect.objectContaining),
      );
      expect(comp.lyceesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const etudiant: IEtudiant = { id: 456 };
      const region: IRegion = { id: 13579 };
      etudiant.region = region;
      const typeSelection: ITypeSelection = { id: 736 };
      etudiant.typeSelection = typeSelection;
      const lycee: ILycee = { id: 30024 };
      etudiant.lycee = lycee;

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.typeSelectionsSharedCollection).toContain(typeSelection);
      expect(comp.lyceesSharedCollection).toContain(lycee);
      expect(comp.etudiant).toEqual(etudiant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 123 };
      jest.spyOn(etudiantFormService, 'getEtudiant').mockReturnValue(etudiant);
      jest.spyOn(etudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etudiant }));
      saveSubject.complete();

      // THEN
      expect(etudiantFormService.getEtudiant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etudiantService.update).toHaveBeenCalledWith(expect.objectContaining(etudiant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 123 };
      jest.spyOn(etudiantFormService, 'getEtudiant').mockReturnValue({ id: null });
      jest.spyOn(etudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etudiant }));
      saveSubject.complete();

      // THEN
      expect(etudiantFormService.getEtudiant).toHaveBeenCalled();
      expect(etudiantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 123 };
      jest.spyOn(etudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etudiantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegion', () => {
      it('Should forward to regionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeSelection', () => {
      it('Should forward to typeSelectionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeSelectionService, 'compareTypeSelection');
        comp.compareTypeSelection(entity, entity2);
        expect(typeSelectionService.compareTypeSelection).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLycee', () => {
      it('Should forward to lyceeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(lyceeService, 'compareLycee');
        comp.compareLycee(entity, entity2);
        expect(lyceeService.compareLycee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
