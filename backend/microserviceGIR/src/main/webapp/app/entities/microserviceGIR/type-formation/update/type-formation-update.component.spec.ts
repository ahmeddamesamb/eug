import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeFormationService } from '../service/type-formation.service';
import { ITypeFormation } from '../type-formation.model';
import { TypeFormationFormService } from './type-formation-form.service';

import { TypeFormationUpdateComponent } from './type-formation-update.component';

describe('TypeFormation Management Update Component', () => {
  let comp: TypeFormationUpdateComponent;
  let fixture: ComponentFixture<TypeFormationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeFormationFormService: TypeFormationFormService;
  let typeFormationService: TypeFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeFormationUpdateComponent],
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
      .overrideTemplate(TypeFormationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeFormationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeFormationFormService = TestBed.inject(TypeFormationFormService);
    typeFormationService = TestBed.inject(TypeFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeFormation: ITypeFormation = { id: 456 };

      activatedRoute.data = of({ typeFormation });
      comp.ngOnInit();

      expect(comp.typeFormation).toEqual(typeFormation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFormation>>();
      const typeFormation = { id: 123 };
      jest.spyOn(typeFormationFormService, 'getTypeFormation').mockReturnValue(typeFormation);
      jest.spyOn(typeFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeFormation }));
      saveSubject.complete();

      // THEN
      expect(typeFormationFormService.getTypeFormation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeFormationService.update).toHaveBeenCalledWith(expect.objectContaining(typeFormation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFormation>>();
      const typeFormation = { id: 123 };
      jest.spyOn(typeFormationFormService, 'getTypeFormation').mockReturnValue({ id: null });
      jest.spyOn(typeFormationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFormation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeFormation }));
      saveSubject.complete();

      // THEN
      expect(typeFormationFormService.getTypeFormation).toHaveBeenCalled();
      expect(typeFormationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFormation>>();
      const typeFormation = { id: 123 };
      jest.spyOn(typeFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeFormationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
