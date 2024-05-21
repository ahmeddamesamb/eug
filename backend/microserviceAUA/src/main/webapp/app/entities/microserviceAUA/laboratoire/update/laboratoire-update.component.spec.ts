import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LaboratoireService } from '../service/laboratoire.service';
import { ILaboratoire } from '../laboratoire.model';
import { LaboratoireFormService } from './laboratoire-form.service';

import { LaboratoireUpdateComponent } from './laboratoire-update.component';

describe('Laboratoire Management Update Component', () => {
  let comp: LaboratoireUpdateComponent;
  let fixture: ComponentFixture<LaboratoireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let laboratoireFormService: LaboratoireFormService;
  let laboratoireService: LaboratoireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LaboratoireUpdateComponent],
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
      .overrideTemplate(LaboratoireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LaboratoireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    laboratoireFormService = TestBed.inject(LaboratoireFormService);
    laboratoireService = TestBed.inject(LaboratoireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const laboratoire: ILaboratoire = { id: 456 };

      activatedRoute.data = of({ laboratoire });
      comp.ngOnInit();

      expect(comp.laboratoire).toEqual(laboratoire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILaboratoire>>();
      const laboratoire = { id: 123 };
      jest.spyOn(laboratoireFormService, 'getLaboratoire').mockReturnValue(laboratoire);
      jest.spyOn(laboratoireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratoire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: laboratoire }));
      saveSubject.complete();

      // THEN
      expect(laboratoireFormService.getLaboratoire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(laboratoireService.update).toHaveBeenCalledWith(expect.objectContaining(laboratoire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILaboratoire>>();
      const laboratoire = { id: 123 };
      jest.spyOn(laboratoireFormService, 'getLaboratoire').mockReturnValue({ id: null });
      jest.spyOn(laboratoireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratoire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: laboratoire }));
      saveSubject.complete();

      // THEN
      expect(laboratoireFormService.getLaboratoire).toHaveBeenCalled();
      expect(laboratoireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILaboratoire>>();
      const laboratoire = { id: 123 };
      jest.spyOn(laboratoireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratoire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(laboratoireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
