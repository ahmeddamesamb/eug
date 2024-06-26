import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SerieService } from '../service/serie.service';
import { ISerie } from '../serie.model';
import { SerieFormService } from './serie-form.service';

import { SerieUpdateComponent } from './serie-update.component';

describe('Serie Management Update Component', () => {
  let comp: SerieUpdateComponent;
  let fixture: ComponentFixture<SerieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serieFormService: SerieFormService;
  let serieService: SerieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SerieUpdateComponent],
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
      .overrideTemplate(SerieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SerieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serieFormService = TestBed.inject(SerieFormService);
    serieService = TestBed.inject(SerieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serie: ISerie = { id: 456 };

      activatedRoute.data = of({ serie });
      comp.ngOnInit();

      expect(comp.serie).toEqual(serie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISerie>>();
      const serie = { id: 123 };
      jest.spyOn(serieFormService, 'getSerie').mockReturnValue(serie);
      jest.spyOn(serieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serie }));
      saveSubject.complete();

      // THEN
      expect(serieFormService.getSerie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serieService.update).toHaveBeenCalledWith(expect.objectContaining(serie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISerie>>();
      const serie = { id: 123 };
      jest.spyOn(serieFormService, 'getSerie').mockReturnValue({ id: null });
      jest.spyOn(serieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serie }));
      saveSubject.complete();

      // THEN
      expect(serieFormService.getSerie).toHaveBeenCalled();
      expect(serieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISerie>>();
      const serie = { id: 123 };
      jest.spyOn(serieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
