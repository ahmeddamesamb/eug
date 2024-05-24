import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeRapportService } from '../service/type-rapport.service';
import { ITypeRapport } from '../type-rapport.model';
import { TypeRapportFormService } from './type-rapport-form.service';

import { TypeRapportUpdateComponent } from './type-rapport-update.component';

describe('TypeRapport Management Update Component', () => {
  let comp: TypeRapportUpdateComponent;
  let fixture: ComponentFixture<TypeRapportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeRapportFormService: TypeRapportFormService;
  let typeRapportService: TypeRapportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeRapportUpdateComponent],
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
      .overrideTemplate(TypeRapportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeRapportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeRapportFormService = TestBed.inject(TypeRapportFormService);
    typeRapportService = TestBed.inject(TypeRapportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeRapport: ITypeRapport = { id: 456 };

      activatedRoute.data = of({ typeRapport });
      comp.ngOnInit();

      expect(comp.typeRapport).toEqual(typeRapport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeRapport>>();
      const typeRapport = { id: 123 };
      jest.spyOn(typeRapportFormService, 'getTypeRapport').mockReturnValue(typeRapport);
      jest.spyOn(typeRapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeRapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeRapport }));
      saveSubject.complete();

      // THEN
      expect(typeRapportFormService.getTypeRapport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeRapportService.update).toHaveBeenCalledWith(expect.objectContaining(typeRapport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeRapport>>();
      const typeRapport = { id: 123 };
      jest.spyOn(typeRapportFormService, 'getTypeRapport').mockReturnValue({ id: null });
      jest.spyOn(typeRapportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeRapport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeRapport }));
      saveSubject.complete();

      // THEN
      expect(typeRapportFormService.getTypeRapport).toHaveBeenCalled();
      expect(typeRapportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeRapport>>();
      const typeRapport = { id: 123 };
      jest.spyOn(typeRapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeRapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeRapportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
