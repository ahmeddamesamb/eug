import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeBourseService } from '../service/type-bourse.service';
import { ITypeBourse } from '../type-bourse.model';
import { TypeBourseFormService } from './type-bourse-form.service';

import { TypeBourseUpdateComponent } from './type-bourse-update.component';

describe('TypeBourse Management Update Component', () => {
  let comp: TypeBourseUpdateComponent;
  let fixture: ComponentFixture<TypeBourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeBourseFormService: TypeBourseFormService;
  let typeBourseService: TypeBourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeBourseUpdateComponent],
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
      .overrideTemplate(TypeBourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeBourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeBourseFormService = TestBed.inject(TypeBourseFormService);
    typeBourseService = TestBed.inject(TypeBourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeBourse: ITypeBourse = { id: 456 };

      activatedRoute.data = of({ typeBourse });
      comp.ngOnInit();

      expect(comp.typeBourse).toEqual(typeBourse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeBourse>>();
      const typeBourse = { id: 123 };
      jest.spyOn(typeBourseFormService, 'getTypeBourse').mockReturnValue(typeBourse);
      jest.spyOn(typeBourseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeBourse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeBourse }));
      saveSubject.complete();

      // THEN
      expect(typeBourseFormService.getTypeBourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeBourseService.update).toHaveBeenCalledWith(expect.objectContaining(typeBourse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeBourse>>();
      const typeBourse = { id: 123 };
      jest.spyOn(typeBourseFormService, 'getTypeBourse').mockReturnValue({ id: null });
      jest.spyOn(typeBourseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeBourse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeBourse }));
      saveSubject.complete();

      // THEN
      expect(typeBourseFormService.getTypeBourse).toHaveBeenCalled();
      expect(typeBourseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeBourse>>();
      const typeBourse = { id: 123 };
      jest.spyOn(typeBourseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeBourse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeBourseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
