import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeSelectionService } from '../service/type-selection.service';
import { ITypeSelection } from '../type-selection.model';
import { TypeSelectionFormService } from './type-selection-form.service';

import { TypeSelectionUpdateComponent } from './type-selection-update.component';

describe('TypeSelection Management Update Component', () => {
  let comp: TypeSelectionUpdateComponent;
  let fixture: ComponentFixture<TypeSelectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeSelectionFormService: TypeSelectionFormService;
  let typeSelectionService: TypeSelectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeSelectionUpdateComponent],
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
      .overrideTemplate(TypeSelectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeSelectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeSelectionFormService = TestBed.inject(TypeSelectionFormService);
    typeSelectionService = TestBed.inject(TypeSelectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeSelection: ITypeSelection = { id: 456 };

      activatedRoute.data = of({ typeSelection });
      comp.ngOnInit();

      expect(comp.typeSelection).toEqual(typeSelection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSelection>>();
      const typeSelection = { id: 123 };
      jest.spyOn(typeSelectionFormService, 'getTypeSelection').mockReturnValue(typeSelection);
      jest.spyOn(typeSelectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSelection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSelection }));
      saveSubject.complete();

      // THEN
      expect(typeSelectionFormService.getTypeSelection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeSelectionService.update).toHaveBeenCalledWith(expect.objectContaining(typeSelection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSelection>>();
      const typeSelection = { id: 123 };
      jest.spyOn(typeSelectionFormService, 'getTypeSelection').mockReturnValue({ id: null });
      jest.spyOn(typeSelectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSelection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeSelection }));
      saveSubject.complete();

      // THEN
      expect(typeSelectionFormService.getTypeSelection).toHaveBeenCalled();
      expect(typeSelectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeSelection>>();
      const typeSelection = { id: 123 };
      jest.spyOn(typeSelectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeSelection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeSelectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
