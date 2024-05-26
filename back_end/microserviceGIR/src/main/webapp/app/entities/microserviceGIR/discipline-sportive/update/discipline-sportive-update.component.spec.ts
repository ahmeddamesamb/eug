import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DisciplineSportiveService } from '../service/discipline-sportive.service';
import { IDisciplineSportive } from '../discipline-sportive.model';
import { DisciplineSportiveFormService } from './discipline-sportive-form.service';

import { DisciplineSportiveUpdateComponent } from './discipline-sportive-update.component';

describe('DisciplineSportive Management Update Component', () => {
  let comp: DisciplineSportiveUpdateComponent;
  let fixture: ComponentFixture<DisciplineSportiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disciplineSportiveFormService: DisciplineSportiveFormService;
  let disciplineSportiveService: DisciplineSportiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DisciplineSportiveUpdateComponent],
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
      .overrideTemplate(DisciplineSportiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplineSportiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disciplineSportiveFormService = TestBed.inject(DisciplineSportiveFormService);
    disciplineSportiveService = TestBed.inject(DisciplineSportiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const disciplineSportive: IDisciplineSportive = { id: 456 };

      activatedRoute.data = of({ disciplineSportive });
      comp.ngOnInit();

      expect(comp.disciplineSportive).toEqual(disciplineSportive);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportive>>();
      const disciplineSportive = { id: 123 };
      jest.spyOn(disciplineSportiveFormService, 'getDisciplineSportive').mockReturnValue(disciplineSportive);
      jest.spyOn(disciplineSportiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplineSportive }));
      saveSubject.complete();

      // THEN
      expect(disciplineSportiveFormService.getDisciplineSportive).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(disciplineSportiveService.update).toHaveBeenCalledWith(expect.objectContaining(disciplineSportive));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportive>>();
      const disciplineSportive = { id: 123 };
      jest.spyOn(disciplineSportiveFormService, 'getDisciplineSportive').mockReturnValue({ id: null });
      jest.spyOn(disciplineSportiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportive: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplineSportive }));
      saveSubject.complete();

      // THEN
      expect(disciplineSportiveFormService.getDisciplineSportive).toHaveBeenCalled();
      expect(disciplineSportiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplineSportive>>();
      const disciplineSportive = { id: 123 };
      jest.spyOn(disciplineSportiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplineSportive });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disciplineSportiveService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
