import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CycleService } from '../service/cycle.service';
import { ICycle } from '../cycle.model';
import { CycleFormService } from './cycle-form.service';

import { CycleUpdateComponent } from './cycle-update.component';

describe('Cycle Management Update Component', () => {
  let comp: CycleUpdateComponent;
  let fixture: ComponentFixture<CycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cycleFormService: CycleFormService;
  let cycleService: CycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CycleUpdateComponent],
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
      .overrideTemplate(CycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cycleFormService = TestBed.inject(CycleFormService);
    cycleService = TestBed.inject(CycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cycle: ICycle = { id: 456 };

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(comp.cycle).toEqual(cycle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue(cycle);
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cycleService.update).toHaveBeenCalledWith(expect.objectContaining(cycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue({ id: null });
      jest.spyOn(cycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(cycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cycleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
