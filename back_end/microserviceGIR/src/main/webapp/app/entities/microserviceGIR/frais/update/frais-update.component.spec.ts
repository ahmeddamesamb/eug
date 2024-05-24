import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeFrais } from 'app/entities/microserviceGIR/type-frais/type-frais.model';
import { TypeFraisService } from 'app/entities/microserviceGIR/type-frais/service/type-frais.service';
import { FraisService } from '../service/frais.service';
import { IFrais } from '../frais.model';
import { FraisFormService } from './frais-form.service';

import { FraisUpdateComponent } from './frais-update.component';

describe('Frais Management Update Component', () => {
  let comp: FraisUpdateComponent;
  let fixture: ComponentFixture<FraisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fraisFormService: FraisFormService;
  let fraisService: FraisService;
  let typeFraisService: TypeFraisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FraisUpdateComponent],
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
      .overrideTemplate(FraisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FraisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fraisFormService = TestBed.inject(FraisFormService);
    fraisService = TestBed.inject(FraisService);
    typeFraisService = TestBed.inject(TypeFraisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeFrais query and add missing value', () => {
      const frais: IFrais = { id: 456 };
      const typeFrais: ITypeFrais = { id: 28979 };
      frais.typeFrais = typeFrais;

      const typeFraisCollection: ITypeFrais[] = [{ id: 29085 }];
      jest.spyOn(typeFraisService, 'query').mockReturnValue(of(new HttpResponse({ body: typeFraisCollection })));
      const additionalTypeFrais = [typeFrais];
      const expectedCollection: ITypeFrais[] = [...additionalTypeFrais, ...typeFraisCollection];
      jest.spyOn(typeFraisService, 'addTypeFraisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(typeFraisService.query).toHaveBeenCalled();
      expect(typeFraisService.addTypeFraisToCollectionIfMissing).toHaveBeenCalledWith(
        typeFraisCollection,
        ...additionalTypeFrais.map(expect.objectContaining),
      );
      expect(comp.typeFraisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const frais: IFrais = { id: 456 };
      const typeFrais: ITypeFrais = { id: 14399 };
      frais.typeFrais = typeFrais;

      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      expect(comp.typeFraisSharedCollection).toContain(typeFrais);
      expect(comp.frais).toEqual(frais);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisFormService, 'getFrais').mockReturnValue(frais);
      jest.spyOn(fraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frais }));
      saveSubject.complete();

      // THEN
      expect(fraisFormService.getFrais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fraisService.update).toHaveBeenCalledWith(expect.objectContaining(frais));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisFormService, 'getFrais').mockReturnValue({ id: null });
      jest.spyOn(fraisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: frais }));
      saveSubject.complete();

      // THEN
      expect(fraisFormService.getFrais).toHaveBeenCalled();
      expect(fraisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFrais>>();
      const frais = { id: 123 };
      jest.spyOn(fraisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ frais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fraisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeFrais', () => {
      it('Should forward to typeFraisService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeFraisService, 'compareTypeFrais');
        comp.compareTypeFrais(entity, entity2);
        expect(typeFraisService.compareTypeFrais).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
