import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeHandicapService } from '../service/type-handicap.service';
import { ITypeHandicap } from '../type-handicap.model';
import { TypeHandicapFormService } from './type-handicap-form.service';

import { TypeHandicapUpdateComponent } from './type-handicap-update.component';

describe('TypeHandicap Management Update Component', () => {
  let comp: TypeHandicapUpdateComponent;
  let fixture: ComponentFixture<TypeHandicapUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeHandicapFormService: TypeHandicapFormService;
  let typeHandicapService: TypeHandicapService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeHandicapUpdateComponent],
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
      .overrideTemplate(TypeHandicapUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeHandicapUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeHandicapFormService = TestBed.inject(TypeHandicapFormService);
    typeHandicapService = TestBed.inject(TypeHandicapService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeHandicap: ITypeHandicap = { id: 456 };

      activatedRoute.data = of({ typeHandicap });
      comp.ngOnInit();

      expect(comp.typeHandicap).toEqual(typeHandicap);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeHandicap>>();
      const typeHandicap = { id: 123 };
      jest.spyOn(typeHandicapFormService, 'getTypeHandicap').mockReturnValue(typeHandicap);
      jest.spyOn(typeHandicapService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeHandicap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeHandicap }));
      saveSubject.complete();

      // THEN
      expect(typeHandicapFormService.getTypeHandicap).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeHandicapService.update).toHaveBeenCalledWith(expect.objectContaining(typeHandicap));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeHandicap>>();
      const typeHandicap = { id: 123 };
      jest.spyOn(typeHandicapFormService, 'getTypeHandicap').mockReturnValue({ id: null });
      jest.spyOn(typeHandicapService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeHandicap: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeHandicap }));
      saveSubject.complete();

      // THEN
      expect(typeHandicapFormService.getTypeHandicap).toHaveBeenCalled();
      expect(typeHandicapService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeHandicap>>();
      const typeHandicap = { id: 123 };
      jest.spyOn(typeHandicapService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeHandicap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeHandicapService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
