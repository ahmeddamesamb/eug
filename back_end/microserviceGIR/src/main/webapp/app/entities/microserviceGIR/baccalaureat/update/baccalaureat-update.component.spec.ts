import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEtudiant } from 'app/entities/microserviceGIR/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/microserviceGIR/etudiant/service/etudiant.service';
import { ISerie } from 'app/entities/microserviceGIR/serie/serie.model';
import { SerieService } from 'app/entities/microserviceGIR/serie/service/serie.service';
import { IBaccalaureat } from '../baccalaureat.model';
import { BaccalaureatService } from '../service/baccalaureat.service';
import { BaccalaureatFormService } from './baccalaureat-form.service';

import { BaccalaureatUpdateComponent } from './baccalaureat-update.component';

describe('Baccalaureat Management Update Component', () => {
  let comp: BaccalaureatUpdateComponent;
  let fixture: ComponentFixture<BaccalaureatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let baccalaureatFormService: BaccalaureatFormService;
  let baccalaureatService: BaccalaureatService;
  let etudiantService: EtudiantService;
  let serieService: SerieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BaccalaureatUpdateComponent],
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
      .overrideTemplate(BaccalaureatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BaccalaureatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    baccalaureatFormService = TestBed.inject(BaccalaureatFormService);
    baccalaureatService = TestBed.inject(BaccalaureatService);
    etudiantService = TestBed.inject(EtudiantService);
    serieService = TestBed.inject(SerieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call etudiant query and add missing value', () => {
      const baccalaureat: IBaccalaureat = { id: 456 };
      const etudiant: IEtudiant = { id: 32663 };
      baccalaureat.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 25284 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const expectedCollection: IEtudiant[] = [etudiant, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ baccalaureat });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, etudiant);
      expect(comp.etudiantsCollection).toEqual(expectedCollection);
    });

    it('Should call Serie query and add missing value', () => {
      const baccalaureat: IBaccalaureat = { id: 456 };
      const serie: ISerie = { id: 18950 };
      baccalaureat.serie = serie;

      const serieCollection: ISerie[] = [{ id: 4745 }];
      jest.spyOn(serieService, 'query').mockReturnValue(of(new HttpResponse({ body: serieCollection })));
      const additionalSeries = [serie];
      const expectedCollection: ISerie[] = [...additionalSeries, ...serieCollection];
      jest.spyOn(serieService, 'addSerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ baccalaureat });
      comp.ngOnInit();

      expect(serieService.query).toHaveBeenCalled();
      expect(serieService.addSerieToCollectionIfMissing).toHaveBeenCalledWith(
        serieCollection,
        ...additionalSeries.map(expect.objectContaining),
      );
      expect(comp.seriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const baccalaureat: IBaccalaureat = { id: 456 };
      const etudiant: IEtudiant = { id: 21442 };
      baccalaureat.etudiant = etudiant;
      const serie: ISerie = { id: 9644 };
      baccalaureat.serie = serie;

      activatedRoute.data = of({ baccalaureat });
      comp.ngOnInit();

      expect(comp.etudiantsCollection).toContain(etudiant);
      expect(comp.seriesSharedCollection).toContain(serie);
      expect(comp.baccalaureat).toEqual(baccalaureat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaccalaureat>>();
      const baccalaureat = { id: 123 };
      jest.spyOn(baccalaureatFormService, 'getBaccalaureat').mockReturnValue(baccalaureat);
      jest.spyOn(baccalaureatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baccalaureat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baccalaureat }));
      saveSubject.complete();

      // THEN
      expect(baccalaureatFormService.getBaccalaureat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(baccalaureatService.update).toHaveBeenCalledWith(expect.objectContaining(baccalaureat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaccalaureat>>();
      const baccalaureat = { id: 123 };
      jest.spyOn(baccalaureatFormService, 'getBaccalaureat').mockReturnValue({ id: null });
      jest.spyOn(baccalaureatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baccalaureat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baccalaureat }));
      saveSubject.complete();

      // THEN
      expect(baccalaureatFormService.getBaccalaureat).toHaveBeenCalled();
      expect(baccalaureatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaccalaureat>>();
      const baccalaureat = { id: 123 };
      jest.spyOn(baccalaureatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baccalaureat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(baccalaureatService.update).toHaveBeenCalled();
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

    describe('compareSerie', () => {
      it('Should forward to serieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serieService, 'compareSerie');
        comp.compareSerie(entity, entity2);
        expect(serieService.compareSerie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
