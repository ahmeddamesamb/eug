import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeFraisService } from '../service/type-frais.service';
import { ITypeFrais } from '../type-frais.model';
import { TypeFraisFormService } from './type-frais-form.service';

import { TypeFraisUpdateComponent } from './type-frais-update.component';

describe('TypeFrais Management Update Component', () => {
  let comp: TypeFraisUpdateComponent;
  let fixture: ComponentFixture<TypeFraisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeFraisFormService: TypeFraisFormService;
  let typeFraisService: TypeFraisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeFraisUpdateComponent],
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
      .overrideTemplate(TypeFraisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeFraisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeFraisFormService = TestBed.inject(TypeFraisFormService);
    typeFraisService = TestBed.inject(TypeFraisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeFrais: ITypeFrais = { id: 456 };

      activatedRoute.data = of({ typeFrais });
      comp.ngOnInit();

      expect(comp.typeFrais).toEqual(typeFrais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFrais>>();
      const typeFrais = { id: 123 };
      jest.spyOn(typeFraisFormService, 'getTypeFrais').mockReturnValue(typeFrais);
      jest.spyOn(typeFraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFrais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeFrais }));
      saveSubject.complete();

      // THEN
      expect(typeFraisFormService.getTypeFrais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeFraisService.update).toHaveBeenCalledWith(expect.objectContaining(typeFrais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFrais>>();
      const typeFrais = { id: 123 };
      jest.spyOn(typeFraisFormService, 'getTypeFrais').mockReturnValue({ id: null });
      jest.spyOn(typeFraisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFrais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeFrais }));
      saveSubject.complete();

      // THEN
      expect(typeFraisFormService.getTypeFrais).toHaveBeenCalled();
      expect(typeFraisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeFrais>>();
      const typeFrais = { id: 123 };
      jest.spyOn(typeFraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeFrais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeFraisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
