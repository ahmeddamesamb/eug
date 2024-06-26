import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DoctoratService } from '../service/doctorat.service';
import { IDoctorat } from '../doctorat.model';
import { DoctoratFormService } from './doctorat-form.service';

import { DoctoratUpdateComponent } from './doctorat-update.component';

describe('Doctorat Management Update Component', () => {
  let comp: DoctoratUpdateComponent;
  let fixture: ComponentFixture<DoctoratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let doctoratFormService: DoctoratFormService;
  let doctoratService: DoctoratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DoctoratUpdateComponent],
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
      .overrideTemplate(DoctoratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DoctoratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    doctoratFormService = TestBed.inject(DoctoratFormService);
    doctoratService = TestBed.inject(DoctoratService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const doctorat: IDoctorat = { id: 456 };

      activatedRoute.data = of({ doctorat });
      comp.ngOnInit();

      expect(comp.doctorat).toEqual(doctorat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorat>>();
      const doctorat = { id: 123 };
      jest.spyOn(doctoratFormService, 'getDoctorat').mockReturnValue(doctorat);
      jest.spyOn(doctoratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doctorat }));
      saveSubject.complete();

      // THEN
      expect(doctoratFormService.getDoctorat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(doctoratService.update).toHaveBeenCalledWith(expect.objectContaining(doctorat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorat>>();
      const doctorat = { id: 123 };
      jest.spyOn(doctoratFormService, 'getDoctorat').mockReturnValue({ id: null });
      jest.spyOn(doctoratService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doctorat }));
      saveSubject.complete();

      // THEN
      expect(doctoratFormService.getDoctorat).toHaveBeenCalled();
      expect(doctoratService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorat>>();
      const doctorat = { id: 123 };
      jest.spyOn(doctoratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(doctoratService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
