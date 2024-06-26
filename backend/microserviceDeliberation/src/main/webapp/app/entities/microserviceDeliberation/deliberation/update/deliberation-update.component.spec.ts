import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliberationService } from '../service/deliberation.service';
import { IDeliberation } from '../deliberation.model';
import { DeliberationFormService } from './deliberation-form.service';

import { DeliberationUpdateComponent } from './deliberation-update.component';

describe('Deliberation Management Update Component', () => {
  let comp: DeliberationUpdateComponent;
  let fixture: ComponentFixture<DeliberationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliberationFormService: DeliberationFormService;
  let deliberationService: DeliberationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DeliberationUpdateComponent],
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
      .overrideTemplate(DeliberationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliberationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliberationFormService = TestBed.inject(DeliberationFormService);
    deliberationService = TestBed.inject(DeliberationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deliberation: IDeliberation = { id: 456 };

      activatedRoute.data = of({ deliberation });
      comp.ngOnInit();

      expect(comp.deliberation).toEqual(deliberation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliberation>>();
      const deliberation = { id: 123 };
      jest.spyOn(deliberationFormService, 'getDeliberation').mockReturnValue(deliberation);
      jest.spyOn(deliberationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliberation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliberation }));
      saveSubject.complete();

      // THEN
      expect(deliberationFormService.getDeliberation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliberationService.update).toHaveBeenCalledWith(expect.objectContaining(deliberation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliberation>>();
      const deliberation = { id: 123 };
      jest.spyOn(deliberationFormService, 'getDeliberation').mockReturnValue({ id: null });
      jest.spyOn(deliberationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliberation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliberation }));
      saveSubject.complete();

      // THEN
      expect(deliberationFormService.getDeliberation).toHaveBeenCalled();
      expect(deliberationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliberation>>();
      const deliberation = { id: 123 };
      jest.spyOn(deliberationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliberation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliberationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
