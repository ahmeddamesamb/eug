import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeAdmissionService } from '../service/type-admission.service';
import { ITypeAdmission } from '../type-admission.model';
import { TypeAdmissionFormService } from './type-admission-form.service';

import { TypeAdmissionUpdateComponent } from './type-admission-update.component';

describe('TypeAdmission Management Update Component', () => {
  let comp: TypeAdmissionUpdateComponent;
  let fixture: ComponentFixture<TypeAdmissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeAdmissionFormService: TypeAdmissionFormService;
  let typeAdmissionService: TypeAdmissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeAdmissionUpdateComponent],
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
      .overrideTemplate(TypeAdmissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeAdmissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeAdmissionFormService = TestBed.inject(TypeAdmissionFormService);
    typeAdmissionService = TestBed.inject(TypeAdmissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeAdmission: ITypeAdmission = { id: 456 };

      activatedRoute.data = of({ typeAdmission });
      comp.ngOnInit();

      expect(comp.typeAdmission).toEqual(typeAdmission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeAdmission>>();
      const typeAdmission = { id: 123 };
      jest.spyOn(typeAdmissionFormService, 'getTypeAdmission').mockReturnValue(typeAdmission);
      jest.spyOn(typeAdmissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeAdmission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeAdmission }));
      saveSubject.complete();

      // THEN
      expect(typeAdmissionFormService.getTypeAdmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeAdmissionService.update).toHaveBeenCalledWith(expect.objectContaining(typeAdmission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeAdmission>>();
      const typeAdmission = { id: 123 };
      jest.spyOn(typeAdmissionFormService, 'getTypeAdmission').mockReturnValue({ id: null });
      jest.spyOn(typeAdmissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeAdmission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeAdmission }));
      saveSubject.complete();

      // THEN
      expect(typeAdmissionFormService.getTypeAdmission).toHaveBeenCalled();
      expect(typeAdmissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeAdmission>>();
      const typeAdmission = { id: 123 };
      jest.spyOn(typeAdmissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeAdmission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeAdmissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
