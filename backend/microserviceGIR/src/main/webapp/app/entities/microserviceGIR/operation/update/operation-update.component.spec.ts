import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeOperation } from 'app/entities/microserviceGIR/type-operation/type-operation.model';
import { TypeOperationService } from 'app/entities/microserviceGIR/type-operation/service/type-operation.service';
import { OperationService } from '../service/operation.service';
import { IOperation } from '../operation.model';
import { OperationFormService } from './operation-form.service';

import { OperationUpdateComponent } from './operation-update.component';

describe('Operation Management Update Component', () => {
  let comp: OperationUpdateComponent;
  let fixture: ComponentFixture<OperationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationFormService: OperationFormService;
  let operationService: OperationService;
  let typeOperationService: TypeOperationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OperationUpdateComponent],
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
      .overrideTemplate(OperationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationFormService = TestBed.inject(OperationFormService);
    operationService = TestBed.inject(OperationService);
    typeOperationService = TestBed.inject(TypeOperationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeOperation query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const typeOperation: ITypeOperation = { id: 4662 };
      operation.typeOperation = typeOperation;

      const typeOperationCollection: ITypeOperation[] = [{ id: 9620 }];
      jest.spyOn(typeOperationService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOperationCollection })));
      const additionalTypeOperations = [typeOperation];
      const expectedCollection: ITypeOperation[] = [...additionalTypeOperations, ...typeOperationCollection];
      jest.spyOn(typeOperationService, 'addTypeOperationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(typeOperationService.query).toHaveBeenCalled();
      expect(typeOperationService.addTypeOperationToCollectionIfMissing).toHaveBeenCalledWith(
        typeOperationCollection,
        ...additionalTypeOperations.map(expect.objectContaining),
      );
      expect(comp.typeOperationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operation: IOperation = { id: 456 };
      const typeOperation: ITypeOperation = { id: 30305 };
      operation.typeOperation = typeOperation;

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(comp.typeOperationsSharedCollection).toContain(typeOperation);
      expect(comp.operation).toEqual(operation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationFormService, 'getOperation').mockReturnValue(operation);
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationFormService.getOperation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationService.update).toHaveBeenCalledWith(expect.objectContaining(operation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationFormService, 'getOperation').mockReturnValue({ id: null });
      jest.spyOn(operationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationFormService.getOperation).toHaveBeenCalled();
      expect(operationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeOperation', () => {
      it('Should forward to typeOperationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeOperationService, 'compareTypeOperation');
        comp.compareTypeOperation(entity, entity2);
        expect(typeOperationService.compareTypeOperation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
